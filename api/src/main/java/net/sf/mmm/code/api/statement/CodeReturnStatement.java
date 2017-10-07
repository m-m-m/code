/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.statement;

import net.sf.mmm.code.api.block.CodeBlockBody;
import net.sf.mmm.code.api.expression.CodeExpression;

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
   *         statement of a {@link net.sf.mmm.code.api.expression.CodeLambdaExpression}'s
   *         {@link net.sf.mmm.code.api.expression.CodeLambdaExpression#getBody() body}, {@code false}
   *         otherwise.
   */
  boolean isOmitReturn();

}
