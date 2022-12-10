/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.expression;

/**
 * {@link CodeExpression} for a constant value.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeConstant extends CodeExpression {

  /**
   * @return the literal value. May be {@code null} for literals that do not exist in Java (in case of a
   *         foreign language).
   */
  Object getValue();

  @Override
  default CodeConstant evaluate() {

    return this;
  }

}
