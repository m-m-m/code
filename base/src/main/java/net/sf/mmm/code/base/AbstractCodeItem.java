/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base;

import java.io.IOException;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.element.CodeElement;
import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.util.io.api.IoMode;
import net.sf.mmm.util.io.api.RuntimeIoException;

/**
 * Base implementation of {@link CodeItem}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class AbstractCodeItem implements CodeItem {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractCodeItem.class);

  private static final String SPACES_MAX = "          ";

  private static final int SPACES_MAX_LENGTH = SPACES_MAX.length();

  private static final String[] SPACES = new String[] { "", " ", "  ", "   ", "    ", "     ", "      ", "       ", "        ", "         ", SPACES_MAX };

  /**
   * The constructor.
   */
  public AbstractCodeItem() {

    super();
  }

  @Override
  public void write(Appendable sink, String newline, String defaultIndent, String currentIndent) {

    try {
      doWrite(sink, newline, defaultIndent, currentIndent);
    } catch (IOException e) {
      throw new RuntimeIoException(e, IoMode.WRITE);
    }
  }

  /**
   * @see #write(Appendable, String, String)
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the code from
   *        this {@link CodeItem}.
   * @param newline the newline {@link String}.
   * @param defaultIndent the {@link String} used for indentation (e.g. a number of spaces to insert per
   *        indent level).
   * @param currentIndent the current indent (number of spaces). Initially the empty string ({@code ""}).
   *        Before a recursion the {@code indent} will be appended.
   * @throws IOException if thrown by {@link Appendable}.
   */
  protected abstract void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent) throws IOException;

  @Override
  public String toString() {

    try {
      StringBuilder buffer = new StringBuilder();
      doWrite(buffer, DEFAULT_NEWLINE, null, null);
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
   * @param name the name to verify.
   * @param pattern the {@link Pattern} the given {@code name} has to {@link String#matches(String) match} or
   *        {@code null} to accept any name.
   */
  protected void verifyName(String name, Pattern pattern) {

    if (pattern == null) {
      return;
    }
    if (pattern.matcher(name).matches()) {
      return;
    }
    throw new IllegalArgumentException("Invalid name '" + name + "' - has to match pattern " + pattern.pattern());
  }

  /**
   * @param element the {@link CodeElement}.
   * @return the owning {@link CodeType}.
   */
  protected static CodeType getOwningType(CodeElement element) {

    if (element instanceof CodeType) {
      return (CodeType) element;
    } else {
      return element.getDeclaringType();
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
