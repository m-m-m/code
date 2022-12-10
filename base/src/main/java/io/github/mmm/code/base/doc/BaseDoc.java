/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.doc;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.code.api.CodeContext;
import io.github.mmm.code.api.arg.CodeException;
import io.github.mmm.code.api.arg.CodeParameter;
import io.github.mmm.code.api.arg.CodeReturn;
import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.copy.CodeCopyType;
import io.github.mmm.code.api.doc.CodeDoc;
import io.github.mmm.code.api.doc.CodeDocFormat;
import io.github.mmm.code.api.doc.CodeDocLink;
import io.github.mmm.code.api.doc.CodeDocMethodLink;
import io.github.mmm.code.api.doc.Tag;
import io.github.mmm.code.api.doc.XmlTag;
import io.github.mmm.code.api.element.CodeElement;
import io.github.mmm.code.api.expression.CodeConstant;
import io.github.mmm.code.api.expression.CodeExpression;
import io.github.mmm.code.api.language.CodeLanguage;
import io.github.mmm.code.api.member.CodeField;
import io.github.mmm.code.api.member.CodeMethod;
import io.github.mmm.code.api.merge.CodeMergeStrategy;
import io.github.mmm.code.api.type.CodeGenericType;
import io.github.mmm.code.api.type.CodeType;
import io.github.mmm.code.api.type.CodeTypeVariable;
import io.github.mmm.code.base.element.BaseElement;
import io.github.mmm.code.base.member.BaseOperation;
import io.github.mmm.code.base.node.BaseNodeItem;
import io.github.mmm.code.base.type.BaseType;

/**
 * Base implementation of {@link CodeDoc}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseDoc extends BaseNodeItem implements CodeDoc {

  private static final Logger LOG = LoggerFactory.getLogger(BaseDoc.class);

  private static final Pattern PATTERN_XML_TAG = Pattern.compile("<[/]?([a-zA-Z_][a-zA-Z0-9:._-]*)([/]?|\\s([^>]*)*)>");

  private static final Pattern PATTERN_INLINE_JAVADOC_TAG = Pattern.compile("\\{@([a-zA-Z]+) ([^}]*)\\}");

  private static final Set<String> HTML_SELF_CLOSING_TAGS = new HashSet<>(Arrays.asList("area", "base", "br", "col", "command", "embed",
      "hr", "img", "input", "keygen", "link", "meta", "param", "source", "track", "wbr"));

  private final BaseElement parent;

  private List<String> lines;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent element}.
   */
  public BaseDoc(BaseElement parent) {

    super();
    this.parent = parent;
    this.lines = new ArrayList<>();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseDoc} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseDoc(BaseDoc template, CodeCopyMapper mapper) {

    super();
    this.parent = mapper.map(template.parent, CodeCopyType.PARENT);
    this.lines = new ArrayList<>(template.lines);
  }

  @Override
  public BaseElement getParent() {

    return this.parent;
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
    if (this.parent == null) {
      return;
    }
    CodeElement sourceElement = this.parent.getSourceCodeObject();
    if (sourceElement != null) {
      this.lines.addAll(sourceElement.getDoc().getLines());
    }
  }

  @Override
  protected void doSetImmutable() {

    super.doSetImmutable();
    this.lines = Collections.unmodifiableList(this.lines);
  }

  @Override
  protected boolean isSystemImmutable() {

    boolean systemImmutable = super.isSystemImmutable();
    if (!systemImmutable) {
      systemImmutable = isSystemImmutable(getParent());
    }
    return systemImmutable;
  }

  @Override
  public List<String> getLines() {

    initialize();
    return this.lines;
  }

  @Override
  public void add(Collection<String> textLines) {

    if ((textLines == null) || textLines.isEmpty()) {
      return;
    }
    verifyMutalbe();
    initialize();
    this.lines.addAll(textLines);
  }

  @Override
  public void add(String... textLines) {

    if ((textLines == null) || (textLines.length == 0)) {
      return;
    }
    verifyMutalbe();
    initialize();
    for (String line : textLines) {
      this.lines.add(line);
    }
  }

  @Override
  public boolean isEmpty() {

    return getLines().isEmpty();
  }

  @Override
  public String getFormatted(CodeDocFormat format, String newline) {

    StringBuilder buffer = new StringBuilder();
    for (String line : getLines()) {
      buffer.append(line);
      buffer.append(newline);
    }
    String raw = buffer.toString().trim();
    if (CodeDocFormat.RAW.equals(format) || (format == null)) {
      return raw;
    } else {
      String comment = raw;
      comment = replaceDocTags(format, comment);
      comment = replaceXmlTags(format, comment);
      return comment;
    }
  }

  /**
   * @return the {@link Pattern} to detect an inline documentation tag.
   */
  protected Pattern getInlineTagPattern() {

    return PATTERN_INLINE_JAVADOC_TAG;
  }

  private String replaceXmlTags(CodeDocFormat format, String comment) {

    if (!format.isReplaceXmlTags()) {
      return comment;
    }
    Matcher matcher = PATTERN_XML_TAG.matcher(comment);
    StringBuffer buffer = new StringBuffer();
    Tag tag = null;
    while (matcher.find()) {
      String markup = matcher.group(0);
      String tagName = matcher.group(1);
      String attributes = "";
      if (matcher.groupCount() >= 3) {
        attributes = matcher.group(3);
      }
      Tag newTag = createTag(tag, markup, tagName, attributes);
      String replacement = format.replaceXmlTag(newTag);
      if (newTag.isOpening()) {
        if (!newTag.isClosing()) {
          tag = newTag;
        }
      } else if (newTag.isClosing()) {
        if (tag != null) {
          if (!tag.getName().equals(newTag.getName())) {
            boolean warn = true;
            if (HTML_SELF_CLOSING_TAGS.contains(tag.getName())) {
              Tag parentTag = tag.getParent();
              if (parentTag != null) {
                tag = parentTag;
                if (tag.getName().equals(newTag.getName())) {
                  warn = false;
                }
              }
            }
            if (warn) {
              LOG.warn("Malformed HTML at {}: closing tag {} does not match opening tag {}.", getParent(), newTag.getName(), tag.getName());
            }
          }
          tag = tag.getParent();
        }
      }
      matcher.appendReplacement(buffer, replacement);
    }
    matcher.appendTail(buffer);
    return buffer.toString();
  }

  private Tag createTag(Tag tag, String markup, String tagName, String attributes) {

    String name = tagName.toLowerCase();
    boolean opening;
    boolean closing;
    if (markup.startsWith("</")) {
      opening = false;
      closing = true;
      if ((attributes != null) && !attributes.isEmpty()) {
        LOG.warn("Illegal markup (closing tag should not have attributes): {}", markup);
      }
    } else if (markup.endsWith("/>")) {
      opening = true;
      closing = true;
    } else {
      opening = true;
      closing = false;
    }
    return new XmlTag(name, opening, closing, attributes, tag);
  }

  private String replaceDocTags(CodeDocFormat format, String comment) {

    Matcher matcher = getInlineTagPattern().matcher(comment);
    StringBuffer buffer = new StringBuffer();
    while (matcher.find()) {
      String tag = matcher.group(1);
      String text = matcher.group(2);
      Supplier<CodeDocLink> link = null;
      if (CodeDoc.TAG_LINK.equals(tag) || CodeDoc.TAG_LINKPLAIN.equals(tag)) {
        link = () -> resolveLink(text);
      } else if (CodeDoc.TAG_VALUE.equals(tag)) {
        link = () -> resolveLink(text);
      }
      String replacement = format.replaceDocTag(tag, link, text);
      matcher.appendReplacement(buffer, replacement);
    }
    matcher.appendTail(buffer);
    return buffer.toString();
  }

  private CodeDocLink resolveLink(String text) {

    CodeType owningType = getOwningType(getParent());
    return new BaseDocLink(text, getLanguage().getPackageSeparator(), owningType.getQualifiedName(), this::resolveLinkUrl,
        this::resolveLinkValue);
  }

  /**
   * @param link the {@link BaseDocLink} to resolve as value (e.g. to resolve "&#64;{value Foo#BAR}" or "&#64;{value}").
   * @return the resolved value.
   */
  protected Object resolveLinkValue(CodeDocLink link) {

    String qualifiedName = resolveLinkQualifiedName(link);
    String anchor = link.getAnchor();
    CodeElement element = getParent();
    CodeField linkedField;
    if (qualifiedName.isEmpty() && (anchor == null)) {
      if (element instanceof CodeField) {
        linkedField = (CodeField) element;
      } else {
        linkedField = null;
      }
    } else {
      if (anchor == null) {
        linkedField = null;
      } else {
        CodeGenericType linkedType = getContext().getType(qualifiedName);
        if (!(linkedType instanceof CodeType)) {
          LOG.warn("Failed to resolve type {}.", qualifiedName);
          return null;
        }
        CodeDocMethodLink methodLink = link.getMethodLink();
        if (methodLink == null) {
          linkedField = linkedType.asType().getFields().get(anchor);
        } else {
          linkedField = null;
        }
      }
    }
    if ((linkedField == null) || !linkedField.getModifiers().isStatic()) {
      LOG.warn("Can only resolve value in constant fields: {}", link);
      return null;
    } else {
      CodeExpression initializer = linkedField.getInitializer();
      if (initializer != null) {
        CodeConstant constant = initializer.evaluate();
        if (constant != null) {
          return constant.getValue();
        }
      }
      return null;
    }
  }

  /**
   * @param link the {@link BaseDocLink} to resolve.
   * @return the {@link BaseDocLink} resolved as URL.
   */
  protected String resolveLinkUrl(CodeDocLink link) {

    String qualifiedName = resolveLinkQualifiedName(link);
    CodeGenericType type = getContext().getType(qualifiedName);
    if (type != null) {
      String docUrl = type.getSource().getDescriptor().getDocUrl();
      if (docUrl != null) {
        return resolveLinkUrl(docUrl, link, true);
      }
    }
    return resolveLinkRelative(link);
  }

  /**
   * @param link the {@link BaseDocLink} to resolve.
   * @return the {@link BaseDocLink#getQualifiedName() qualified name} of the {@link BaseDocLink}. In case it is
   *         {@code null} the resolved qualified name from the {@link BaseDocLink#getSimpleName() simple name}.
   */
  protected String resolveLinkQualifiedName(CodeDocLink link) {

    String qualifiedName = link.getQualifiedName();
    if (qualifiedName == null) {
      if (link.getSimpleName().isEmpty()) {
        return "";
      }
      CodeType owningType = getOwningType(getParent());
      qualifiedName = getContext().getQualifiedName(link.getSimpleName(), owningType, false);
    }
    return qualifiedName;
  }

  /**
   * @param url the base URL or relative path.
   * @param link the {@link BaseDocLink}.
   * @param absolute - {@code true} if the {@link BaseDocLink#getQualifiedName() qualified name} should be appended as
   *        path to the URL, {@code false} otherwise.
   * @return the resolved URL.
   */
  protected String resolveLinkUrl(String url, CodeDocLink link, boolean absolute) {

    StringBuilder buffer = new StringBuilder(url);
    CodeContext context = getContext();
    char separator = getLanguage().getPackageSeparator();
    if (absolute) {
      if (!url.endsWith("/")) {
        buffer.append('/');
      }
      String path = link.getQualifiedName();
      if (separator != '/') {
        path = path.replace(separator, '/');
      }
      buffer.append(path);
    }
    buffer.append(".html");
    String anchor = link.getAnchor();
    if (anchor != null) {
      buffer.append('#');
      CodeDocMethodLink methodLink = link.getMethodLink();
      if (methodLink == null) {
        buffer.append(anchor);
      } else {
        buffer.append(methodLink.getName());
        buffer.append('-');
        List<String> parameters = methodLink.getParameters();
        if (parameters.isEmpty()) {
          buffer.append('-');
        } else {
          CodeType owningType = getOwningType(getParent());
          for (String arg : parameters) {
            if (arg.indexOf(separator) < 0) {
              arg = context.getQualifiedName(arg, owningType, false);
            }
            buffer.append(arg);
            buffer.append('-');
          }
        }
      }
    }
    return buffer.toString();
  }

  /**
   * @param link the {@link BaseDocLink} to resolve.
   * @return the path relative to this context.
   */
  protected String resolveLinkRelative(CodeDocLink link) {

    CodeType owningType = getOwningType(getParent());
    Path qualifiedSource = createPath(owningType.getParentPackage().getQualifiedName());
    Path qualifiedTarget = createPath(resolveLinkQualifiedName(link));
    Path relativePath = qualifiedSource.relativize(qualifiedTarget);
    String path = relativePath.toString().replace('\\', '/');
    if (!path.startsWith(".")) {
      path = "./" + path;
    }
    return resolveLinkUrl(path, link, false);
  }

  /**
   * @param qualifiedName the qualified name of a type.
   * @return the {@link Path}.
   */
  private Path createPath(String qualifiedName) {

    String separatorString;
    char separator = getLanguage().getPackageSeparator();
    if (separator == '.') {
      separatorString = "\\.";
    } else {
      separatorString = Character.toString(separator);
    }
    String[] segments = qualifiedName.split(separatorString);
    return Paths.get(".", segments);
  }

  @Override
  public CodeDoc merge(CodeDoc other, CodeMergeStrategy strategy) {

    if (strategy == CodeMergeStrategy.KEEP) {
      return this;
    }
    List<String> myLines = getLines();
    List<String> otherLines = other.getLines();
    if (strategy == CodeMergeStrategy.OVERRIDE) {
      myLines.clear();
    }
    if (myLines.isEmpty()) {
      myLines.addAll(otherLines);
    } else if (!otherLines.isEmpty()) {
      // TODO merge without duplicating?
    }
    return this;
  }

  @Override
  public BaseDoc copy() {

    return copy(getDefaultCopyMapper());
  }

  @Override
  public BaseDoc copy(CodeCopyMapper mapper) {

    return new BaseDoc(this, mapper);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language)
      throws IOException {

    int size = getLines().size();
    BaseOperation operation = null;
    List<? extends CodeTypeVariable> typeParameters = null;
    CodeElement element = getParent();
    if (element instanceof BaseOperation) {
      operation = (BaseOperation) element;
      typeParameters = operation.getTypeParameters().getDeclared();
      size = size + typeParameters.size();
    } else if (element instanceof BaseType) {
      typeParameters = ((BaseType) element).getTypeParameters().getDeclared();
      size = size + typeParameters.size();
    }
    if ((size == 0) && (operation == null)) {
      return;
    } else if ((size == 1) && (operation == null)) {
      sink.append(currentIndent);
      sink.append("/**");
      sink.append(' ');
      if (this.lines.isEmpty()) {
        doWriteTypeParameters(sink, "", "", typeParameters);
      } else {
        sink.append(this.lines.get(0).trim());
      }
      sink.append(" */");
      sink.append(newline);
    } else {
      boolean docStarted = false;
      if (size > 0) {
        writeDocStart(sink, newline, currentIndent);
        docStarted = true;
        for (String line : this.lines) {
          doWriteLine(sink, newline, currentIndent, docStarted, "", line);
        }
        if (typeParameters != null) {
          doWriteTypeParameters(sink, newline, currentIndent, typeParameters);
        }
      }
      if (operation != null) {
        for (CodeParameter arg : operation.getParameters()) {
          String tag = "@param " + arg.getName();
          docStarted = doWriteElement(sink, newline, currentIndent, docStarted, arg, tag, "      ");
        }
        for (CodeException ex : operation.getExceptions()) {
          String tag = "@throws " + ex.getType().asType().getSimpleName();
          docStarted = doWriteElement(sink, newline, currentIndent, docStarted, ex, tag, "       ");
        }
        if (operation instanceof CodeMethod) {
          CodeReturn result = ((CodeMethod) operation).getReturns();
          if (!result.getType().asType().isVoid()) {
            String tag = "@return";
            docStarted = doWriteElement(sink, newline, currentIndent, docStarted, result, tag, "       ");
          }
        }
      }
      if (docStarted) {
        sink.append(currentIndent);
        sink.append(" */");
        sink.append(newline);
      }
    }
  }

  private void writeDocStart(Appendable sink, String newline, String currentIndent) throws IOException {

    sink.append(currentIndent);
    sink.append("/**");
    sink.append(newline);
  }

  private void doWriteTypeParameters(Appendable sink, String newline, String currentIndent, List<? extends CodeTypeVariable> typeParameters)
      throws IOException {

    for (CodeTypeVariable variable : typeParameters) {
      if (!variable.isWildcard()) {
        String tag = "@param <" + variable.getName() + ">";
        doWriteElement(sink, newline, currentIndent, true, variable, tag, "      ");
      }
    }
  }

  private boolean doWriteElement(Appendable sink, String newline, String currentIndent, boolean docStarted, CodeElement element, String tag,
      String spaces) throws IOException {

    List<String> docLines = element.getDoc().getLines();
    if (docLines.isEmpty()) {
      return docStarted;
    }
    String prefix = tag;
    for (String line : docLines) {
      doWriteLine(sink, newline, currentIndent, docStarted, prefix, line);
      prefix = spaces;
    }
    return true;
  }

  private void doWriteLine(Appendable sink, String newline, String currentIndent, boolean docStarted, String prefix, String line)
      throws IOException {

    if (!docStarted) {
      writeDocStart(sink, newline, currentIndent);
    }
    sink.append(currentIndent);
    sink.append(" * ");
    sink.append(prefix);
    String trimmed = line.trim();
    if (!trimmed.isEmpty()) {
      if (!prefix.isEmpty()) {
        sink.append(' ');
      }
      sink.append(trimmed);
    }
    sink.append(newline);
  }

}
