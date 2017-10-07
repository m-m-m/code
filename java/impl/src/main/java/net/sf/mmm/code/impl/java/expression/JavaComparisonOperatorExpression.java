/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.expression;

import net.sf.mmm.code.api.expression.CodeConstant;
import net.sf.mmm.code.api.expression.CodeExpression;
import net.sf.mmm.code.api.operator.CodeComparisonOperator;
import net.sf.mmm.code.base.expression.GenericComparisonOperatorExpression;
import net.sf.mmm.code.impl.java.expression.literal.JavaLiteral;
import net.sf.mmm.util.lang.api.CompareOperator;

/**
 * Implementation of {@link GenericComparisonOperatorExpression} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaComparisonOperatorExpression extends GenericComparisonOperatorExpression implements JavaExpression {

  /**
   * The constructor.
   *
   * @param leftArg the {@link #getLeftArg() left argument}.
   * @param operator the {@link #getOperator() operator}.
   * @param rightArg the {@link #getRightArg() right argument}.
   */
  public JavaComparisonOperatorExpression(CodeExpression leftArg, CodeComparisonOperator operator, CodeExpression rightArg) {

    super(leftArg, operator, rightArg);
  }

  @Override
  public JavaLiteral<?> evaluate() {

    CompareOperator op = CompareOperator.fromValue(getOperator().getName());
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
    boolean result = op.eval(leftVal, rightVal);
    return JavaLiteral.of(result);
  }

}
