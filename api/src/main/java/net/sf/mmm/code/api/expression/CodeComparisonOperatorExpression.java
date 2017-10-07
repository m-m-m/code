/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.expression;

import java.util.Arrays;
import java.util.List;

import net.sf.mmm.code.api.operator.CodeComparisonOperator;

/**
 * {@link CodeOperatorExpression} for a {@link CodeComparisonOperator} expression. <br>
 * Syntax: <pre>
 * «{@link #getLeftArg() left-arg}» «{@link #getOperator() operator}» «{@link #getRightArg() right-arg}»
 * </pre> Example: <pre>
 * 5 > 3
 * </pre>
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeComparisonOperatorExpression extends CodeOperatorExpression, CodeCondition {

  /**
   * @return the left hand side argument to compare.
   */
  CodeExpression getLeftArg();

  @Override
  CodeComparisonOperator getOperator();

  /**
   * @return the right hand side argument to compare.
   */
  CodeExpression getRightArg();

  @Override
  default List<? extends CodeExpression> getArguments() {

    return Arrays.asList(getLeftArg(), getRightArg());
  }

}
