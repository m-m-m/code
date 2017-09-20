/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.node.CodeNode;
import net.sf.mmm.code.api.node.CodeNodeItem;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.source.CodeSource;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * Abstract implementation of {@link CodeNodeItem}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class AbstractCodeNodeItem extends AbstractCodeItem implements CodeNodeItem {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractCodeNodeItem.class);

  private boolean immutable;

  private int initialized;

  private Runnable lazyInit;

  /**
   * The constructor.
   */
  public AbstractCodeNodeItem() {

    super();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link AbstractCodeNodeItem} to copy.
   */
  public AbstractCodeNodeItem(AbstractCodeNodeItem template) {

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
   * @param item the {@link AbstractCodeNodeItem} to set the
   * @param lazyInit the {@link Runnable} for lazy initialization. See {@link #setLazyInit(Runnable)}.
   * @see #setLazyInit(Runnable)
   */
  protected static void doSetLazyInit(AbstractCodeNodeItem item, Runnable lazyInit) {

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
    if (LOG.isTraceEnabled()) {
      LOG.trace("Initializing {}", toPathString());
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

    if (this.lazyInit != null) {
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
      throw new ReadOnlyException(getClass().getSimpleName());
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

  /**
   * @param <P> type of the parent.
   * @param <N> type of the {@link List} elements.
   * @param list the {@link List} to {@link CodeNodeItemWithGenericParent#copy(CodeNode) copy}.
   * @param newParent the new {@link CodeNodeItemWithGenericParent#getParent() parent}.
   * @return an mutable deep-copy of the {@link List}.
   */
  protected <P extends CodeNode, N extends CodeNodeItemWithGenericParent<P, N>> List<N> doCopy(List<N> list, P newParent) {

    List<N> copy = new ArrayList<>(list.size());
    for (N node : list) {
      copy.add(node.copy(newParent));
    }
    return copy;
  }

  @Override
  public boolean equals(Object obj) {

    // has to be overridden by every sub-class
    if ((obj == null) || (obj.getClass() != getClass())) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {

    // has to be overridden by every sub-class
    return 1;
  }

  /**
   * @return a {@link String} representation of the entire node tree to this node.
   */
  public String toPathString() {

    StringBuilder buffer = new StringBuilder();
    toPathString(buffer);
    return buffer.toString();
  }

  private void toPathString(StringBuilder buffer) {

    CodeNode parent = getParent();
    if (parent == null) {
      LOG.debug("Node item {} without parent.", this);
    } else if (parent instanceof CodeSource) {
      buffer.append(parent.toString());
    } else if (parent instanceof AbstractCodeNodeItem) {
      toPathString(buffer);
    } else {
      buffer.append(parent.toString());
    }
    buffer.append('/');
    buffer.append(getClass().getSimpleName());
    buffer.append(':');
    try {
      doWrite(buffer, DEFAULT_NEWLINE, null, null);
    } catch (Exception e) {
      LOG.debug("{}.toString() failed!", getClass().getSimpleName(), e);
    }
  }

}
