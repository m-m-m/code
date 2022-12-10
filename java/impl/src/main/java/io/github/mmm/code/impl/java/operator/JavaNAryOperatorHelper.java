/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java.operator;

import io.github.mmm.code.api.expression.CodeExpression;
import io.github.mmm.code.impl.java.expression.JavaNAryOperatorExpression;
import io.github.mmm.code.impl.java.expression.constant.JavaConstant;

/**
 * Helper to {@link #evaluate(JavaNAryOperatorExpression) evaluate} {@link JavaNAryOperatorExpression}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaNAryOperatorHelper {

  /**
   * @param expression the {@link JavaNAryOperatorExpression} to {@link JavaNAryOperatorExpression#evaluate()
   *        evaluate}.
   * @return the result of the {@link JavaNAryOperatorExpression#evaluate() evaluation}.
   */
  public static JavaConstant<?> evaluate(JavaNAryOperatorExpression expression) {

    JavaNAryOperatorAggregate<?> aggregate = new JavaNAryOperatorAggregateInitial(expression.getOperator());
    boolean primitive = true;
    for (CodeExpression arg : expression.getArguments()) {
      JavaConstant<?> constant = (JavaConstant<?>) arg.evaluate();
      if (constant == null) {
        return null;
      }
      if (primitive) {
        primitive = constant.isPrimitive();
      }
      Object value = constant.getValue();
      aggregate = aggregate.evaluate(value);
      if (aggregate == null) {
        return null;
      }
    }
    Object result = aggregate.getValue();
    if (result == null) {
      return null;
    }
    return JavaConstant.of(result, primitive);
  }

}
