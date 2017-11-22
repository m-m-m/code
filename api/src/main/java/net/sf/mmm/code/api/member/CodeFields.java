/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.member;

import net.sf.mmm.code.api.merge.CodeAdvancedMergeableItem;
import net.sf.mmm.code.api.node.CodeNodeItemContainerHierarchicalWithName;

/**
 * {@link CodeMembers} as a container for the {@link CodeField}s.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <F> the type of the contained {@link CodeField}s.
 * @since 1.0.0
 */
public interface CodeFields<F extends CodeField>
    extends CodeMembers<F>, CodeNodeItemContainerHierarchicalWithName<F>, CodeAdvancedMergeableItem<CodeFields<?>> {

  @Override
  CodeFields<F> copy();

}
