/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.operator;

import net.sf.mmm.code.api.operator.CodeNAryNumericOperator;

/**
 * Base implementation of {@link CodeNAryNumericOperator}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class BaseNAryNumericOperator extends BaseNAryOperator implements CodeNAryNumericOperator {

  /** Instance for {@link #NAME_SUB}. */
  public static final BaseNAryNumericOperator SUB = new BaseNAryNumericOperator(NAME_SUB);

  /** Instance for {@link #NAME_MUL}. */
  public static final BaseNAryNumericOperator MUL = new BaseNAryNumericOperator(NAME_MUL);

  /** Instance for {@link #NAME_DIV}. */
  public static final BaseNAryNumericOperator DIV = new BaseNAryNumericOperator(NAME_DIV);

  /** Instance for {@link #NAME_MOD}. */
  public static final BaseNAryNumericOperator MOD = new BaseNAryNumericOperator(NAME_MOD);

  /** Instance for {@link #NAME_SHIFT_RIGHT_SIGNED}. */
  public static final BaseNAryNumericOperator SHIFT_RIGHT_SIGNED = new BaseNAryNumericOperator(NAME_SHIFT_RIGHT_SIGNED);

  /** Instance for {@link #NAME_SHIFT_RIGHT_UNSIGNED}. */
  public static final BaseNAryNumericOperator SHIFT_RIGHT_UNSIGNED = new BaseNAryNumericOperator(NAME_SHIFT_RIGHT_UNSIGNED);

  /** Instance for {@link #NAME_SHIFT_LEFT}. */
  public static final BaseNAryNumericOperator SHIFT_LEFT = new BaseNAryNumericOperator(NAME_SHIFT_LEFT);

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   */
  public BaseNAryNumericOperator(String name) {

    super(name);
  }

  @Override
  public boolean isBoolean() {

    return false;
  }

  /**
   * @param name the {@link #getName() name} of the requested {@link BaseNAryNumericOperator}.
   * @return the {@link BaseNAryNumericOperator} or {@code null} if not found.
   */
  public static BaseNAryNumericOperator of(String name) {

    return of(name, BaseNAryNumericOperator.class);
  }

  static void initialize() {

    // ensure class-loading so constant fields are created...
  }
}
