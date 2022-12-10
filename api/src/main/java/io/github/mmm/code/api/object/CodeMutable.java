/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.object;

/**
 * A code object that can be {@link #isMutable() mutable} or {@link #isImmutable() immutable}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeMutable {

  /**
   * By default a {@link io.github.mmm.code.api.node.CodeNode} retrieved from an existing source (e.g. via
   * {@link io.github.mmm.code.api.CodeContext#getType(String)}) is immutable. Use an according {@code edit()} method to
   * get a new mutable copy of the object before you do any changes. A newly created
   * {@link io.github.mmm.code.api.item.CodeItem} will however always be mutable. In case a {@link CodeMutable} object
   * is immutable, all setter methods will throw a {@link io.github.mmm.base.exception.ReadOnlyException} and all
   * {@link java.util.Collection}s returned by getters will be
   * {@link java.util.Collections#unmodifiableCollection(java.util.Collection) unmodifiable}.
   *
   * @return {@code true} if this item itself (not the reflected code) is immutable and can not be edited (setters may
   *         be called without getting exceptions), {@code false} otherwise (if mutable).
   */
  boolean isImmutable();

  /**
   * @return {@code true} if mutable (not {@link #isImmutable() immutable})
   */
  default boolean isMutable() {

    return !isImmutable();
  }

}
