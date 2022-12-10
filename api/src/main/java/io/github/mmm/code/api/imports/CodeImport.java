/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.imports;

import java.util.List;

import io.github.mmm.code.api.item.CodeItem;

/**
 * {@link CodeItem} representing an import.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeImport extends CodeItem {

  /**
   * @return the reference pointing to the location where to import from. For Java typically the
   *         {@link Class#getName() qualified name} of the according type. May also be a star import but these
   *         are discouraged.
   */
  String getReference();

  /**
   * @return {@code true} if this is a static import, {@code false} otherwise.
   */
  boolean isStatic();

  /**
   * @return the {@link List} of {@link CodeImportItem items} to import from the {@link #getReference()
   *         reference}. Will be {@link List#isEmpty() empty} for no item and therefore is never {@code null}.
   *         For Java this will always be empty.
   */
  List<CodeImportItem> getItems();

}
