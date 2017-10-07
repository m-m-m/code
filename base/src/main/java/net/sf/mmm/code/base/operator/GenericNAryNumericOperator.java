/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.operator;

import net.sf.mmm.code.api.operator.CodeNAryNumericOperator;

/**
 * Generic implementation of {@link CodeNAryNumericOperator}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class GenericNAryNumericOperator extends GenericNAryOperator implements CodeNAryNumericOperator {

  /** Instance for {@link #NAME_SUB}. */
  public static final GenericNAryNumericOperator SUB = new GenericNAryNumericOperator(NAME_SUB);

  /** Instance for {@link #NAME_MUL}. */
  public static final GenericNAryNumericOperator MUL = new GenericNAryNumericOperator(NAME_MUL);

  /** Instance for {@link #NAME_DIV}. */
  public static final GenericNAryNumericOperator DIV = new GenericNAryNumericOperator(NAME_DIV);

  /** Instance for {@link #NAME_MOD}. */
  public static final GenericNAryNumericOperator MOD = new GenericNAryNumericOperator(NAME_MOD);

  /** Instance for {@link #NAME_SHIFT_RIGHT_SIGNED}. */
  public static final GenericNAryNumericOperator SHIFT_RIGHT_SIGNED = new GenericNAryNumericOperator(NAME_SHIFT_RIGHT_SIGNED);

  /** Instance for {@link #NAME_SHIFT_RIGHT_UNSIGNED}. */
  public static final GenericNAryNumericOperator SHIFT_RIGHT_UNSIGNED = new GenericNAryNumericOperator(NAME_SHIFT_RIGHT_UNSIGNED);

  /** Instance for {@link #NAME_SHIFT_LEFT}. */
  public static final GenericNAryNumericOperator SHIFT_LEFT = new GenericNAryNumericOperator(NAME_SHIFT_LEFT);

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   */
  public GenericNAryNumericOperator(String name) {

    super(name);
  }

  @Override
  public boolean isBoolean() {

    return false;
  }

  /**
   * @param name the {@link #getName() name} of the requested {@link GenericNAryNumericOperator}.
   * @return the {@link GenericNAryNumericOperator} or {@code null} if not found.
   */
  public static GenericNAryNumericOperator of(String name) {

    return of(name, GenericNAryNumericOperator.class);
  }

  static void initialize() {

    // ensure class-loading so constant fields are created...
  }
}
