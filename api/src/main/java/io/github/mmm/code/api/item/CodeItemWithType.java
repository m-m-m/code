/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.item;

import io.github.mmm.code.api.type.CodeGenericType;

/**
 * {@link CodeItem} that has a {@link #getType() generic type}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeItemWithType extends CodeItem {

  /**
   * @return the {@link CodeGenericType generic type} of this item.
   */
  CodeGenericType getType();

}
