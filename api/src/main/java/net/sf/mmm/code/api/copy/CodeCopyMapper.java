/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.copy;

import net.sf.mmm.code.api.node.CodeNode;

/**
 * Call-back interface to map and resolve {@link CodeNode}s whilst {@link CodeNodeItemCopyable#copy(CodeNode)
 * copying}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeCopyMapper {

  /**
   * @param <N> type of the given {@link CodeNode}.
   * @param node the {@link CodeNode} to map.
   * @return the given {@link CodeNode} or a mapped replacement (e.g. a {@link CodeNodeItemCopyable#copy()
   *         copy} of it).
   */
  <N extends CodeNode> N map(N node);

}
