/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.type;

import net.sf.mmm.code.api.element.CodeElement;
import net.sf.mmm.code.api.item.CodeMutableItemWithType;
import net.sf.mmm.code.api.node.CodeNodeItemWithDeclaringOperation;

/**
 * {@link CodeGenericType} representing a parameterized type. It is a variable as a placeholder for a
 * {@link CodeGenericType generic} {@link CodeType type}.
 *
 * @see java.lang.reflect.ParameterizedType
 * @see Class#getGenericSuperclass()
 * @see CodeType#createParameterizedType(CodeElement)
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeParameterizedType extends CodeGenericType, CodeMutableItemWithType, CodeNodeItemWithDeclaringOperation {

  @Override
  CodeElement getParent();

  /**
   * @return the raw {@link CodeType} that is parameterized here. In other words this method returns this
   *         parameterized type with its {@link #getTypeParameters() type parameters} removed.
   * @see java.lang.reflect.ParameterizedType#getRawType()
   */
  @Override
  CodeType getType();

  @Override
  CodeParameterizedType copy();

}
