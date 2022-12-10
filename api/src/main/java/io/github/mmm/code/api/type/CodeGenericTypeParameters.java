/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.type;

import io.github.mmm.code.api.member.CodeOperation;
import io.github.mmm.code.api.node.CodeNodeItemContainer;
import io.github.mmm.code.api.node.CodeNodeItemContainerFlat;
import io.github.mmm.code.api.node.CodeNodeItemWithDeclaringOperation;

/**
 * {@link CodeNodeItemContainer} containing the {@link CodeTypeVariable}s of a {@link CodeType} or
 * {@link CodeOperation}.
 *
 * @see Class#getTypeParameters()
 * @see java.lang.reflect.Executable#getTypeParameters()
 * @see CodeGenericType#getTypeParameters()
 * @see CodeOperation#getTypeParameters()
 * @see CodeTypeVariable
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <P> the type of the contained type parameters.
 * @since 1.0.0
 */
public abstract interface CodeGenericTypeParameters<P extends CodeGenericType>
    extends CodeNodeItemContainerFlat<P>, CodeNodeItemWithDeclaringOperation {

  /**
   * @param parameter the type to add as parameter.
   * @throws io.github.mmm.base.exception.ReadOnlyException if {@link #isImmutable() immutable}.
   */
  void add(P parameter);

  @Override
  CodeGenericTypeParameters<P> copy();

}
