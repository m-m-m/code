/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.node;

import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.node.CodeNodeItem;

/**
 * {@link CodeNodeItem} that has a {@link #getParent() parent}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <P> type of {@link #getParent()}.
 * @param <SELF> type of this type itself.
 * @since 1.0.0
 */
public abstract interface BaseNodeItemWithParent<P extends BaseNode, SELF extends BaseNodeItemWithParent<P, SELF>> extends BaseNodeItem {

  /**
   * @return the parent {@link CodeItem}. Shall never be {@code null} and can not be modified. To change the
   *         parent you need to create a new {@link #copy(BaseNode)}
   */
  @Override
  P getParent();

  /**
   * @param newParent the new {@link #getParent() parent}.
   * @return a new {@link #isImmutable() mutable} copy.
   */
  SELF copy(P newParent);

}
