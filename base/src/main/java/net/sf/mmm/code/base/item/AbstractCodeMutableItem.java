/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.item.CodeMutableItem;
import net.sf.mmm.code.api.node.CodeNodeItem;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * Abstract implementation of {@link CodeMutableItem}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class AbstractCodeMutableItem extends AbstractCodeItem implements CodeMutableItem {

  private boolean immutable;

  private int initialized;

  private Runnable lazyInit;

  /**
   * The constructor.
   */
  public AbstractCodeMutableItem() {

    super();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link AbstractCodeMutableItem} to copy.
   */
  public AbstractCodeMutableItem(AbstractCodeMutableItem template) {

    super();
    template.initialize();
    // immutable flag is not copied on purpose
  }

  /**
   * Initializes this node item.
   */
  protected final void initialize() {

    if (this.initialized == 0) {
      this.initialized++;
      doInitialize();
      this.initialized++;
    }
  }

  /**
   * @param lazyInit the {@link Runnable} that shall be invoked during {@link #initialize() initialization} by
   *        {@link #doInitialize()}. If you use this method after the {@link #initialize() initialization} and
   *        {@link Runnable#run() execution} of the {@code lazyInit} this node item will automatically be set
   *        to {@link #isImmutable() immutable}.
   */
  protected void setLazyInit(Runnable lazyInit) {

    Objects.requireNonNull(lazyInit, "lazyInit");
    if (this.initialized > 0) {
      throw new IllegalStateException("Already initialized!");
    }
    if (this.lazyInit == null) {
      this.lazyInit = lazyInit;
    }
    if (this.lazyInit != lazyInit) {
      throw new IllegalStateException("LazyInit is already set!");
    }
  }

  /**
   * @param item the {@link AbstractCodeMutableItem} to set the
   * @param lazyInit the {@link Runnable} for lazy initialization. See {@link #setLazyInit(Runnable)}.
   * @see #setLazyInit(Runnable)
   */
  protected static void doSetLazyInit(AbstractCodeMutableItem item, Runnable lazyInit) {

    item.setLazyInit(lazyInit);
  }

  /**
   * @param init {@code true} to call {@link #initialize()}, {@code false} to do nothing.
   */
  protected final void initialize(boolean init) {

    if (init) {
      initialize();
    }
  }

  /**
   * Called from {@link #initialize()} on first invocation. May be overridden but never be called from
   * anywhere else.
   */
  protected void doInitialize() {

    if (this.initialized != 1) {
      throw new IllegalStateException("Already initialized!");
    }
    boolean systemImmutable = isSystemImmutable();
    if (this.lazyInit != null) {
      this.lazyInit.run();
      this.lazyInit = null;
    }
    if (systemImmutable) {
      setImmutable();
    }
  }

  @Override
  public boolean isImmutable() {

    if (isSystemImmutable()) {
      return true;
    }
    return this.immutable;
  }

  /**
   * @return {@code true} if this is a system internal node item that is considered to be
   *         {@link #isImmutable() immutable} but is technically {@link #setImmutable() set to immutable}
   *         during (lazy) {@link #initialize() initialization}. Otherwise {@code false}.
   */
  protected boolean isSystemImmutable() {

    return (this.lazyInit != null);
  }

  /**
   * Calls {@link #setImmutable()} but only if not {@link #isSystemImmutable() system immutable}. Use this
   * method for implementations of {@link #doSetImmutable()} to propagate immutable-flag to children in order
   * to prevent eager initialization.
   */
  public void setImmutableIfNotSystemImmutable() {

    if (!isSystemImmutable()) {
      setImmutable();
    }
  }

  /**
   * Makes this item {@link #isImmutable() immutable}.
   */
  @Override
  public void setImmutable() {

    if (this.immutable) {
      return;
    }
    initialize();
    doSetImmutable();
    this.immutable = true;
  }

  /**
   * Called on the first call of {@link #setImmutable()}. Has to be overridden to update
   * {@link java.util.Collection}s, make child items immutable, etc.
   */
  protected void doSetImmutable() {

    initialize();
  }

  /**
   * Verifies that this item is not {@link #isImmutable() immutable}. Call this method from any edit-method
   * (setter, etc.).
   *
   * @throws ReadOnlyException if this item is immutable.
   */
  protected void verifyMutalbe() {

    initialize();
    if (this.immutable) {
      throw new ReadOnlyException(getClass().getSimpleName() + ":" + toString());
    }
  }

  /**
   * @param <T> the type of the {@link List} elements.
   * @param list the {@link List} to make immutable.
   * @return an immutable copy of the {@link List}.
   */
  @SuppressWarnings("unchecked")
  protected <T extends CodeItem> List<T> makeImmutable(List<T> list) {

    if (list.isEmpty()) {
      return Collections.emptyList();
    }
    T item = list.get(0);
    if (item instanceof CodeNodeItem) {
      for (CodeNodeItem element : (List<? extends CodeNodeItem>) list) {
        element.setImmutable();
      }
    }
    return Collections.unmodifiableList(new ArrayList<>(list));
  }

}
