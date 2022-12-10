/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java.operator;

import io.github.mmm.code.api.operator.CodeNAryOperator;

/**
 * Implementation of {@link JavaNAryOperatorAggregateNumeric} for {@code long}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
@SuppressWarnings("javadoc")
class JavaNAryOperatorAggregateLong extends JavaNAryOperatorAggregateNumeric<Long> {

  private long value;

  JavaNAryOperatorAggregateLong(CodeNAryOperator operator, long value) {

    super(operator);
    this.value = value;
  }

  @Override
  public Long getValue() {

    return Long.valueOf(this.value);
  }

  @Override
  public JavaNAryOperatorAggregate<?> evaluate(Object arg) {

    if (arg instanceof Float) {
      return new JavaNAryOperatorAggregateFloat(this.operator, this.value).evaluate(arg);
    } else if (arg instanceof Double) {
      return new JavaNAryOperatorAggregateDouble(this.operator, this.value).evaluate(arg);
    }
    return super.evaluate(arg);
  }

  @Override
  protected JavaNAryOperatorAggregateNumeric<?> add(Number arg) {

    this.value += arg.longValue();
    return this;
  }

  @Override
  protected JavaNAryOperatorAggregateNumeric<?> sub(Number arg) {

    this.value -= arg.longValue();
    return this;
  }

  @Override
  protected JavaNAryOperatorAggregateNumeric<?> mul(Number arg) {

    this.value *= arg.longValue();
    return this;
  }

  @Override
  protected JavaNAryOperatorAggregateNumeric<?> div(Number arg) {

    this.value /= arg.longValue();
    return this;
  }

  @Override
  protected JavaNAryOperatorAggregateNumeric<?> mod(Number arg) {

    this.value %= arg.longValue();
    return this;
  }

  @Override
  protected JavaNAryOperatorAggregateNumeric<?> xor(Number arg) {

    this.value ^= arg.longValue();
    return this;
  }

  @Override
  protected JavaNAryOperatorAggregateNumeric<?> shiftLeft(Number arg) {

    this.value <<= arg.longValue();
    return this;
  }

  @Override
  protected JavaNAryOperatorAggregateNumeric<?> shiftRightSigned(Number arg) {

    this.value >>= arg.longValue();
    return this;
  }

  @Override
  protected JavaNAryOperatorAggregateNumeric<?> shiftRightUnsigned(Number arg) {

    this.value >>>= arg.longValue();
    return this;
  }

  @Override
  protected JavaNAryOperatorAggregateNumeric<?> bitOr(Number arg) {

    this.value |= arg.longValue();
    return this;
  }

  @Override
  protected JavaNAryOperatorAggregateNumeric<?> bitAnd(Number arg) {

    this.value &= arg.longValue();
    return this;
  }

}
