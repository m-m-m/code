/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.doc;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.mmm.code.api.CodeElement;
import net.sf.mmm.code.api.CodeType;
import net.sf.mmm.code.api.doc.CodeDoc;
import net.sf.mmm.code.api.doc.CodeDocDescriptor;
import net.sf.mmm.code.api.doc.CodeDocFormat;
import net.sf.mmm.code.api.imports.CodeImport;
import net.sf.mmm.code.base.AbstractCodeContext;
import net.sf.mmm.code.base.AbstractCodeItem;

/**
 * Abstract base implementation of {@link CodeDoc}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <C> type of {@link #getContext()}.
 * @since 1.0.0
 */
public abstract class AbstractCodeDoc<C extends AbstractCodeContext<?, ?>> extends AbstractCodeItem<C> implements CodeDoc {

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
  public String get(CodeDocFormat format) {

    StringBuilder buffer = new StringBuilder();
    for (String line : this.lines) {
      buffer.append(line);
      writeNewline(buffer);
    }
    String raw = buffer.toString().trim();
    if (CodeDocFormat.RAW.equals(format) || (format == null)) {
      return raw;
    } else {
      String comment = raw.replace("\n", " ").replace("\r", "");
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
    // format.replaceXmlTag(tag)
    // TODO Auto-generated method stub
    return comment;
  }

  private String replaceDocTags(CodeDocFormat format, String comment) {

    Matcher matcher = getTagPattern().matcher(comment);
    StringBuffer buffer = new StringBuffer();
    while (matcher.find()) {
      String tag = matcher.group(1);
      String text = matcher.group(2);
      String parameter = "";
      if (CodeDoc.TAG_LINK.equals(tag) || CodeDoc.TAG_LINKPLAIN.equals(tag)) {
        CodeDocLink link = parseLink(text);
        String url = resolveLink(link);
        parameter = url;
        text = link.getText();
      } else if (CodeDoc.TAG_VALUE.equals(tag)) {
        text = "" + resolveValue(text);
      }
      String replacement = format.replaceDocTag(tag, parameter, text);
      matcher.appendReplacement(buffer, replacement);
    }
    matcher.appendTail(buffer);
    return buffer.toString();
  }

  /**
   * The {@value CodeDoc#TAG_CODE} is cool.
   *
   * @param text the referenced value. May be empty to reference the value of the current constant field.
   * @return the resolved value.
   */
  protected Object resolveValue(String text) {

    return null;
  }

  /**
   * @param link the {@link CodeDocLink} to resolve.
   * @return the {@link CodeDocLink} resolved as URL.
   */
  protected String resolveLink(CodeDocLink link) {

    String qualifiedName = link.getQualifiedName();
    AbstractCodeContext<?, ?> context = getContext();
    char packageSeparator = context.getPackageSeparator();
    for (CodeDocDescriptor descriptor : context.getDocDescriptors()) {
      if (qualifiedName.startsWith(descriptor.getPackagePrefix() + packageSeparator)) {
        return resolveLink(descriptor.getUrl(), link, true);
      }
    }
    return resolveLinkRelative(link);
  }

  /**
   * @param url the base URL or relative path.
   * @param link the {@link CodeDocLink}.
   * @param absolute - {@code true} if the {@link CodeDocLink#getQualifiedName() qualified name} should be
   *        appended as path to the URL, {@code false} otherwise.
   * @return the resolved URL.
   */
  protected String resolveLink(String url, CodeDocLink link, boolean absolute) {

    StringBuilder buffer = new StringBuilder(url);
    if (absolute) {
      if (!url.endsWith("/")) {
        buffer.append('/');
      }
      String path = link.getQualifiedName();
      char separator = getContext().getPackageSeparator();
      if (separator != '/') {
        path = path.replace(separator, '/');
      }
      buffer.append(path);
      buffer.append(".html");
    }
    String anchor = link.getAnchor();
    if (anchor != null) {
      buffer.append('#');
      buffer.append(anchor);
    }
    return buffer.toString();
  }

  /**
   * @param link the {@link CodeDocLink} to resolve.
   * @return the path relative to this context.
   */
  protected String resolveLinkRelative(CodeDocLink link) {

    CodeType owningType = getOwningType(this.element);
    Path qualifiedSource = createPath(owningType.getQualifiedName());
    Path qualifiedTarget = createPath(link.getQualifiedName());
    Path relativePath = qualifiedSource.relativize(qualifiedTarget);
    return relativePath.toString();
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

  /**
   * @param value the raw text of the link tag containing the link followed by the optional title text.
   * @return the parsed {@link CodeDocLink}.
   */
  protected CodeDocLink parseLink(String value) {

    int spaceIndex = value.indexOf(' ');
    String link;
    String text = null;
    if (spaceIndex > 0) {
      link = value.substring(0, spaceIndex);
      text = value.substring(spaceIndex + 1);
    } else {
      link = value;
    }
    int hashIndex = link.indexOf('#');
    String type = null;
    String anchor;
    if (hashIndex >= 0) {
      anchor = link.substring(hashIndex + 1);
      if (hashIndex > 0) {
        type = link.substring(0, hashIndex);
      }
    } else {
      anchor = null;
      type = link;
    }
    String simpleName;
    String qualifiedName;
    if (type == null) {
      CodeType owningType = getOwningType(this.element);
      simpleName = owningType.getSimpleName();
      qualifiedName = owningType.getQualifiedName();
    } else {
      char separator = getContext().getPackageSeparator();
      int separatorIndex = type.lastIndexOf(separator);
      if (separatorIndex > 0) {
        simpleName = type.substring(separatorIndex + 1);
        qualifiedName = type;
      } else {
        simpleName = type;
        qualifiedName = qualify(type);
      }
    }
    if (text == null) {
      text = simpleName;
      if (anchor != null) {
        text = text + '.' + anchor;
      }
    }
    return new CodeDocLink(simpleName, qualifiedName, anchor, text);
  }

  /**
   * @param simpleName the simple name to qualify.
   * @return the qualified name.
   */
  protected String qualify(String simpleName) {

    CodeType owningType = getOwningType(this.element);
    if (owningType.getSimpleName().equals(simpleName)) {
      return owningType.getQualifiedName();
    }
    List<CodeImport> imports = owningType.getFile().getImports();
    char separator = getContext().getPackageSeparator();
    String suffix = separator + simpleName;
    for (CodeImport imp : imports) {
      if (!imp.isStatic()) {
        String source = imp.getSource();
        if (source.endsWith(suffix)) {
          return source;
        }
      }
    }
    String qname = qualifyStandardType(simpleName);
    if (qname != null) {
      return qname;
    }
    String pkgName = owningType.getParentPackage().getQualifiedName();
    if (pkgName.isEmpty()) {
      return simpleName;
    }
    return pkgName + separator + simpleName;
  }

  /**
   * @param simpleName the {@link CodeType#getSimpleName() simple name} of the {@link CodeType}.
   * @return the corresponding {@link CodeType#getQualifiedName() qualified name} or {@code null} if no
   *         standard type (import is required).
   */
  protected abstract String qualifyStandardType(String simpleName);

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
