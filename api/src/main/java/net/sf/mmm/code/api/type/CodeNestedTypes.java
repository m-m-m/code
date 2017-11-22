/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.type;

import net.sf.mmm.code.api.merge.CodeAdvancedMergeableItem;
import net.sf.mmm.code.api.node.CodeNodeItemContainerHierarchical;
import net.sf.mmm.code.api.node.CodeNodeItemContainerHierarchicalWithName;

/**
 * {@link CodeNodeItemContainerHierarchical} containing the nested {@link CodeType}s.
 *
 * @see Class#getDeclaringClass()
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <T> the type of the contained {@link CodeType}s.
 * @since 1.0.0
 */
public abstract interface CodeNestedTypes<T extends CodeType>
    extends CodeNodeItemContainerHierarchicalWithName<T>, CodeAdvancedMergeableItem<CodeNestedTypes<?>> {

  @Override
  T getParent();

  @Override
  CodeNestedTypes<T> copy();

}
