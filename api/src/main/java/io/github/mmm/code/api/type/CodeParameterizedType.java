/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.type;

import java.lang.reflect.ParameterizedType;

import io.github.mmm.code.api.copy.CodeNodeItemCopyable;
import io.github.mmm.code.api.element.CodeElement;
import io.github.mmm.code.api.item.CodeMutableItemWithType;
import io.github.mmm.code.api.node.CodeNodeItemWithDeclaringOperation;

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
public interface CodeParameterizedType extends CodeGenericType, CodeMutableItemWithType, CodeNodeItemWithDeclaringOperation,
    CodeNodeItemCopyable<CodeElement, CodeParameterizedType> {

  @Override
  CodeElement getParent();

  /**
   * @return the raw {@link CodeType} that is parameterized here. In other words this method returns this parameterized
   *         type with its {@link #getTypeParameters() type parameters} removed.
   * @see java.lang.reflect.ParameterizedType#getRawType()
   */
  @Override
  CodeType getType();

  @Override
  ParameterizedType getReflectiveObject();

  @Override
  CodeTypeParameters getTypeParameters();

  @Override
  CodeParameterizedType copy();

}
