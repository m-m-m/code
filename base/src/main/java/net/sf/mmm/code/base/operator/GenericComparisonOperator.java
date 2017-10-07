/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.operator;

import net.sf.mmm.code.api.operator.CodeComparisonOperator;

/**
 * Generic implementation of {@link CodeComparisonOperator}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class GenericComparisonOperator extends GenericOperator implements CodeComparisonOperator {

  /** Instance for {@link #NAME_EQ}. */
  public static final GenericComparisonOperator EQ = new GenericComparisonOperator(NAME_EQ);

  /** Instance for {@link #NAME_NEQ}. */
  public static final GenericComparisonOperator NEQ = new GenericComparisonOperator(NAME_NEQ);

  /** Instance for {@link #NAME_GT}. */
  public static final GenericComparisonOperator GT = new GenericComparisonOperator(NAME_GT);

  /** Instance for {@link #NAME_GE}. */
  public static final GenericComparisonOperator GE = new GenericComparisonOperator(NAME_GE);

  /** Instance for {@link #NAME_LT}. */
  public static final GenericComparisonOperator LT = new GenericComparisonOperator(NAME_LT);

  /** Instance for {@link #NAME_LE}. */
  public static final GenericComparisonOperator LE = new GenericComparisonOperator(NAME_LE);

  /**
   * The constructor. Only use to declare new constants.
   *
   * @param name the {@link #getName() name}.
   */
  public GenericComparisonOperator(String name) {

    super(name);
  }

  @Override
  public final boolean isComparison() {

    return true;
  }

  @Override
  public final boolean isNAry() {

    return false;
  }

  @Override
  public final boolean isUnary() {

    return false;
  }

  /**
   * @param name the {@link #getName() name} of the requested {@link GenericComparisonOperator}.
   * @return the {@link GenericComparisonOperator} or {@code null} if not found.
   */
  public static GenericComparisonOperator of(String name) {

    return of(name, GenericComparisonOperator.class);
  }

  static void init() {

    // ensure class-loading so constant fields are created...
  }

}
