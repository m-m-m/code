/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.node;

import net.sf.mmm.code.api.item.CodeItem;

/**
 * {@link CodeItem} that has a generic {@link #getParent() parent}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <P> type of {@link #getParent()}.
 * @param <SELF> this type itself.
 * @since 1.0.0
 */
public abstract interface CodeNodeItemWithGenericParent<P extends CodeNode, SELF extends CodeNodeItemWithGenericParent<P, SELF>> extends CodeNodeItem {

  @Override
  P getParent();

  @Override
  SELF copy();

  /**
   * @param newParent the new {@link #getParent() parent}.
   * @return a {@link #copy()} with the give {@link #getParent() parent}.
   */
  SELF copy(P newParent);

}
