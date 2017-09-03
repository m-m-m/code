/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.item;

import java.util.List;

/**
 * {@link CodeItem} containing {@link CodeItem}s of a particular type. It groups these items and all the
 * methods to operate on them.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <I> the type of the contained {@link CodeItem}s.
 * @since 1.0.0
 */
public abstract interface CodeItemContainerWithInheritance<I extends CodeItem> extends CodeItemContainer<I>, CodeItemWithDeclaringType {

  /**
   * @return the {@link List} of contained {@link CodeItem}s declared by the {@link #getDeclaringType()
   *         declaring type}. May be {@link List#isEmpty() empty} but is never {@code null}.
   */
  List<? extends I> getDeclared();

  /**
   * @return the {@link Iterable} of {@link CodeItem}s inherited by the {@link #getDeclaringType() declaring
   *         type} from one of its {@link net.sf.mmm.code.api.type.CodeType#getSuperTypes() super types}. May
   *         be {@link List#isEmpty() empty} but is never {@code null}.
   */
  Iterable<? extends I> getInherited();

  /**
   * @return the {@link CodeItem} of all {@link CodeItem}s as the union of {@link #getDeclared() declared} and
   *         {@link #getInherited() inherited} {@link CodeItem}s. May be {@link List#isEmpty() empty} but is
   *         never {@code null}.
   */
  Iterable<? extends I> getAll();

}
