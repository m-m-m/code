/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.operator;

import net.sf.mmm.code.api.operator.CodeNAryOperator;

/**
 * Generic implementation of {@link CodeNAryOperator}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class GenericNAryOperator extends GenericOperator implements CodeNAryOperator {

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   */
  public GenericNAryOperator(String name) {

    super(name);
  }

  @Override
  public final boolean isComparison() {

    return false;
  }

  @Override
  public final boolean isNAry() {

    return true;
  }

  @Override
  public final boolean isUnary() {

    return false;
  }

  /**
   * @param name the {@link #getName() name} of the requested {@link CodeNAryOperator}.
   * @return the {@link CodeNAryOperator} or {@code null} if not found.
   */
  public static CodeNAryOperator of(String name) {

    return of(name, CodeNAryOperator.class);
  }

  static void init() {

    GenericNAryArbitraryOperator.initialize();
    GenericNAryBooleanOperator.initialize();
    GenericNAryHybridOperator.initialize();
    GenericNAryNumericOperator.initialize();
  }
}
