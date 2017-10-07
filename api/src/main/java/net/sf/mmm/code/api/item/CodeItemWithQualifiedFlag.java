/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.item;

import net.sf.mmm.code.api.type.CodeType;

/**
 * {@link CodeItem} that has a {@link #isQualified() qualified flag}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeItemWithQualifiedFlag extends CodeItem {

  /**
   * @return {@code true} if the usage of this item in its place is {@link CodeType#getQualifiedName() fully
   *         qualified}, {@code false} otherwise (if the {@link CodeType#getSimpleName() simple name} shall be
   *         used what is the default).
   */
  boolean isQualified();

}
