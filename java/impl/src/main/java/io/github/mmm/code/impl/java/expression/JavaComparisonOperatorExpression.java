/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java.expression;

import io.github.mmm.base.compare.CompareOperator;
import io.github.mmm.code.api.expression.CodeConstant;
import io.github.mmm.code.api.expression.CodeExpression;
import io.github.mmm.code.api.operator.CodeComparisonOperator;
import io.github.mmm.code.base.expression.BaseComparisonOperatorExpression;
import io.github.mmm.code.impl.java.expression.literal.JavaLiteral;
import io.github.mmm.code.impl.java.expression.literal.JavaLiteralBoolean;

/**
 * Implementation of {@link BaseComparisonOperatorExpression} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaComparisonOperatorExpression extends BaseComparisonOperatorExpression implements JavaExpression {

  /**
   * The constructor.
   *
   * @param leftArg the {@link #getLeftArg() left argument}.
   * @param operator the {@link #getOperator() operator}.
   * @param rightArg the {@link #getRightArg() right argument}.
   */
  public JavaComparisonOperatorExpression(CodeExpression leftArg, CodeComparisonOperator operator,
      CodeExpression rightArg) {

    super(leftArg, operator, rightArg);
  }

  @Override
  public JavaLiteral<?> evaluate() {

    CompareOperator op = CompareOperator.ofSymbol(getOperator().getName());
    if (op == null) {
      return null;
    }
    CodeConstant left = getLeftArg().evaluate();
    if (left == null) {
      return null;
    }
    Object leftVal = left.getValue();
    if (leftVal == null) {
      return null;
    }
    CodeConstant right = getRightArg().evaluate();
    if (right == null) {
      return null;
    }
    Object rightVal = right.getValue();
    if (rightVal == null) {
      return null;
    }
    boolean result = op.evalObject(leftVal, rightVal);
    return JavaLiteralBoolean.of(result);
  }

}
