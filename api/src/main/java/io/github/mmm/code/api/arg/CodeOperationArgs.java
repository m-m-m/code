/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.arg;

import io.github.mmm.code.api.item.CodeItemWithDeclaringType;
import io.github.mmm.code.api.member.CodeOperation;
import io.github.mmm.code.api.node.CodeNodeItemContainer;
import io.github.mmm.code.api.node.CodeNodeItemContainerFlat;

/**
 * {@link CodeNodeItemContainer} containing the {@link CodeOperationArg}s of a {@link CodeOperation}.
 *
 * @see java.lang.reflect.Executable#getParameters()
 * @see CodeParameter
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <A> type of the contained {@link CodeOperationArg}s.
 * @since 1.0.0
 */
public abstract interface CodeOperationArgs<A extends CodeOperationArg> extends CodeNodeItemContainerFlat<A>, CodeItemWithDeclaringType {

  @Override
  CodeOperation getParent();

  @Override
  CodeOperationArgs<A> copy();

}
