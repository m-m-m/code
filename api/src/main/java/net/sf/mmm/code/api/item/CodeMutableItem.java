/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.item;

import net.sf.mmm.code.api.CodeContext;
import net.sf.mmm.code.api.node.CodeNode;

/**
 * {@link CodeNode} that is also a {@link CodeItem}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeMutableItem extends CodeItem {

  /**
   * By default a {@link CodeNode} retrieved from an existing source (e.g. via
   * {@link CodeContext#getType(String)}) is immutable. Use an according {@code edit()} method to get a new
   * mutable copy of the object before you do any changes. A newly created {@link CodeItem} will however
   * always be mutable. In case a {@link CodeItem} is immutable, all setter methods will throw a
   * {@link net.sf.mmm.util.exception.api.ReadOnlyException} and all {@link java.util.Collection}s returned by
   * getters will be {@link java.util.Collections#unmodifiableCollection(java.util.Collection) unmodifiable}.
   *
   * @return {@code true} if this item itself (not the reflected code) is immutable and can not be edited
   *         (setters may be called without getting exceptions), {@code false} otherwise (if mutable).
   */
  boolean isImmutable();

  /**
   * @return {@code true} if mutable (not {@link #isImmutable() immutable})
   */
  default boolean isMutable() {

    return !isImmutable();
  }

  /**
   * Makes this item {@link #isImmutable() immutable}. Can not be undone. Multiple calls will have no further
   * effect.
   */
  void setImmutable();

  /**
   * @return a new {@link #isMutable() mutable} copy of this {@link CodeMutableItem}. Will be a deep-copy with
   *         copies of all child {@link CodeMutableItem}s.
   */
  CodeMutableItem copy();

}
