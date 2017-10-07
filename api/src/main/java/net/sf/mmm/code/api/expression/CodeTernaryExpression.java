/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.expression;

/**
 * {@link CodeExpression} for a ternary expression. <br>
 * Syntax: <pre>
 * «{@link #getCondition() (condition)}» ? «{@link #getIfArg() if-arg}» : «{@link #getElseArg() else-arg}»
 * </pre> Example: <pre>
 * (x == 1) ? y : (z + 1)
 * </pre>
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeTernaryExpression extends CodeExpression {

  /**
   * @return the {@link CodeCondition} that decides about the result ({@link #getIfArg() if-arg} or
   *         {@link #getElseArg() else-arg}).
   */
  CodeCondition getCondition();

  /**
   * @return the {@link CodeExpression} that is evaluated as result if the {@link #getCondition() condition}
   *         is {@code true}.
   */
  CodeExpression getIfArg();

  /**
   * @return the {@link CodeExpression} that is evaluated as result if the {@link #getCondition() condition}
   *         is {@code false}.
   */
  CodeExpression getElseArg();

  @Override
  default CodeConstant evaluate() {

    CodeConstant evaluate = getCondition().evaluate();
    if (evaluate != null) {
      Object value = evaluate.getValue();
      if (Boolean.TRUE.equals(value)) {
        return getIfArg().evaluate();
      } else if (Boolean.FALSE.equals(value)) {
        return getElseArg().evaluate();
      }
    }
    return null;
  }

}
