/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.imports;

import java.util.List;

import net.sf.mmm.code.api.CodeItem;

/**
 * {@link CodeItem} representing an import. An import is always {@link #isImmutable() immutable}. To modify it
 * create a new one and replace it.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeImport extends CodeItem {

  /**
   * @return the source that references where to import from. For Java typically the {@link Class#getName()
   *         qualified name} of the according type. May also
   */
  String getSource();

  /**
   * @return {@code true} if this is a static import, {@code false} otherwise.
   */
  boolean isStatic();

  /**
   * @return the {@link List} of {@link CodeImportItem items} to import from {@link #getSource() source}. Will
   *         be {@link List#isEmpty() empty} for no item and therefore is never {@code null}. For Java this
   *         will always be empty.
   */
  List<CodeImportItem> getItems();

}
