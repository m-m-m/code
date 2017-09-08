/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.type;

import net.sf.mmm.code.api.node.CodeNodeItemWithDeclaringOperation;

/**
 * {@link CodeGenericType} representing a parameterized type. It is a variable as a placeholder for a
 * {@link CodeGenericType generic} {@link CodeType type}.
 *
 * @see java.lang.reflect.ParameterizedType
 * @see Class#getGenericSuperclass()
 * @see CodeTypeVariables#createParameterizedType(CodeType)
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeParameterizedType extends CodeGenericType, CodeNodeItemWithDeclaringOperation {

  @Override
  CodeTypeVariables getParent();

  @Override
  CodeParameterizedType copy();

}
