/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.expression;

import io.github.mmm.code.api.statement.CodeLocalVariable;

/**
 * {@link CodeForExpression} for a {@code foreach} loop.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeForEachExpression extends CodeForExpression {

  /**
   * @return the loop variable.
   */
  CodeLocalVariable getVariable();

  /**
   * @return the expression resulting in the object to iterate.
   */
  CodeExpression getExpression();

}
