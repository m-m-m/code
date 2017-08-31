/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.mmm.code.api.CodeElement;
import net.sf.mmm.code.api.CodeItem;
import net.sf.mmm.code.api.CodeType;
import net.sf.mmm.util.exception.api.ReadOnlyException;
import net.sf.mmm.util.io.api.IoMode;
import net.sf.mmm.util.io.api.RuntimeIoException;

/**
 * Base implementation of {@link CodeItem}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <C> type of {@link #getContext()}.
 * @since 1.0.0
 */
public abstract class AbstractCodeItem<C extends AbstractCodeContext<?, ?>> implements CodeItem {

  private final C context;

  private boolean immutable;

  /**
   * The constructor.
   *
   * @param context the {@link #getContext() context}.
   */
  public AbstractCodeItem(C context) {

    super();
    this.context = context;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link AbstractCodeItem} to copy.
   */
  public AbstractCodeItem(AbstractCodeItem<C> template) {

    super();
    // immutable flag is not copied on purpose
    this.context = template.context;
  }

  /**
   * @return the {@link AbstractCodeContext}.
   */
  public C getContext() {

    return this.context;
  }

  @Override
  public boolean isImmutable() {

    return this.immutable;
  }

  /**
   * Makes this item {@link #isImmutable() immutable}.
   */
  public final void setImmutable() {

    if (this.immutable) {
      return;
    }
    doSetImmutable();
    this.immutable = true;
  }

  /**
   * Called on the first call of {@link #setImmutable()}. Has to be overridden to update
   * {@link java.util.Collection}s, make child items immutable, etc.
   */
  protected void doSetImmutable() {

    // nothing to do here...
  }

  /**
   * @param <T> the type of the {@link List} elements.
   * @param list the {@link List} to make immutable.
   * @return an immutable copy of the {@link List}.
   */
  protected <T extends CodeItem> List<T> makeImmutable(List<T> list) {

    if (list.isEmpty()) {
      return Collections.emptyList();
    }
    for (T element : list) {
      element.setImmutable();
    }
    return Collections.unmodifiableList(new ArrayList<>(list));
  }

  /**
   * Verifies that this item is not {@link #isImmutable() immutable}. Call this method from any edit-method
   * (setter, etc.).
   *
   * @throws ReadOnlyException if this item is immutable.
   */
  protected void verifyMutalbe() {

    if (this.immutable) {
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
      CharSequence newline;
      if (this.context == null) {
        newline = "\n";
      } else {
        newline = this.context.getNewline();
      }
      sink.append(newline);
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

}
