/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.expression;

import java.io.IOException;

import io.github.mmm.code.api.item.CodeItemWithDeclaration;
import io.github.mmm.code.api.item.CodeItemWithName;
import io.github.mmm.code.api.item.CodeItemWithType;

/**
 * {@link CodeExpression} for a variable (local variable, {@link io.github.mmm.code.api.arg.CodeParameter parameter},
 * {@link io.github.mmm.code.api.member.CodeField field}).
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeVariable extends CodeExpression, CodeItemWithName, CodeItemWithType, CodeItemWithDeclaration {

  @Override
  default void writeReference(Appendable sink, boolean declaration, Boolean qualified) throws IOException {

    getType().writeReference(sink, declaration, qualified);
  }

}
