/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.expression;

import io.github.mmm.code.api.type.CodeGenericType;

/**
 * {@link CodeExpression} to cast an {@link #getExpression() expression} to a specific {@link #getType()
 * type}. Syntax: <pre>
 * («{@link #getType() type}») «{@link #getExpression() expression}»
 * </pre>
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeCastExpression extends CodeExpression {

  /**
   * @return the {@link CodeGenericType} to cast the {@link #getExpression() expression} to.
   */
  CodeGenericType getType();

  /**
   * @return the {@link CodeExpression} to be casted to {@link #getType() type}.
   */
  CodeExpression getExpression();

}
