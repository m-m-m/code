/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.type;

import io.github.mmm.code.api.copy.CodeNodeItemCopyable;
import io.github.mmm.code.api.merge.CodeAdvancedMergeableItem;
import io.github.mmm.code.api.node.CodeNodeItemContainerHierarchical;
import io.github.mmm.code.api.node.CodeNodeItemContainerHierarchicalWithName;

/**
 * {@link CodeNodeItemContainerHierarchical} containing the nested {@link CodeType}s.
 *
 * @see Class#getDeclaringClass()
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeNestedTypes
    extends CodeNodeItemContainerHierarchicalWithName<CodeType>, CodeAdvancedMergeableItem<CodeNestedTypes>, CodeNodeItemCopyable<CodeType, CodeNestedTypes> {

  @Override
  CodeType getParent();

  @Override
  CodeNestedTypes copy();

}
