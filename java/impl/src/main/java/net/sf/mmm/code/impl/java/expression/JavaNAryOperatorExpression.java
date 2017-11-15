/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.expression;

import java.util.List;

import net.sf.mmm.code.api.expression.CodeExpression;
import net.sf.mmm.code.api.operator.CodeNAryOperator;
import net.sf.mmm.code.base.expression.BaseNAryOperatorExpression;
import net.sf.mmm.code.impl.java.expression.constant.JavaConstant;
import net.sf.mmm.code.impl.java.operator.JavaNAryOperatorHelper;

/**
 * Implementation of {@link BaseNAryOperatorExpression} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaNAryOperatorExpression extends BaseNAryOperatorExpression implements JavaExpression {

  /**
   * The constructor.
   *
   * @param operator the {@link #getOperator() operator}.
   * @param arguments the {@link #getArguments() arguments}.
   */
  public JavaNAryOperatorExpression(CodeNAryOperator operator, CodeExpression... arguments) {

    super(operator, arguments);
  }

  /**
   * The constructor.
   *
   * @param operator the {@link #getOperator() operator}.
   * @param arguments the {@link #getArguments() arguments}.
   */
  public JavaNAryOperatorExpression(CodeNAryOperator operator, List<CodeExpression> arguments) {

    super(operator, arguments);
  }

  @Override
  public JavaConstant<?> evaluate() {

    return JavaNAryOperatorHelper.evaluate(this);
  }

}
