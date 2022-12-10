/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java.expression.literal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.code.api.expression.CodeLiteral;
import io.github.mmm.code.impl.java.expression.constant.JavaConstant;

/**
 * Implementation of {@link CodeLiteral} for Java.
 *
 * @author hohwille
 * @param <T> type of {@link #getValue()}.
 * @since 1.0.0
 */
public abstract class JavaLiteral<T> extends JavaConstant<T> implements CodeLiteral {

  private static final Logger LOG = LoggerFactory.getLogger(JavaLiteral.class);

  /**
   * The constructor.
   *
   * @param value the {@link #getValue() value}.
   */
  protected JavaLiteral(T value) {

    super(value);
  }

  @Override
  public JavaLiteral<T> evaluate() {

    return this;
  }

  @Override
  public boolean isPrimitive() {

    return true;
  }

  @Override
  public abstract JavaLiteral<T> withValue(T newValue);

  /**
   * @param value the literal value.
   * @return the {@link JavaLiteral} for the given {@code value}. May be {@code null} if the type of the given
   *         {@code value} is not supported.
   */
  public static JavaLiteral<?> of(Object value) {

    if (value == null) {
      return JavaLiteralNull.NULL;
    } else if (value instanceof Boolean) {
      return JavaLiteralBoolean.of(((Boolean) value).booleanValue());
    } else if (value instanceof Integer) {
      return JavaLiteralInt.of((Integer) value);
    } else if (value instanceof Long) {
      return JavaLiteralLong.of((Long) value);
    } else if (value instanceof Short) {
      return JavaLiteralShort.of((Short) value);
    } else if (value instanceof Float) {
      return JavaLiteralFloat.of((Float) value);
    } else if (value instanceof Double) {
      return JavaLiteralDouble.of((Double) value);
    } else if (value instanceof Character) {
      return JavaLiteralChar.of((Character) value);
    } else if (value instanceof String) {
      return JavaLiteralString.of((String) value);
    } else if (value instanceof Class) {
      return JavaLiteralClass.of((Class<?>) value);
    } else {
      LOG.debug("Undefined value type for literal: {}", value);
      return null;
    }
  }

}
