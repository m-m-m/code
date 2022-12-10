/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.operator;

/**
 * {@link CodeOperator} that is unary and takes exactly one single argument.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeUnaryOperator extends CodeOperator {

  /** {@link #getName() Name} of bitwise complement operator. */
  String NAME_BIT_NOT = "~";

  /** {@link #getName() Name} of negation operator. */
  String NAME_NOT = "!";

  @Override
  default boolean isUnary() {

    return true;
  }

}
