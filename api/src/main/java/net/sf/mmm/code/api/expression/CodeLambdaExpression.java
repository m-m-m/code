/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.expression;

import java.util.List;

import net.sf.mmm.code.api.block.CodeBlockBody;
import net.sf.mmm.code.api.node.CodeFunction;
import net.sf.mmm.code.api.statement.CodeReturnStatement;

/**
 * {@link CodeExpression} representing a lambda function.<br>
 * Syntax: <pre>
 * («{@link #getVariables() variable-1}, ..., {@link #getVariables() variable-N}») -> {«{@link #getBody() body}»}
 * </pre>
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeLambdaExpression extends CodeExpression, CodeFunction {

  /**
   * @return the {@link List} of {@link CodeVariable}s taken as input for the lambda. May be
   *         {@link List#isEmpty() empty} (for burger arrow function) but never {@code null}.
   */
  List<? extends CodeVariable> getVariables();

  /**
   * If the body contains only a single statement that is not a {@link CodeReturnStatement}
   */
  @Override
  CodeBlockBody getBody();

}
