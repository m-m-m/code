/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.node;

import io.github.mmm.code.api.item.CodeItem;
import io.github.mmm.code.api.item.CodeItemWithName;

/**
 * {@link CodeNodeItemContainerHierarchical} containing {@link CodeItemWithName#getName() named}
 * {@link CodeItem}s of a particular type.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <I> the type of the contained {@link CodeItem}s.
 * @since 1.0.0
 */
public abstract interface CodeNodeItemContainerFlatWithName<I extends CodeItem> extends CodeNodeItemContainerFlat<I>, CodeNodeItemContainerWithName<I> {

  @Override
  default I get(String name) {

    return getDeclared(name);
  }

  @Override
  CodeNodeItemContainerFlatWithName<I> copy();

}
