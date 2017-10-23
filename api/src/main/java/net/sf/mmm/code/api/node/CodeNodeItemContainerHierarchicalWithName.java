/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.node;

import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.item.CodeItemWithName;

/**
 * {@link CodeNodeItemContainerHierarchical} containing {@link CodeItemWithName#getName() named}
 * {@link CodeItem}s of a particular type.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <I> the type of the contained {@link CodeItem}s.
 * @since 1.0.0
 */
public abstract interface CodeNodeItemContainerHierarchicalWithName<I extends CodeItem>
    extends CodeNodeItemContainerHierarchical<I>, CodeNodeItemContainerWithName<I> {

  @Override
  CodeNodeItemContainerHierarchicalWithName<I> copy();

}
