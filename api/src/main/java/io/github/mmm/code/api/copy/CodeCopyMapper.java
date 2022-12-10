/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.copy;

import io.github.mmm.code.api.node.CodeNode;

/**
 * Call-back interface to {@link #map(CodeNode, CodeCopyType) map} and resolve {@link CodeNode}s whilst
 * {@link CodeNodeItemCopyable#copy(CodeCopyMapper) copying}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeCopyMapper {

  /**
   * @param <N> type of the given {@link CodeNode}.
   * @param node the {@link CodeNode} to map. May be {@code null} for simplicity. In such case {@code null} has to be
   *        returned. Extend {@link AbstractCodeCopyMapper} to avoid mistakes.
   * @param type the {@link CodeCopyType} defining the relation of the {@link CodeNode} to map.
   * @return the given {@link CodeNode} or a mapped replacement (e.g. a {@link CodeNodeItemCopyable#copy() copy} of it).
   */
  <N extends CodeNode> N map(N node, CodeCopyType type);

  /**
   * @param name the original name of the {@link CodeNode} to map.
   * @param node the original {@link CodeNode} to copy and map.
   * @return the given {@code name} or a resolved name.
   */
  default String mapName(String name, CodeNode node) {

    return name;
  }

}
