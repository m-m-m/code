/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.copy;

import io.github.mmm.code.api.item.CodeItem;
import io.github.mmm.code.api.node.CodeNode;
import io.github.mmm.code.api.node.CodeNodeItem;

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
  SELF copy();

  @Override
  SELF copy(CodeCopyMapper mapper);

}
