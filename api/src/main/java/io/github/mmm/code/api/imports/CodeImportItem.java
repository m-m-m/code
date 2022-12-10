/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.imports;

import io.github.mmm.code.api.item.CodeItem;

/**
 * An item of a {@link CodeImport} as used in type-scrypt (ES6).
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeImportItem extends CodeItem {

  /** The star import (asterisk) to import all items. */
  String ITEM_ALL = "*";

  /**
   * @return the item to import from the {@link CodeImport#getReference() reference}. Either
   *         {@link #ITEM_ALL}, or a simple type name.
   */
  String getItem();

  /**
   * @return the optional alias to rename the {@link #getItem() item}. Will be {@code null} for no alias.
   */
  String getAlias();

}
