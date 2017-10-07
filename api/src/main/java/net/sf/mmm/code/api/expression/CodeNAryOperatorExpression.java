/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.expression;

import java.util.List;

import net.sf.mmm.code.api.operator.CodeNAryOperator;

/**
 * {@link CodeOperatorExpression} using a {@link CodeNAryOperator}. <br>
 * Syntax: <pre>
 * «{@link #getArguments() arg1}»«{@link #getOperator() operator}»«{@link #getArguments() arg2}»...«{@link #getOperator() operator}»«{@link #getArguments() arg-N}»
 * </pre> Example: <pre>
 * 4 + 1 + "foo"
 * </pre>
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeNAryOperatorExpression extends CodeOperatorExpression {

  /**
   * @return the {@link java.util.Collections#unmodifiableList(List) unmodifyable} {@link List} of arguments.
   *         The {@link List#size() size} as to be at least 2.
   */
  @Override
  List<? extends CodeExpression> getArguments();

  @Override
  CodeNAryOperator getOperator();

}
