/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.doc;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.mmm.code.api.doc.CodeDoc;
import net.sf.mmm.code.api.doc.CodeDocFormat;
import net.sf.mmm.code.api.doc.EmptyCodeDoc;

/**
 * Context for conversion of {@link CodeDocImpl code documentation}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class CodeDocContext {

  private final char separator;

  private final List<CodeDocDescriptor> docDescriptors;

  /**
   * The constructor.
   *
   * @param separator the package separator character. For Java use '.'.
   * @param docDescriptors the {@link List} of {@link CodeDocDescriptor}s.
   */
  public CodeDocContext(char separator, List<CodeDocDescriptor> docDescriptors) {

    super();
    this.separator = separator;
    this.docDescriptors = docDescriptors;
  }

  /**
   * @return the {@link Class#getSimpleName() simple name} of the {@link java.lang.reflect.Type} owning the
   *         {@link CodeDoc}.
   */
  protected abstract String getSimpleName();

  /**
   * @return the {@link Pattern} to detect a documentation tag.
   */
  protected abstract Pattern getTagPattern();

  /**
   * @param doc the {@link CodeDoc}.
   * @param format the desired format.
   * @return the documentation converted to the requested format.
   */
  public String getConverted(CodeDoc doc, CodeDocFormat format) {

    String comment = doc.get(CodeDocFormat.RAW).trim().replace("\n", " ").replace("\r", "");
    comment = replaceDocTags(format, comment);
    comment = replaceXmlTags(format, comment);
    return comment;
  }

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
    for (CodeDocDescriptor descriptor : this.docDescriptors) {
      if (qualifiedName.startsWith(descriptor.getPackagePrefix() + this.separator)) {
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
      if (this.separator != '/') {
        path = path.replace(this.separator, '/');
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

    Path qualifiedSource = createPath(qualify(getSimpleName()));
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
    if (this.separator == '.') {
      separatorString = "\\.";
    } else {
      separatorString = Character.toString(this.separator);
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
      simpleName = getSimpleName();
      qualifiedName = qualify(simpleName);
    } else {
      int separatorIndex = type.lastIndexOf(this.separator);
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
  protected abstract String qualify(String simpleName);

  /**
   * @param lines the the {@link CodeDocFormat#RAW raw} documentation. May be {@code null}.
   * @return the wrapped {@link CodeDoc}.
   */
  public CodeDoc createDoc(String... lines) {

    if ((lines == null) || (lines.length == 0)) {
      return EmptyCodeDoc.INSTANCE;
    }
    CodeDocImpl doc = new CodeDocImpl(this);
    List<String> list = doc.getLines();
    for (String line : lines) {
      list.add(line);
    }
    return doc;
  }

}
