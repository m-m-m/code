/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java.operator;

import io.github.mmm.code.api.operator.CodeNAryOperator;

/**
 * Implementation of {@link JavaNAryOperatorAggregateNumeric} for {@code float}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
@SuppressWarnings("javadoc")
class JavaNAryOperatorAggregateFloat extends JavaNAryOperatorAggregateNumeric<Float> {

  private float value;

  JavaNAryOperatorAggregateFloat(CodeNAryOperator operator, float value) {

    super(operator);
    this.value = value;
  }

  @Override
  public Float getValue() {

    return Float.valueOf(this.value);
  }

  @Override
  public JavaNAryOperatorAggregate<?> evaluate(Object arg) {

    if (arg instanceof Double) {
      return new JavaNAryOperatorAggregateDouble(this.operator, this.value).evaluate(arg);
    }
    return super.evaluate(arg);
  }

  @Override
  protected JavaNAryOperatorAggregateNumeric<?> add(Number arg) {

    this.value += arg.floatValue();
    return this;
  }

  @Override
  protected JavaNAryOperatorAggregateNumeric<?> sub(Number arg) {

    this.value -= arg.floatValue();
    return this;
  }

  @Override
  protected JavaNAryOperatorAggregateNumeric<?> mul(Number arg) {

    this.value *= arg.floatValue();
    return this;
  }

  @Override
  protected JavaNAryOperatorAggregateNumeric<?> div(Number arg) {

    this.value /= arg.floatValue();
    return this;
  }

  @Override
  protected JavaNAryOperatorAggregateNumeric<?> mod(Number arg) {

    this.value %= arg.floatValue();
    return this;
  }

}
