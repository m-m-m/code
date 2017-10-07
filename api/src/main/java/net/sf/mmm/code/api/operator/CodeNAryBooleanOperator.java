/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.operator;

/**
 * {@link CodeNAryOperator} that takes boolean arguments and has a boolean result. Except for instances of
 * {@link CodeNAryHybridOperator} other types of arguments and result are not supported.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeNAryBooleanOperator extends CodeNAryOperator {

  /** {@link #getName() Name} of OR operator. */
  String NAME_OR = "||";

  /** {@link #getName() Name} of AND operator. */
  String NAME_AND = "&&";

  @Override
  default boolean isBoolean() {

    return true;
  }

}
