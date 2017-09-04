/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.type;

import net.sf.mmm.code.api.item.CodeItemContainer;
import net.sf.mmm.code.api.item.CodeItemContainerWithInheritanceAndName;
import net.sf.mmm.code.api.item.CodeItemWithDeclaringOperation;
import net.sf.mmm.code.api.member.CodeOperation;

/**
 * {@link CodeItemContainer} containing the {@link CodeTypeVariable}s of a {@link CodeType} or
 * {@link CodeOperation}.
 *
 * @see Class#getTypeParameters()
 * @see java.lang.reflect.Executable#getTypeParameters()
 * @see CodeGenericType#getTypeVariables()
 * @see CodeOperation#getTypeVariables()
 * @see CodeTypeVariable
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeTypeVariables extends CodeItemContainerWithInheritanceAndName<CodeTypeVariable>, CodeItemWithDeclaringOperation {

  @Override
  CodeTypeVariables copy(CodeType newDeclaringType);

  @Override
  CodeTypeVariables copy(CodeOperation newDeclaringOperation);

}
