/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.operator;

/**
 * {@link CodeOperator} for comparison expressions. It is always binary and takes two arguments.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeComparisonOperator extends CodeOperator {

  /** {@link #getName() Name} of equal to operator. */
  String NAME_EQ = "==";

  /** {@link #getName() Name} of not equal to operator. */
  String NAME_NEQ = "!=";

  /** {@link #getName() Name} of greater than operator. */
  String NAME_GT = ">";

  /** {@link #getName() Name} of greater than or equal to operator. */
  String NAME_GE = ">=";

  /** {@link #getName() Name} of less than operator. */
  String NAME_LT = "<";

  /** {@link #getName() Name} of less than or equal to operator. */
  String NAME_LE = "<=";

  @Override
  default boolean isComparison() {

    return true;
  }

}
