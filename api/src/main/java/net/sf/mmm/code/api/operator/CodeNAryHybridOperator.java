/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.operator;

/**
 * {@link CodeNAryOperator} that is both {@link #isNumeric() numeric} and {@link #isBoolean() boolean}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeNAryHybridOperator extends CodeNAryNumericOperator, CodeNAryBooleanOperator {

  /**
   * {@link #getName() Name} of bitwise OR operator (can technically be applied to boolean values in Java but
   * this is discouraged).
   */
  String NAME_BIT_OR = "|";

  /**
   * {@link #getName() Name} of bitwise AND operator (can technically be applied to boolean values in Java but
   * this is discouraged).
   */
  String NAME_BIT_AND = "&";

  /**
   * {@link #getName() Name} of (bitwise) XOR operator (can be applied to boolean values in Java - has to be
   * wrapped as a {@link net.sf.mmm.code.api.expression.CodeCondition} then).
   */
  String NAME_XOR = "^";

}
