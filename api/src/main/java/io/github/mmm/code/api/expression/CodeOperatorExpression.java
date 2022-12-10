/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.expression;

import java.util.List;

import io.github.mmm.code.api.operator.CodeOperator;

/**
 * {@link CodeExpression} that applies an {@link #getOperator() operator} to a number of
 * {@link #getArguments() arguments}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeOperatorExpression extends CodeExpression {

  /**
   * @return the {@link java.util.Collections#unmodifiableList(List) unmodifyable} {@link List} of arguments.
   *         The {@link List#size() size} as to be at least 2.
   */
  List<? extends CodeExpression> getArguments();

  /**
   * @return the {@link CodeOperator} applied to the {@link #getArguments() arguments} to calculate the result
   *         of the expression.
   */
  CodeOperator getOperator();

}
