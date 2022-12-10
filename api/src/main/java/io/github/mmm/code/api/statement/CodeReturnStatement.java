/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.statement;

import io.github.mmm.code.api.block.CodeBlockBody;
import io.github.mmm.code.api.expression.CodeExpression;

/**
 * {@link CodeAtomicStatement} returning the result of an {@link #getExpression() expression}. Has to be the
 * last {@link CodeStatement} of a {@link CodeBlockBody}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeReturnStatement extends CodeAtomicStatement {

  /**
   * @return the {@link CodeExpression} to evaluate and return.
   */
  CodeExpression getExpression();

  /**
   * @return {@code true} if the {@code return} keyword should be omitted because this is the only single
   *         statement of a {@link io.github.mmm.code.api.expression.CodeLambdaExpression}'s
   *         {@link io.github.mmm.code.api.expression.CodeLambdaExpression#getBody() body}, {@code false}
   *         otherwise.
   */
  boolean isOmitReturn();

}
