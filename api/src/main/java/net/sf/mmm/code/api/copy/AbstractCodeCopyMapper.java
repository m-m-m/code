/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.copy;

import java.util.HashMap;
import java.util.Map;

import net.sf.mmm.code.api.node.CodeNode;
import net.sf.mmm.util.exception.api.DuplicateObjectException;

/**
 * Implementation of {@link CodeCopyMapper} for default mapping (copy only children).
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class AbstractCodeCopyMapper implements CodeCopyMapper {

  private final Map<CodeNode, CodeNode> mapping;

  /**
   * The constructor.
   */
  public AbstractCodeCopyMapper() {

    super();
    this.mapping = new HashMap<>();
  }

  @SuppressWarnings("unchecked")
  @Override
  public <N extends CodeNode> N map(N node, CodeCopyType type) {

    if (node == null) {
      return null;
    }
    CodeNode result = getMapping(node);
    if ((result == null) && !this.mapping.containsKey(node)) {
      result = doMap(node, type);
    }
    return (N) result;
  }

  /**
   * @param <N> type of the given {@link CodeNode}.
   * @param node the {@link CodeNode} to get from the existing mapping.
   * @return the mapped {@link CodeNode} or {@code null} if no mapping is {@link #registerMapping(CodeNode, CodeNode)
   *         registered}.
   */
  @SuppressWarnings("unchecked")
  protected <N extends CodeNode> N getMapping(N node) {

    return (N) this.mapping.get(node);
  }

  /**
   * @see #map(CodeNode, CodeCopyType)
   * @param <N> type of the given {@link CodeNode}.
   * @param node the {@link CodeNode} to map.
   * @param type the {@link CodeCopyType} defining the relation of the {@link CodeNode} to map.
   * @return the given {@link CodeNode} or a mapped replacement (e.g. a {@link CodeNodeItemCopyable#copy() copy} of it).
   */
  protected abstract <N extends CodeNode> N doMap(N node, CodeCopyType type);

  /**
   * @param <N> type of the given {@link CodeNode}s.
   * @param source the original {@link CodeNode} to map.
   * @param target the {@link CodeNode} copy to map to.
   */
  public <N extends CodeNode> void registerMapping(N source, N target) {

    CodeNode duplicate = this.mapping.put(source, target);
    if (duplicate != null) {
      throw new DuplicateObjectException(target, source, duplicate);
    }
  }

}
