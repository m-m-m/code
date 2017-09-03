/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.item;

import net.sf.mmm.code.api.CodeContext;

/**
 * Abstract top-level interface for any item of code as defined by this API. It reflects code structure.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeItem {

  /** The default indent (2 spaces). */
  String DEFAULT_INDENT = "  ";

  /**
   * By default a {@link CodeItem} retrieved from an existing source (e.g. via
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
   * Makes this item {@link #isImmutable() immutable}. Can not be undone. Multiple calls will have no further
   * effect.
   */
  void setImmutable();

  /**
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the code from
   *        this {@link CodeItem}.
   */
  default void write(Appendable sink) {

    write(sink, DEFAULT_INDENT);
  }

  /**
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the code from
   *        this {@link CodeItem}.
   * @param defaultIndent the {@link String} used for indentation (e.g. a number of spaces to insert per
   *        indent level).
   */
  default void write(Appendable sink, String defaultIndent) {

    write(sink, defaultIndent, "");
  }

  /**
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the code from
   *        this {@link CodeItem}.
   * @param defaultIndent the {@link String} used for indentation (e.g. a number of spaces to insert per
   *        indent level).
   * @param currentIndent the current indent (number of spaces). Initially the empty string ({@code ""}).
   *        Before a recursion the {@code indent} will be appended.
   */
  void write(Appendable sink, String defaultIndent, String currentIndent);

}
