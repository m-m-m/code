/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.item;

import java.util.List;

import net.sf.mmm.code.api.type.CodeType;

/**
 * {@link CodeItem} containing {@link CodeItem}s of a particular type. It groups these items and all the
 * methods to operate on them.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <I> the type of the contained {@link CodeItem}s.
 * @since 1.0.0
 */
public abstract interface CodeItemContainerWithInheritance<I extends CodeItem> extends CodeItemContainer<I> {

  /**
   * @return the {@link List} of contained {@link CodeItem}s declared by the {@link #getDeclaringType()
   *         declaring type}. May be {@link List#isEmpty() empty} but is never {@code null}.
   */
  List<? extends I> getDeclared();

  /**
   * @return the {@link Iterable} of all {@link CodeItem}s as the union of the {@link #getDeclared() declared}
   *         and its recursively traversed items. May be {@link java.util.Collection#isEmpty() empty} but is
   *         never {@code null}.
   */
  @Override
  Iterable<? extends I> getAll();

  @Override
  CodeItemContainerWithInheritance<I> copy(CodeType newDeclaringType);

}
