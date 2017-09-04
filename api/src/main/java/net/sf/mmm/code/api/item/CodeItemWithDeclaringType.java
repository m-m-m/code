/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.item;

import net.sf.mmm.code.api.CodePackage;
import net.sf.mmm.code.api.type.CodeType;

/**
 * {@link CodeItem} that has a {@link #getDeclaringType() declaring type}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeItemWithDeclaringType extends CodeItem {

  /**
   * @return the {@link CodeType} declaring this {@link CodeItem}. Here declaring means owning and containing.
   *         May only be {@code null} for {@link CodeType} and for {@link CodePackage} (where it will always
   *         be {@code null}). The declaring type is always immutable and can never be changed. To actually
   *         change it, you need to create a {@link #copy(CodeType) copy} of the item with the new declaring
   *         type.
   * @see CodeType#getDeclaringType()
   */
  CodeType getDeclaringType();

  /**
   * @param newDeclaringType the new {@link #getDeclaringType() declaring type}.
   * @return a new {@link #isImmutable() mutable} copy.
   */
  CodeItemWithDeclaringType copy(CodeType newDeclaringType);

}
