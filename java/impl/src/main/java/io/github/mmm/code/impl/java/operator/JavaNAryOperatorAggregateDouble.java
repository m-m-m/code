/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java.operator;

import io.github.mmm.code.api.operator.CodeNAryOperator;

/**
 * Implementation of {@link JavaNAryOperatorAggregateNumeric} for {@code double}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
@SuppressWarnings("javadoc")
class JavaNAryOperatorAggregateDouble extends JavaNAryOperatorAggregateNumeric<Double> {

  private double value;

  JavaNAryOperatorAggregateDouble(CodeNAryOperator operator, double value) {

    super(operator);
    this.value = value;
  }

  @Override
  public Double getValue() {

    return Double.valueOf(this.value);
  }

  @Override
  protected JavaNAryOperatorAggregateNumeric<?> add(Number arg) {

    this.value += arg.doubleValue();
    return this;
  }

  @Override
  protected JavaNAryOperatorAggregateNumeric<?> sub(Number arg) {

    this.value -= arg.doubleValue();
    return this;
  }

  @Override
  protected JavaNAryOperatorAggregateNumeric<?> mul(Number arg) {

    this.value *= arg.doubleValue();
    return this;
  }

  @Override
  protected JavaNAryOperatorAggregateNumeric<?> div(Number arg) {

    this.value /= arg.doubleValue();
    return this;
  }

  @Override
  protected JavaNAryOperatorAggregateNumeric<?> mod(Number arg) {

    this.value %= arg.doubleValue();
    return this;
  }

}
