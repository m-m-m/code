/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.operator;

/**
 * {@link CodeOperator} that is N-ary. It takes two or more arguments.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeNAryOperator extends CodeOperator {

  @Override
  default boolean isNAry() {

    return true;
  }

  /**
   * @return {@code true} if {@link CodeNAryBooleanOperator}, {@code false} otherwise. If {@code false} the
   *         result can never be a boolean value.
   */
  boolean isBoolean();

  /**
   * @return {@code true} if {@link CodeNAryNumericOperator}, {@code false} otherwise. If {@code false} the
   *         result can never be a numeric value.
   */
  boolean isNumeric();

}
