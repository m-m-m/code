/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.expression;

/**
 * {@link CodeCondition} wrapping an {@link #getExpression() expression} that evaluates to a boolean result
 * (like a {@link java.util.function.Predicate}).
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeExpressionCondition extends CodeCondition {

  /**
   * @return the actual {@link CodeExpression} wrapped as condition. Should not already be a
   *         {@link CodeCondition} such as
   *         {@link net.sf.mmm.code.api.expression.CodeComparisonOperatorExpression}. Further it shall not be
   *         a {@link CodeExpression} that can never result in a boolean value such as a
   *         {@link CodeMethodInvocation} with a {@link CodeMethodInvocation#getMember() method} returning a
   *         {@link net.sf.mmm.code.api.type.CodeGenericType} that can not be
   *         {@link net.sf.mmm.code.api.type.CodeGenericType#isAssignableTo(net.sf.mmm.code.api.type.CodeGenericType)
   *         assigned to} a boolean type or a {@link CodeNAryOperatorExpression} with an
   *         {@link CodeNAryOperatorExpression#getOperator() operator} that is <b>not</b>
   *         {@link net.sf.mmm.code.api.operator.CodeNAryOperator#isBoolean() boolean}.
   */
  CodeExpression getExpression();

}
