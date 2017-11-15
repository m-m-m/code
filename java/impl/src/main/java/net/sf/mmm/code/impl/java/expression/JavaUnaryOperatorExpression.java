/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.expression;

import net.sf.mmm.code.api.expression.CodeConstant;
import net.sf.mmm.code.api.expression.CodeExpression;
import net.sf.mmm.code.api.operator.CodeUnaryOperator;
import net.sf.mmm.code.base.expression.BaseUnaryOperatorExpression;
import net.sf.mmm.code.impl.java.expression.constant.JavaConstant;
import net.sf.mmm.code.impl.java.expression.literal.JavaLiteralBoolean;

/**
 * Implementation of {@link BaseUnaryOperatorExpression} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaUnaryOperatorExpression extends BaseUnaryOperatorExpression implements JavaExpression {

  /**
   * The constructor.
   *
   * @param operator the {@link #getOperator() operator}.
   * @param argument the {@link #getArgument() argument}.
   */
  public JavaUnaryOperatorExpression(CodeUnaryOperator operator, CodeExpression argument) {

    super(operator, argument);
  }

  @Override
  public JavaConstant<?> evaluate() {

    String op = getOperator().getName();
    if (CodeUnaryOperator.NAME_NOT.equals(op)) {
      CodeConstant constant = getArgument().evaluate();
      if (constant != null) {
        Object value = constant.getValue();
        if (Boolean.TRUE.equals(value)) {
          return JavaLiteralBoolean.FALSE;
        } else if (Boolean.FALSE.equals(value)) {
          return JavaLiteralBoolean.TRUE;
        }
      }
    } else if (CodeUnaryOperator.NAME_BIT_NOT.equals(op)) {
      CodeConstant literal = getArgument().evaluate();
      if (literal instanceof JavaConstant<?>) {
        return complement((JavaConstant<?>) literal);
      }
    }
    return null;
  }

  private <T> JavaConstant<T> complement(JavaConstant<T> literal) {

    T value = literal.getValue();
    value = complement(value);
    if (value != null) {
      return literal.withValue(value);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  private <T> T complement(T value) {

    Object result = null;
    if (value instanceof Integer) {
      result = Integer.valueOf(~((Integer) value).intValue());
    } else if (value instanceof Long) {
      result = Long.valueOf(~((Long) value).longValue());
    } else if (value instanceof Short) {
      result = Short.valueOf((short) ~((Short) value).shortValue());
    } else if (value instanceof Byte) {
      result = Byte.valueOf((byte) ~((Byte) value).byteValue());
    }
    return (T) result;
  }

}
