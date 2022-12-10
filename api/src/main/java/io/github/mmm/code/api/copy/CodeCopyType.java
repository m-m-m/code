/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.copy;

/**
 * {@link Enum} with the available types defining the relation of a {@link io.github.mmm.code.api.node.CodeNodeItem} to
 * {@link CodeCopyMapper#map(io.github.mmm.code.api.node.CodeNode, CodeCopyType) map} to the
 * {@link io.github.mmm.code.api.node.CodeNodeItem} to
 * {@link io.github.mmm.code.api.node.CodeNodeItem#copy(CodeCopyMapper) copy}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public enum CodeCopyType {

  /**
   * {@link CodeCopyType} of a directly contained node (e.g.
   * {@link io.github.mmm.code.api.node.CodeNodeItemContainer#getDeclared()}). Copied by default.
   */
  CHILD,

  /**
   * {@link CodeCopyType} of a {@link io.github.mmm.code.api.node.CodeNodeItem#getParent() parent node}. Not copied by
   * default.
   */
  PARENT,

  /**
   * {@link CodeCopyType} of any relation other than {@link #CHILD} or {@link #PARENT} such as a referenced
   * {@link io.github.mmm.code.api.type.CodeGenericType type} (e.g. from
   * {@link io.github.mmm.code.api.item.CodeItemWithType}). Not copied by default.
   */
  REFERENCE

}
