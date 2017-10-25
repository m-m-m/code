/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.node;

import net.sf.mmm.code.api.item.CodeItem;

/**
 * Class to extend by internal implementation classes to get internal access to
 * {@link AbstractCodeNodeItemContainer}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class AbstractCodeNodeItemContainerAccess {

  /**
   * @param <I> type of the {@link CodeItem}.
   * @param container the {@link AbstractCodeNodeItemContainer}.
   * @param item - see {@link AbstractCodeNodeItemContainer#addInternal(CodeItem)}.
   */
  protected static <I extends CodeItem> void addContainerItem(AbstractCodeNodeItemContainer<I> container, I item) {

    container.addInternal(item);
  }

}
