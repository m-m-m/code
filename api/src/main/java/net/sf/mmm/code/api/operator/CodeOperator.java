/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.operator;

import net.sf.mmm.code.api.item.CodeItem;

/**
 * {@link CodeItem} for an operator symbol.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeOperator extends CodeItem {

  /**
   * @return the name of this operator.
   */
  String getName();

  /**
   * @return {@code true} if this is a {@link CodeUnaryOperator} that takes exactly one argument,
   *         {@code false} otherwise.
   */
  boolean isUnary();

  /**
   * @return {@code true} if this is a {@link CodeComparisonOperator} that takes exactly two arguments to
   *         compare, {@code false} otherwise.
   */
  boolean isComparison();

  /**
   * @return {@code true} if this is a {@link CodeNAryOperator} that takes two or more arguments to perform a
   *         calculation, {@code false} otherwise.
   *
   */
  boolean isNAry();

}
