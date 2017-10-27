/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.doc;

import java.util.function.Supplier;

import net.sf.mmm.util.xml.base.XmlUtilImpl;

/**
 * Definition of format(s) to be able to convert {@link CodeDoc} to.
 *
 * @see CodeDoc#getFormatted(CodeDocFormat)
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class CodeDocFormat {

  /** {@link CodeDocFormat} for documentation as HTML fragment. */
  public static final CodeDocFormat HTML = new CodeDocFormatHtml();

  /** {@link CodeDocFormat} for documentation as AsciiDoc fragment. */
  public static final CodeDocFormat ASCII_DOC = new CodeDocFormatAsciiDoc();

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
   * @param link the optional {@link Supplier} used to resolve a {@link CodeDocLink} in case of
   *        {@link CodeDoc#TAG_LINK link}, {@link CodeDoc#TAG_LINKPLAIN linkplain}, or
   *        {@link CodeDoc#TAG_VALUE value} {@code tag}. Otherwise {@code null}.
   * @param text the plain text.
   * @return the tag converted to this format.
   */
  public abstract String replaceDocTag(String tag, Supplier<CodeDocLink> link, String text);

  @Override
  public abstract String toString();

  private static int getListLevel(Tag tag) {

    Tag parent = tag.getParent();
    if (parent == null) {
      return 0;
    }
    int level = getListLevel(parent);
    String name = tag.getName();
    if (name.equals("ul")) {
      if (level == 0) {
        level = -1;
      } else if (level > 0) {
        level = -level;
      }
    } else if (name.equals("ol")) {
      if (level == 0) {
        level = 1;
      } else if (level < 0) {
        level = -level;
      }
    } else if (name.equals("li")) {
      if (level > 0) {
        level++;
      } else {
        level--;
      }
    }
    return level;
  }

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
    public String replaceDocTag(String tag, Supplier<CodeDocLink> link, String text) {

      StringBuilder buffer = new StringBuilder(text.length() + 16);
      if (CodeDoc.TAG_LINK.equals(tag)) {
        buffer.append(CODE_START);
        appendLink(buffer, link.get(), text);
        buffer.append(CODE_END);
      } else if (CodeDoc.TAG_LINKPLAIN.equals(tag)) {
        appendLink(buffer, link.get(), text);
      } else if (CodeDoc.TAG_CODE.equals(tag)) {
        buffer.append(CODE_START);
        appendText(buffer, text);
        buffer.append(CODE_END);
      } else if (CodeDoc.TAG_LITERAL.equals(tag)) {
        appendText(buffer, text);
      } else if (CodeDoc.TAG_VALUE.equals(tag)) {
        appendValue(buffer, link.get());
      } else {
        // unknown tag...
        appendText(buffer, text);
      }
      return buffer.toString();
    }

    private void appendValue(StringBuilder buffer, CodeDocLink link) {

      buffer.append(CODE_START);
      appendText(buffer, link.getLinkedValueAsString());
      buffer.append(CODE_END);
    }

    private void appendLink(StringBuilder buffer, CodeDocLink link, String text) {

      buffer.append("<a href='");
      buffer.append(link.getLinkUrl());
      buffer.append("'>");
      appendText(buffer, link.getText());
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
      } else if (tag.getName().equals("ol") || tag.getName().equals("ul")) {
        return "\n";
      } else if (tag.isOpening() && !tag.isClosing()) {
        if ("li".equals(name)) {
          if (tag.isOpening()) {
            int level = getListLevel(tag);
            int indent = level;
            String bullet;
            if (indent < 0) {
              indent = -indent;
              bullet = "*";
            } else {
              bullet = ".";
            }
            StringBuilder buffer = new StringBuilder(indent + 2);
            buffer.append('\n');
            for (int i = 0; i < indent; i++) {
              buffer.append(bullet);
            }
            buffer.append(' ');
            return buffer.toString();
          }
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
    public String replaceDocTag(String tag, Supplier<CodeDocLink> link, String text) {

      StringBuilder buffer = new StringBuilder(text.length() + 16);
      if (CodeDoc.TAG_LINK.equals(tag)) {
        buffer.append('`');
        appendLink(buffer, link.get(), text);
        buffer.append('`');
      } else if (CodeDoc.TAG_LINKPLAIN.equals(tag)) {
        appendLink(buffer, link.get(), text);
      } else if (CodeDoc.TAG_CODE.equals(tag)) {
        buffer.append('`');
        appendText(buffer, text);
        buffer.append('`');
      } else if (CodeDoc.TAG_LITERAL.equals(tag)) {
        appendText(buffer, text);
      } else if (CodeDoc.TAG_VALUE.equals(tag)) {
        appendValue(buffer, link.get());
      } else {
        // unknown tag...
        appendText(buffer, text);
      }
      return buffer.toString();
    }

    private void appendValue(StringBuilder buffer, CodeDocLink link) {

      buffer.append('`');
      appendText(buffer, link.getLinkedValueAsString());
      buffer.append('`');
    }

    private void appendLink(StringBuilder buffer, CodeDocLink link, String text) {

      String url = link.getLinkUrl();
      if (!url.contains("://")) {
        buffer.append("link:");
      }
      buffer.append(url);
      buffer.append('[');
      appendText(buffer, link.getText());
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

      if (tag.getName().equals("li")) {
        if (tag.isOpening()) {
          int level = getListLevel(tag);
          int indent = level;
          String bullet;
          if (indent < 0) {
            indent = -indent;
            bullet = "* ";
          } else {
            bullet = ". ";
          }
          StringBuilder buffer = new StringBuilder(2 * (indent - 1) + 3);
          buffer.append('\n');
          for (int i = 1; i < indent; i++) {
            buffer.append("  ");
          }
          buffer.append(bullet);
          return buffer.toString();
        }
      } else if (tag.isClosing()) {
        if (tag.getName().equals("ol") || tag.getName().equals("ul")) {
          return "\n";
        }
      }
      return "";
    }

    @Override
    public String replaceDocTag(String tag, Supplier<CodeDocLink> link, String text) {

      if (link != null) {
        return link.get().getText();
      }
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
    public String replaceDocTag(String tag, Supplier<CodeDocLink> link, String text) {

      throw new IllegalStateException();
    }

    @Override
    public String toString() {

      return "raw";
    }
  }
}
