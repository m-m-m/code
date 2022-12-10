/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java.operator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.code.api.operator.CodeNAryOperator;

class JavaNAryOperatorAggregateInitial extends JavaNAryOperatorAggregate<Object> {

  private static final Logger LOG = LoggerFactory.getLogger(JavaNAryOperatorAggregateInitial.class);

  JavaNAryOperatorAggregateInitial(CodeNAryOperator operator) {

    super(operator);
  }

  @Override
  public Object getValue() {

    return null;
  }

  protected JavaNAryOperatorAggregateNumeric<?> initial(Number arg) {

    if (arg instanceof Integer) {
      return new JavaNAryOperatorAggregateInt(this.operator, arg.intValue());
    } else if (arg instanceof Long) {
      return new JavaNAryOperatorAggregateLong(this.operator, arg.longValue());
    } else if (arg instanceof Double) {
      return new JavaNAryOperatorAggregateDouble(this.operator, arg.doubleValue());
    } else if (arg instanceof Float) {
      return new JavaNAryOperatorAggregateFloat(this.operator, arg.floatValue());
    } else if (arg instanceof Byte) {
      return new JavaNAryOperatorAggregateByte(this.operator, arg.byteValue());
    } else if (arg instanceof Short) {
      return new JavaNAryOperatorAggregateShort(this.operator, arg.shortValue());
    } else {
      LOG.warn("Invalid number type {}", arg.getClass());
      return null;
    }
  }

  protected JavaNAryOperatorAggregateString initial(String arg) {

    return new JavaNAryOperatorAggregateString(this.operator, arg);
  }

  protected JavaNAryOperatorAggregateBoolean initial(Boolean arg) {

    return new JavaNAryOperatorAggregateBoolean(this.operator, arg.booleanValue());
  }

  @Override
  protected JavaNAryOperatorAggregateNumeric<?> add(Number arg) {

    return initial(arg);
  }

  @Override
  protected JavaNAryOperatorAggregateString add(String arg) {

    return initial(arg);
  }

  @Override
  protected JavaNAryOperatorAggregateNumeric<?> sub(Number arg) {

    return initial(arg);
  }

  @Override
  protected JavaNAryOperatorAggregateNumeric<?> mul(Number arg) {

    return initial(arg);
  }

  @Override
  protected JavaNAryOperatorAggregateNumeric<?> div(Number arg) {

    return initial(arg);
  }

  @Override
  protected JavaNAryOperatorAggregateNumeric<?> mod(Number arg) {

    return initial(arg);
  }

  @Override
  protected JavaNAryOperatorAggregateNumeric<?> shiftLeft(Number arg) {

    return initial(arg);
  }

  @Override
  protected JavaNAryOperatorAggregateNumeric<?> shiftRightSigned(Number arg) {

    return initial(arg);
  }

  @Override
  protected JavaNAryOperatorAggregateNumeric<?> shiftRightUnsigned(Number arg) {

    return initial(arg);
  }

  @Override
  protected JavaNAryOperatorAggregateBoolean and(Boolean arg) {

    return initial(arg);
  }

  @Override
  protected JavaNAryOperatorAggregate<? extends Boolean> or(Boolean arg) {

    return initial(arg);
  }

  @Override
  protected JavaNAryOperatorAggregate<? extends Boolean> xor(Boolean arg) {

    return initial(arg);
  }

  @Override
  protected JavaNAryOperatorAggregateNumeric<?> xor(Number arg) {

    return initial(arg);
  }

  @Override
  protected JavaNAryOperatorAggregateNumeric<?> bitOr(Number arg) {

    return initial(arg);
  }

  @Override
  protected JavaNAryOperatorAggregateNumeric<?> bitAnd(Number arg) {

    return initial(arg);
  }

}
