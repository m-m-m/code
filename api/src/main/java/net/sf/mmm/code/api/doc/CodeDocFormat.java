/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.doc;

import net.sf.mmm.util.xml.base.XmlUtilImpl;

/**
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class CodeDocFormat {

  /** {@link CodeDocFormat} for documentation as HTML fragment. */
  public static final CodeDocFormat HTML = new CodeDocFormatHtml();

  /** {@link CodeDocFormat} for documentation as AsciiDoc fragment. */
  public static final CodeDocFormat ASCII_DOC = new CodeDocFormatHtml();

  /** {@link CodeDocFormat} for documentation as plain text (all markup removed). */
  public static final CodeDocFormat PLAIN_TEXT = new CodeDocFormatPlainText();

  /**
   * {@link CodeDocFormat} for documentation as raw text in its original source format (e.g. JavaDoc or
   * JSDoc).
   */
  public static final CodeDocFormat RAW = new CodeDocFormatRaw();

  private static final String CODE_START = "<code>";

  private static final String CODE_END = "</code>";

  /**
   * @return {@code true} if XML tags shall be parsed and {@link #replaceXmlTag(Tag) replaced}, {@code false}
   *         otherwise (if they shall be kept unmodified).
   */
  public abstract boolean isReplaceXmlTags();

  /**
   * @param tag the XML {@link Tag} to replace.
   * @return the replacement.
   */
  public abstract String replaceXmlTag(Tag tag);

  /**
   * @param tag the documentation tag such as {@link CodeDoc#TAG_CODE} or {@link CodeDoc#TAG_LINK}.
   * @param parameter the optional parameter. In case of {@link CodeDoc#TAG_LINK} or
   *        {@link CodeDoc#TAG_LINKPLAIN} this will be the resolved URL.
   * @param text the plain text.
   * @return the tag converted to this format.
   */
  public abstract String replaceDocTag(String tag, String parameter, String text);

  @Override
  public abstract String toString();

  /** {@link CodeDocFormat} for {@link CodeDocFormat#HTML}. */
  protected static class CodeDocFormatHtml extends CodeDocFormat {

    @Override
    public boolean isReplaceXmlTags() {

      return false;
    }

    @Override
    public String replaceXmlTag(Tag tag) {

      throw new IllegalStateException();
    }

    @Override
    public String replaceDocTag(String tag, String parameter, String text) {

      StringBuilder buffer = new StringBuilder(text.length() + 16);
      if (CodeDoc.TAG_LINK.equals(tag)) {
        buffer.append(CODE_START);
        appendLink(buffer, parameter, text);
        buffer.append(CODE_END);
      } else if (CodeDoc.TAG_LINKPLAIN.equals(tag)) {
        appendLink(buffer, parameter, text);
      } else if (CodeDoc.TAG_CODE.equals(tag)) {
        buffer.append(CODE_START);
        appendText(buffer, text);
        buffer.append(CODE_END);
      } else if (CodeDoc.TAG_LITERAL.equals(tag)) {
        appendText(buffer, text);
      } else if (CodeDoc.TAG_VALUE.equals(tag)) {
        appendText(buffer, text);
      } else {
        // unknown tag...
        appendText(buffer, parameter);
        if (buffer.length() > 0) {
          buffer.append(' ');
        }
        appendText(buffer, text);
      }
      return buffer.toString();
    }

    private void appendLink(StringBuilder buffer, String url, String text) {

      buffer.append("<a href=");
      buffer.append(url);
      buffer.append('>');
      appendText(buffer, text);
      buffer.append("</a>");
    }

    private void appendText(StringBuilder buffer, String text) {

      if ((text == null) || (text.isEmpty())) {
        return;
      }
      buffer.append(XmlUtilImpl.getInstance().escapeXml(text.trim(), false));
    }

    @Override
    public String toString() {

      return "html";
    }
  }

  /** {@link CodeDocFormat} for {@link CodeDocFormat#ASCII_DOC}. */
  protected static class CodeDocFormatAsciiDoc extends CodeDocFormat {

    @Override
    public boolean isReplaceXmlTags() {

      return true;
    }

    @Override
    public String replaceXmlTag(Tag tag) {

      String name = tag.getName();
      if (tag.isOpening() && tag.isClosing()) {
        if ("br".equals(name)) {
          return "\n";
        } else if ("p".equals(name)) {
          return "\n<<<<\n";
        } else if ("hr".equals(name)) {
          return "\n'''\n";
        }
        return "";
      }
      if (CodeDoc.TAG_CODE.equals(name)) {
        return "`";
      } else if ("b".equals(name) || "strong".equals(name)) {
        return "*";
      } else if ("i".equals(name) || "em".equals(name)) {
        return "_";
      } else if (tag.isOpening() && !tag.isClosing()) {
        if ("li".equals(name)) {
          Tag parent = tag.getParent();
          if (parent != null) {
            if ("ol".equals(parent.getName())) {
              return "\n. ";
            }
          }
          return "\n* ";
        } else if ("h1".equals(name)) {
          return "\n= ";
        } else if ("h2".equals(name)) {
          return "\n== ";
        } else if ("h3".equals(name)) {
          return "\n=== ";
        } else if ("h4".equals(name)) {
          return "\n==== ";
        }
      }
      // unknown tag...
      return "";
    }

    @Override
    public String replaceDocTag(String tag, String parameter, String text) {

      StringBuilder buffer = new StringBuilder(text.length() + 16);
      if (CodeDoc.TAG_LINK.equals(tag)) {
        buffer.append('`');
        appendLink(buffer, parameter, text);
        buffer.append('`');
      } else if (CodeDoc.TAG_LINKPLAIN.equals(tag)) {
        appendLink(buffer, parameter, text);
      } else if (CodeDoc.TAG_CODE.equals(tag)) {
        buffer.append('`');
        appendText(buffer, text);
        buffer.append('`');
      } else if (CodeDoc.TAG_LITERAL.equals(tag)) {
        appendText(buffer, text);
      } else if (CodeDoc.TAG_VALUE.equals(tag)) {
        appendText(buffer, text);
      } else {
        // unknown tag...
        appendText(buffer, parameter);
        if (buffer.length() > 0) {
          buffer.append(' ');
        }
        appendText(buffer, text);
      }
      return buffer.toString();
    }

    private void appendLink(StringBuilder buffer, String url, String text) {

      if (!url.contains("://")) {
        buffer.append("link:");
      }
      buffer.append(url);
      buffer.append('[');
      appendText(buffer, text);
      buffer.append(']');
    }

    private void appendText(StringBuilder buffer, String text) {

      if ((text == null) || (text.isEmpty())) {
        return;
      }
      buffer.append(text.trim());
    }

    @Override
    public String toString() {

      return "ascii-doc";
    }

  }

  /** {@link CodeDocFormat} for {@link CodeDocFormat#PLAIN_TEXT}. */
  protected static class CodeDocFormatPlainText extends CodeDocFormat {

    @Override
    public boolean isReplaceXmlTags() {

      return true;
    }

    @Override
    public String replaceXmlTag(Tag tag) {

      return "";
    }

    @Override
    public String replaceDocTag(String tag, String parameter, String text) {

      return text;
    }

    @Override
    public String toString() {

      return "plain-text";
    }
  }

  /** {@link CodeDocFormat} for {@link CodeDocFormat#RAW}. */
  protected static class CodeDocFormatRaw extends CodeDocFormat {

    @Override
    public boolean isReplaceXmlTags() {

      return false;
    }

    @Override
    public String replaceXmlTag(Tag tag) {

      throw new IllegalStateException();
    }

    @Override
    public String replaceDocTag(String tag, String parameter, String text) {

      throw new IllegalStateException();
    }

    @Override
    public String toString() {

      return "raw";
    }
  }
}
