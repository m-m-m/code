/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.node;

import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.item.CodeMutableItem;
import net.sf.mmm.code.api.source.CodeSource;
import net.sf.mmm.code.api.syntax.CodeSyntax;

/**
 * {@link CodeNode} that is also a {@link CodeItem}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeNodeItem extends CodeNode, CodeMutableItem {

  @Override
  default CodeSource getSource() {

    return getParent().getSource();
  }

  @Override
  default CodeSyntax getSyntax() {

    return getContext().getSyntax();
  }

  /**
   * @return a new {@link #isMutable() mutable} copy of this {@link CodeNodeItem}. Will be a deep-copy with
   *         copies of all child {@link CodeNodeItem}s.
   */
  @Override
  CodeNodeItem copy();

}
