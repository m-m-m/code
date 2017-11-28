/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.operator;

import net.sf.mmm.code.api.operator.CodeComparisonOperator;

/**
 * Base implementation of {@link CodeComparisonOperator}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class BaseComparisonOperator extends BaseOperator implements CodeComparisonOperator {

  /** Instance for {@link #NAME_EQ}. */
  public static final BaseComparisonOperator EQ = new BaseComparisonOperator(NAME_EQ);

  /** Instance for {@link #NAME_NEQ}. */
  public static final BaseComparisonOperator NEQ = new BaseComparisonOperator(NAME_NEQ);

  /** Instance for {@link #NAME_GT}. */
  public static final BaseComparisonOperator GT = new BaseComparisonOperator(NAME_GT);

  /** Instance for {@link #NAME_GE}. */
  public static final BaseComparisonOperator GE = new BaseComparisonOperator(NAME_GE);

  /** Instance for {@link #NAME_LT}. */
  public static final BaseComparisonOperator LT = new BaseComparisonOperator(NAME_LT);

  /** Instance for {@link #NAME_LE}. */
  public static final BaseComparisonOperator LE = new BaseComparisonOperator(NAME_LE);

  /**
   * The constructor. Only use to declare new constants.
   *
   * @param name the {@link #getName() name}.
   */
  public BaseComparisonOperator(String name) {

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
   * @param name the {@link #getName() name} of the requested {@link BaseComparisonOperator}.
   * @return the {@link BaseComparisonOperator} or {@code null} if not found.
   */
  public static BaseComparisonOperator of(String name) {

    return of(name, BaseComparisonOperator.class);
  }

  static void init() {

    // ensure class-loading so constant fields are created...
  }

}
