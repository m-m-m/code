/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base;

import java.io.IOException;

import net.sf.mmm.code.api.CodeItem;
import net.sf.mmm.util.exception.api.ReadOnlyException;
import net.sf.mmm.util.io.api.IoMode;
import net.sf.mmm.util.io.api.RuntimeIoException;

/**
 * Implementation of {@link CodeItem}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class AbstractCodeItem implements CodeItem {

  private boolean immutable;

  /**
   * The constructor.
   */
  public AbstractCodeItem() {

    super();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link AbstractCodeItem} to copy.
   */
  public AbstractCodeItem(AbstractCodeItem template) {

    super();
    // immutable flag is not copied on purpose
  }

  @Override
  public boolean isImmutable() {

    return this.immutable;
  }

  /**
   * Makes this item {@link #isImmutable() immutable}.
   */
  public void setImmutable() {

    this.immutable = false;
  }

  /**
   * Verifies that this item is not {@link #isImmutable() immutable}. Call this method from any edit-method
   * (setter, etc.).
   *
   * @throws ReadOnlyException if this item is immutable.
   */
  protected void verifyMutalbe() {

    if (!this.immutable) {
      throw new ReadOnlyException(getClass().getSimpleName());
    }
  }

  @Override
  public void write(Appendable sink, String defaultIndent, String currentIndent) {

    try {
      doWrite(sink, defaultIndent, currentIndent);
    } catch (IOException e) {
      throw new RuntimeIoException(e, IoMode.WRITE);
    }
  }

  /**
   * @see #write(Appendable, String, String)
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the code from
   *        this {@link CodeItem}.
   * @param defaultIndent the {@link String} used for indentation (e.g. a number of spaces to insert per
   *        indent level).
   * @param currentIndent the current indent (number of spaces). Initially the empty string ({@code ""}).
   *        Before a recursion the {@code indent} will be appended.
   * @throws IOException if thrown by {@link Appendable}.
   */
  protected abstract void doWrite(Appendable sink, String defaultIndent, String currentIndent) throws IOException;

  /**
   * {@link Appendable#append(CharSequence) Writes} a newline to the given {@link Appendable}.
   *
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the code from
   *        this {@link CodeItem}.
   */
  protected final void writeNewline(Appendable sink) {

    try {
      sink.append("\n");
    } catch (IOException e) {
      throw new RuntimeIoException(e, IoMode.WRITE);
    }
  }

  @Override
  public String toString() {

    try {
      StringBuilder buffer = new StringBuilder();
      doWrite(buffer, DEFAULT_INDENT, "");
      return buffer.toString();
    } catch (Exception e) {
      return "<failed: " + e + ">";
    }
  }

}
