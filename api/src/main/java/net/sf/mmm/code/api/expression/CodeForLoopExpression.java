/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.expression;

import net.sf.mmm.code.api.statement.CodeAtomicStatement;

/**
 * {@link CodeForExpression} for a regular {@code for} loop.
 *
 * @author hohwille
 * @since 1.0.0
 */
public abstract interface CodeForLoopExpression extends CodeForExpression {

  /**
   * @return the initialization {@link CodeAtomicStatement statement} such as "{@code int i = 0}".
   */
  CodeAtomicStatement getInit();

  /**
   * @return the {@link CodeCondition} such as "{@code i < size}".
   */
  CodeCondition getCondition();

  /**
   * @return the update {@link CodeAtomicStatement statement} such as "{@code i++}".
   */
  CodeAtomicStatement getUpdate();

}
