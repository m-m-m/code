/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.copy;

import net.sf.mmm.code.api.item.CodeMutableItem;
import net.sf.mmm.code.api.node.CodeNode;

/**
 * Implementation of {@link CodeCopyMapper} for default mapping (copy only children).
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class CodeCopyMapperDefault extends AbstractCodeCopyMapper {

  /**
   * The constructor.
   */
  public CodeCopyMapperDefault() {

    super();
  }

  @SuppressWarnings("unchecked")
  @Override
  protected <N extends CodeNode> N doMap(N node, CodeCopyType type) {

    if (type == CodeCopyType.CHILD) {
      if (node instanceof CodeMutableItem) {
        return (N) ((CodeMutableItem) node).copy(this);
      }
    }
    return node;
  }

}
