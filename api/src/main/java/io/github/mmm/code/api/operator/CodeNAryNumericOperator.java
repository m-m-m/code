/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.operator;

/**
 * {@link CodeNAryOperator} that takes {@link Number numeric} arguments and in such case has a {@link Number
 * numeric} result. Except for instances of {@link CodeNAryArbitraryOperator} other types of arguments and
 * result are not supported.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeNAryNumericOperator extends CodeNAryOperator {

  /** {@link #getName() Name} of subtraction (minus) operator. */
  String NAME_SUB = "-";

  /** {@link #getName() Name} of multiplication operator. */
  String NAME_MUL = "*";

  /** {@link #getName() Name} of division operator. */
  String NAME_DIV = "/";

  /** {@link #getName() Name} of modulo operator. */
  String NAME_MOD = "%";

  /** {@link #getName() Name} of bitwise signed shift right operator. */
  String NAME_SHIFT_RIGHT_SIGNED = ">>";

  /** {@link #getName() Name} of bitwise unsigned shift right operator. */
  String NAME_SHIFT_RIGHT_UNSIGNED = ">>>";

  /** {@link #getName() Name} of bitwise signed shift left operator. */
  String NAME_SHIFT_LEFT = "<<";

  @Override
  default boolean isNumeric() {

    return true;
  }

}
