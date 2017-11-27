/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.copy;

import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.node.CodeNode;
import net.sf.mmm.code.api.node.CodeNodeItem;

/**
 * {@link CodeItem} that has a generic {@link #getParent() parent}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <P> type of {@link #getParent()}.
 * @param <SELF> this type itself.
 * @since 1.0.0
 */
public abstract interface CodeNodeItemCopyable<P extends CodeNode, SELF extends CodeNodeItemCopyable<P, SELF>> extends CodeNodeItem {

  @Override
  P getParent();

  @Override
  default SELF copy() {

    return copy(getParent());
  }

  /**
   * @param newParent the new {@link #getParent() parent}.
   * @return a {@link #copy()} with the give {@link #getParent() parent}.
   */
  default SELF copy(P newParent) {

    return copy(newParent, CodeCopyMapperNone.INSTANCE);
  }

  /**
   * @param newParent the new {@link #getParent() parent}.
   * @param mapper the {@link CodeCopyMapper} used to map involved nodes during copy.
   * @return a {@link #copy()} with the give {@link #getParent() parent}.
   */
  SELF copy(P newParent, CodeCopyMapper mapper);

}
