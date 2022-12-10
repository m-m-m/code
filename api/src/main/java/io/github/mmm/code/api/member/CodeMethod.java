/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.member;

import java.lang.reflect.Method;

import io.github.mmm.code.api.arg.CodeReturn;
import io.github.mmm.code.api.copy.CodeNodeItemCopyable;
import io.github.mmm.code.api.expression.CodeExpression;
import io.github.mmm.code.api.merge.CodeAdvancedMergeableItem;
import io.github.mmm.code.api.type.CodeGenericType;

/**
 * {@link CodeOperation} that represents a method of a {@link io.github.mmm.code.api.type.CodeType}.
 *
 * @see io.github.mmm.code.api.type.CodeType#getMethods()
 * @see CodeMethods#getDeclared()
 * @see java.lang.reflect.Method
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeMethod
    extends CodeOperation, CodeAdvancedMergeableItem<CodeMethod>, CodeNodeItemCopyable<CodeMethods, CodeMethod> {

  /**
   * @return the return type of this method.
   */
  @Override
  default CodeGenericType getType() {

    return getReturns().getType();
  }

  /**
   * @return the {@link CodeReturn} with the information about the returned result of this method. Will never be
   *         <code>null</code>. In case of a programming language that supports multiple return types a single
   *         {@link CodeReturn} will be returned that reflects a tuple of the actual returned types.
   */
  CodeReturn getReturns();

  /**
   * @return the default value of this method or {@code null} for none.
   * @see java.lang.reflect.Method#getDefaultValue()
   */
  CodeExpression getDefaultValue();

  /**
   * @param defaultValue the new value of {@link #getDefaultValue()}.
   */
  void setDefaultValue(CodeExpression defaultValue);

  /**
   * <b>Attention:</b><br>
   * This method is expensive as it has to traverse all methods of the entire type hierarchy recursively.
   *
   * @return the {@link CodeMethod} inherited from the closest {@link io.github.mmm.code.api.type.CodeType#getSuperTypes()
   *         super type} that is {@link Override overridden} by this method or {@code null} if this method does not
   *         override any method. Here closest means that {@link io.github.mmm.code.api.type.CodeSuperTypes#getSuperClass()
   *         class hierarchy} is searched first, then the interface hierarchy in left recursive order.
   */
  CodeMethod getParentMethod();

  /**
   * @return the {@link CodeField} that acts as a native property with this method as {@link CodeField#getGetter()
   *         getter} or {@link CodeField#getSetter() setter}. Will be {@code null} for a regular method.
   */
  CodeField getAccessorField();

  @Override
  Method getReflectiveObject();

  @Override
  CodeMethod copy();
}
