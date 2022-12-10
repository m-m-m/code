/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.node;

import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.item.CodeItem;
import io.github.mmm.code.api.item.CodeMutableItem;

/**
 * {@link CodeNode} that is also a {@link CodeItem}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeNodeItem extends CodeNode, CodeMutableItem {

  @Override
  CodeNodeItem copy();

  @Override
  CodeNodeItem copy(CodeCopyMapper mapper);

}
