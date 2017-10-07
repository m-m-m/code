/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.expression;

import java.util.Arrays;
import java.util.List;

import net.sf.mmm.code.api.operator.CodeUnaryOperator;

/**
 * {@link CodeOperatorExpression} using a {@link CodeUnaryOperator}. <br>
 * Syntax: <pre>
 * «{@link #getOperator() operator}»«{@link #getArgument() argument}»
 * </pre> Example: <pre>
 * !true
 * </pre>
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeUnaryOperatorExpression extends CodeOperatorExpression {

  /**
   * @return the single argument of this unary expression.
   */
  CodeExpression getArgument();

  @Override
  default List<? extends CodeExpression> getArguments() {

    return Arrays.asList(getArgument());
  }

  @Override
  CodeUnaryOperator getOperator();

}
