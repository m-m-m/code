/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.doc;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.CodeElement;
import net.sf.mmm.code.api.CodeType;
import net.sf.mmm.code.api.doc.CodeDoc;
import net.sf.mmm.code.api.doc.CodeDocDescriptor;
import net.sf.mmm.code.api.doc.CodeDocFormat;
import net.sf.mmm.code.api.doc.CodeDocLink;
import net.sf.mmm.code.api.doc.CodeDocMethodLink;
import net.sf.mmm.code.api.doc.Tag;
import net.sf.mmm.code.api.doc.XmlTag;
import net.sf.mmm.code.api.expression.CodeExpression;
import net.sf.mmm.code.api.member.CodeField;
import net.sf.mmm.code.base.AbstractCodeContext;
import net.sf.mmm.code.base.BasicCodeItem;

/**
 * Abstract base implementation of {@link CodeDoc}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <C> type of {@link #getContext()}.
 * @since 1.0.0
 */
public abstract class AbstractCodeDoc<C extends AbstractCodeContext<?, ?>> extends BasicCodeItem<C> implements CodeDoc {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractCodeDoc.class);

  private static final Pattern PATTERN_XML_TAG = Pattern.compile("<[/]?([a-zA-Z_][a-zA-Z0-9:._-]*)([/]?|\\s([^>]*)*)>");

  private static final Set<String> HTML_SELF_CLOSING_TAGS = new HashSet<>(
      Arrays.asList("area", "base", "br", "col", "command", "embed", "hr", "img", "input", "keygen", "link", "meta", "param", "source", "track", "wbr"));

  private final CodeElement element;

  private List<String> lines;

  /**
   * The constructor.
   *
   * @param context the {@link #getContext() context}.
   * @param element the owning {@link CodeElement}.
   */
  public AbstractCodeDoc(C context, CodeElement element) {

    super(context);
    this.element = element;
    this.lines = new ArrayList<>();
  }

  /**
   * @return the owning {@link CodeElement}.
   */
  public CodeElement getElement() {

    return this.element;
  }

  @Override
  public List<String> getLines() {

    return this.lines;
  }

  @Override
  public boolean isEmpty() {

    return this.lines.isEmpty();
  }

  @Override
  public String getFormatted(CodeDocFormat format) {

    StringBuilder buffer = new StringBuilder();
    for (String line : this.lines) {
      buffer.append(line);
      writeNewline(buffer);
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
   * @return the {@link Pattern} to detect a documentation tag.
   */
  protected abstract Pattern getTagPattern();

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
              Tag parent = tag.getParent();
              if (parent != null) {
                tag = parent;
                if (tag.getName().equals(newTag.getName())) {
                  warn = false;
                }
              }
            }
            if (warn) {
              LOG.warn("Malformed HTML at {}: closing tag {} does not match opening tag {}.", getElement(), newTag.getName(), tag.getName());
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

  /**
   * @param tag
   * @param markup
   * @param tagName
   * @param attributes
   * @return
   */
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

    Matcher matcher = getTagPattern().matcher(comment);
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

    CodeType owningType = getOwningType(this.element);
    return new CodeDocLinkImpl(text, getContext().getPackageSeparator(), owningType.getQualifiedName(), this::resolveLinkUrl, this::resolveLinkValue);
  }

  /**
   * @param link the {@link CodeDocLinkImpl} to resolve as value (e.g. to resolve "&#64;{value Foo#BAR}" or
   *        "&#64;{value}").
   * @return the resolved value.
   */
  protected Object resolveLinkValue(CodeDocLink link) {

    C context = getContext();
    String qualifiedName = resolveLinkQualifiedName(link);
    String anchor = link.getAnchor();
    CodeField linkedField;
    if (qualifiedName.isEmpty() && (anchor == null)) {
      if (this.element instanceof CodeField) {
        linkedField = (CodeField) this.element;
      } else {
        linkedField = null;
      }
    } else {
      CodeType linkedType = context.getType(qualifiedName);
      if (linkedType == null) {
        LOG.warn("Failed to resolve type {}.", qualifiedName);
        return null;
      }
      if (anchor == null) {
        linkedField = null;
      } else {
        CodeDocMethodLink methodLink = link.getMethodLink();
        if (methodLink == null) {
          linkedField = linkedType.getField(anchor);
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
        return initializer.evaluate(getOwningType(this.element));
      }
      return null;
    }
  }

  /**
   * @param link the {@link CodeDocLinkImpl} to resolve.
   * @return the {@link CodeDocLinkImpl} resolved as URL.
   */
  protected String resolveLinkUrl(CodeDocLink link) {

    String qualifiedName = resolveLinkQualifiedName(link);
    AbstractCodeContext<?, ?> context = getContext();
    char packageSeparator = context.getPackageSeparator();
    for (CodeDocDescriptor descriptor : context.getDocDescriptors()) {
      if (qualifiedName.startsWith(descriptor.getPackagePrefix() + packageSeparator)) {
        return resolveLinkUrl(descriptor.getUrl(), link, true);
      }
    }
    return resolveLinkRelative(link);
  }

  /**
   * @param link the {@link CodeDocLinkImpl} to resolve.
   * @return the {@link CodeDocLinkImpl#getQualifiedName() qualified name} of the {@link CodeDocLinkImpl}. In
   *         case it is {@code null} the resolved qualified name from the
   *         {@link CodeDocLinkImpl#getSimpleName() simple name}.
   */
  protected String resolveLinkQualifiedName(CodeDocLink link) {

    String qualifiedName = link.getQualifiedName();
    if (qualifiedName == null) {
      if (link.getSimpleName().isEmpty()) {
        return "";
      }
      CodeType owningType = getOwningType(this.element);
      qualifiedName = getContext().getQualifiedName(link.getSimpleName(), owningType, false);
    }
    return qualifiedName;
  }

  /**
   * @param url the base URL or relative path.
   * @param link the {@link CodeDocLinkImpl}.
   * @param absolute - {@code true} if the {@link CodeDocLinkImpl#getQualifiedName() qualified name} should be
   *        appended as path to the URL, {@code false} otherwise.
   * @return the resolved URL.
   */
  protected String resolveLinkUrl(String url, CodeDocLink link, boolean absolute) {

    StringBuilder buffer = new StringBuilder(url);
    C context = getContext();
    char separator = context.getPackageSeparator();
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
          CodeType owningType = getOwningType(this.element);
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
   * @param link the {@link CodeDocLinkImpl} to resolve.
   * @return the path relative to this context.
   */
  protected String resolveLinkRelative(CodeDocLink link) {

    CodeType owningType = getOwningType(this.element);
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
    char separator = getContext().getPackageSeparator();
    if (separator == '.') {
      separatorString = "\\.";
    } else {
      separatorString = Character.toString(separator);
    }
    String[] segments = qualifiedName.split(separatorString);
    return Paths.get(".", segments);
  }

  @Override
  protected void doWrite(Appendable sink, String defaultIndent, String currentIndent) throws IOException {

    int size = this.lines.size();
    if (size == 0) {
      return;
    }
    sink.append(currentIndent);
    sink.append("/**");
    if (size == 1) {
      sink.append(' ');
      sink.append(this.lines.get(0).trim());
    } else {
      writeNewline(sink);
      for (String line : this.lines) {
        sink.append(currentIndent);
        sink.append(" * ");
        sink.append(line.trim());
        writeNewline(sink);
      }
      sink.append(currentIndent);
    }
    sink.append(" */");
    writeNewline(sink);
  }

}
