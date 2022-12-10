/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.operator;

/**
 * {@link CodeNAryOperator} that can arbitrary types of result ({@link Number numeric} or non-numeric such as
 * {@link String}).
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeNAryArbitraryOperator extends CodeNAryNumericOperator {

  /** {@link #getName() Name} of addition (plus) operator. */
  String NAME_ADD = "+";

}
