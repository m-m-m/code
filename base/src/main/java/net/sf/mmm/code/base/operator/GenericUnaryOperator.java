/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.operator;

import net.sf.mmm.code.api.operator.CodeUnaryOperator;

/**
 * Generic implementation of {@link CodeUnaryOperator}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class GenericUnaryOperator extends GenericOperator implements CodeUnaryOperator {

  /** Instance for {@link #NAME_NOT}. */
  public static final GenericUnaryOperator NOT = new GenericUnaryOperator(NAME_NOT);

  /** Instance for {@link #NAME_BIT_NOT}. */
  public static final GenericUnaryOperator BIT_NOT = new GenericUnaryOperator(NAME_BIT_NOT);

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   */
  public GenericUnaryOperator(String name) {

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
   * @param name the {@link #getName() name} of the requested {@link GenericUnaryOperator}.
   * @return the {@link GenericUnaryOperator} or {@code null} if not found.
   */
  public static GenericUnaryOperator of(String name) {

    return of(name, GenericUnaryOperator.class);
  }

  static void init() {

    // ensure class-loading so constant fields are created...
  }

}
