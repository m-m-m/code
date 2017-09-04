/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.item;

import net.sf.mmm.code.api.type.CodeType;

/**
 * {@link CodeItem} containing {@link CodeItem}s of a particular type. It groups these items and all the
 * methods to operate on them.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <I> the type of the contained {@link CodeItem}s.
 * @since 1.0.0
 */
public abstract interface CodeItemContainer<I extends CodeItem> extends CodeItemWithDeclaringType {

  /**
   * @return all contained items.
   */
  Iterable<? extends I> getAll();

  @Override
  CodeItemContainer<I> copy(CodeType newDeclaringType);

}
