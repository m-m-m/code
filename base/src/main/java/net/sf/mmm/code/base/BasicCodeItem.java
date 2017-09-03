/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.mmm.code.api.item.CodeItem;
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
public abstract class BasicCodeItem<C extends AbstractCodeContext<?, ?>> extends AbstractCodeItem {

  private final C context;

  private boolean immutable;

  /**
   * The constructor.
   *
   * @param context the {@link #getContext() context}.
   */
  public BasicCodeItem(C context) {

    super();
    this.context = context;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BasicCodeItem} to copy.
   */
  public BasicCodeItem(BasicCodeItem<C> template) {

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
  public void setImmutable() {

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
   * @param <T> the type of the {@link List} elements.
   * @param list the {@link List} to copy.
   * @return an mutable copy of the {@link List}.
   */
  protected <T extends CodeItem> List<T> copy(List<T> list) {

    return new ArrayList<>(list);
  }

  protected <T extends CodeItem> T copy(T item) {

    return null;
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

}
