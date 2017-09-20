/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.node;

import net.sf.mmm.code.impl.java.item.JavaItem;

/**
 * Class to extend by internal implementation classes to get internal access to {@link JavaNodeItemContainer}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaNodeItemContainerAccess {

  /**
   * @param <I> type of the {@link JavaItem}.
   * @param container the {@link JavaNodeItemContainer}.
   * @param item - see {@link JavaNodeItemContainer#addInternal(JavaItem)}.
   */
  protected static <I extends JavaItem> void addContainerItem(JavaNodeItemContainer<I> container, I item) {

    container.addInternal(item);
  }

}
