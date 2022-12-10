/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.item;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.base.exception.RuntimeIoException;
import io.github.mmm.code.api.item.CodeItem;
import io.github.mmm.code.api.item.CodeItemWithDeclaringType;
import io.github.mmm.code.api.language.CodeLanguage;
import io.github.mmm.code.api.type.CodeType;

/**
 * Base implementation of {@link CodeItem}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BaseItem implements CodeItem {

  private static final Logger LOG = LoggerFactory.getLogger(BaseItem.class);

  private static final String SPACES_MAX = "          ";

  private static final int SPACES_MAX_LENGTH = SPACES_MAX.length();

  private static final String[] SPACES = new String[] { "", " ", "  ", "   ", "    ", "     ", "      ", "       ",
  "        ", "         ", SPACES_MAX };

  /**
   * The constructor.
   */
  public BaseItem() {

    super();
  }

  @Override
  public final void write(Appendable sink, String newline, String defaultIndent, String currentIndent,
      CodeLanguage language) {

    try {
      doWrite(sink, newline, defaultIndent, currentIndent, language);
    } catch (IOException e) {
      throw new RuntimeIoException(e);
    }
  }

  /**
   * @see #write(Appendable, String, String)
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the code from this
   *        {@link CodeItem}.
   * @param newline the newline {@link String}.
   * @param defaultIndent the {@link String} used for indentation (e.g. a number of spaces to insert per indent level).
   * @param currentIndent the current indent (number of spaces). Initially the empty string ({@code ""}). Before a
   *        recursion the {@code defaultIndent} will be appended.
   * @param language the {@link CodeLanguage} to use.
   * @throws IOException if thrown by {@link Appendable}.
   */
  protected abstract void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent,
      CodeLanguage language) throws IOException;

  @Override
  public String toString() {

    try {
      StringBuilder buffer = new StringBuilder();
      doWrite(buffer, "", null, "", getLanguage());
      return buffer.toString();
    } catch (Exception e) {
      LOG.debug("{}.toString() failed!", getClass().getSimpleName(), e);
      return "<failed: " + e + ">";
    }
  }

  @Override
  public String getSourceCode() {

    StringBuilder buffer = new StringBuilder();
    write(buffer);
    return buffer.toString();
  }

  /**
   * @param element the {@link CodeItem}.
   * @return the owning {@link CodeType}.
   */
  protected static CodeType getOwningType(CodeItem element) {

    if (element instanceof CodeType) {
      return (CodeType) element;
    } else if (element instanceof CodeItemWithDeclaringType) {
      return ((CodeItemWithDeclaringType) element).getDeclaringType();
    } else {
      return null;
    }
  }

  /**
   * @param length the number of spaces requested.
   * @return a {@link String} of the given {@link String#length() length} only containing whitespaces.
   */
  protected static String getSpaces(int length) {

    if (length < SPACES.length) {
      return SPACES[length];
    }
    StringBuilder buffer = new StringBuilder(length);
    int len = length;
    while (len > SPACES_MAX_LENGTH) {
      buffer.append(SPACES_MAX);
      len = len - SPACES_MAX_LENGTH;
    }
    if (len > 0) {
      buffer.append(SPACES[len]);
    }
    return buffer.toString();
  }

}
