/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.node.CodeNode;
import net.sf.mmm.code.api.node.CodeNodeItem;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * Abstract implementation of {@link CodeNodeItem}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class AbstractCodeNodeItem extends AbstractCodeItem implements CodeNodeItem {

  private boolean immutable;

  private int initialized;

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
  }

  @Override
  public boolean isImmutable() {

    if (this.immutable) {
      return true;
    }
    initialize();
    return this.immutable;
  }

  /**
   * Makes this item {@link #isImmutable() immutable}.
   */
  @Override
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

}
