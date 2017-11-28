/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.operator;

import net.sf.mmm.code.api.operator.CodeUnaryOperator;

/**
 * Base implementation of {@link CodeUnaryOperator}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class BaseUnaryOperator extends BaseOperator implements CodeUnaryOperator {

  /** Instance for {@link #NAME_NOT}. */
  public static final BaseUnaryOperator NOT = new BaseUnaryOperator(NAME_NOT);

  /** Instance for {@link #NAME_BIT_NOT}. */
  public static final BaseUnaryOperator BIT_NOT = new BaseUnaryOperator(NAME_BIT_NOT);

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   */
  public BaseUnaryOperator(String name) {

    super(name);
  }

  @Override
  public final boolean isComparison() {

    return false;
  }

  @Override
  public final boolean isNAry() {

    return false;
  }

  @Override
  public final boolean isUnary() {

    return true;
  }

  /**
   * @param name the {@link #getName() name} of the requested {@link BaseUnaryOperator}.
   * @return the {@link BaseUnaryOperator} or {@code null} if not found.
   */
  public static BaseUnaryOperator of(String name) {

    return of(name, BaseUnaryOperator.class);
  }

  static void init() {

    // ensure class-loading so constant fields are created...
  }

}
