/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.type;

import net.sf.mmm.code.api.member.CodeOperation;
import net.sf.mmm.code.api.node.CodeNodeItemContainer;
import net.sf.mmm.code.api.node.CodeNodeItemContainerFlat;
import net.sf.mmm.code.api.node.CodeNodeItemWithDeclaringOperation;

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
public abstract interface CodeGenericTypeParameters<P extends CodeGenericType> extends CodeNodeItemContainerFlat<P>, CodeNodeItemWithDeclaringOperation {

  @Override
  CodeGenericTypeParameters<P> copy();

}
