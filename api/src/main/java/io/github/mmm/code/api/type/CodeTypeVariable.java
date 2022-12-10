/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.type;

import java.lang.reflect.TypeVariable;

import io.github.mmm.code.api.copy.CodeNodeItemCopyable;
import io.github.mmm.code.api.node.CodeNodeItemWithDeclaringOperation;

/**
 * {@link CodeGenericType} representing a type variable. It is a variable as a placeholder for a {@link CodeGenericType
 * generic} {@link CodeType type}.
 *
 * @see java.lang.reflect.TypeVariable
 * @see CodeType#getTypeParameters()
 * @see io.github.mmm.code.api.member.CodeOperation#getTypeParameters()
 * @see CodeGenericType#asTypeVariable()
 * @see CodeTypeVariables
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeTypeVariable extends CodeTypePlaceholder, CodeNodeItemWithDeclaringOperation, CodeNodeItemCopyable<CodeTypeVariables, CodeTypeVariable> {

  @Override
  CodeTypeVariables getParent();

  @Override
  default boolean isExtends() {

    return true;
  }

  @Override
  default boolean isWildcard() {

    return false;
  }

  @Override
  default CodeTypeVariable asTypeVariable() {

    return this;
  }

  @Override
  TypeVariable<?> getReflectiveObject();

  @Override
  CodeTypeVariable copy();

}
