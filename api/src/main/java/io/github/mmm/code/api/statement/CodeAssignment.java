/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.statement;

import io.github.mmm.code.api.expression.CodeExpression;
import io.github.mmm.code.api.expression.CodeVariable;

/**
 * {@link CodeAtomicStatement} representing an assignment of a {@link #getVariable() variable} to the result
 * of an {@link #getExpression() expression}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeAssignment extends CodeAtomicStatement {

  /**
   * @return the {@link CodeVariable} that is to be assigned.
   */
  CodeVariable getVariable();

  /**
   * @return the {@link CodeExpression} to assign. The result of this expression will be assigned to the
   *         {@link #getVariable() variable}. May be {@code null} for {@link CodeLocalVariable}.
   */
  CodeExpression getExpression();

}
