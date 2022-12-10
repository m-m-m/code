/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.operator;

import io.github.mmm.code.api.operator.CodeNAryOperator;

/**
 * Base implementation of {@link CodeNAryOperator}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BaseNAryOperator extends BaseOperator implements CodeNAryOperator {

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   */
  public BaseNAryOperator(String name) {

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

    BaseNAryArbitraryOperator.initialize();
    BaseNAryBooleanOperator.initialize();
    BaseNAryHybridOperator.initialize();
    BaseNAryNumericOperator.initialize();
  }
}
