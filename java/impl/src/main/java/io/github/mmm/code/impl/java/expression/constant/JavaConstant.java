/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java.expression.constant;

import java.io.IOException;

import io.github.mmm.code.api.expression.CodeConstant;
import io.github.mmm.code.api.language.CodeLanguage;
import io.github.mmm.code.base.expression.BaseExpression;
import io.github.mmm.code.impl.java.expression.JavaExpression;
import io.github.mmm.code.impl.java.expression.literal.JavaLiteral;
import io.github.mmm.code.impl.java.expression.literal.JavaLiteralClass;
import io.github.mmm.code.impl.java.expression.literal.JavaLiteralEnum;
import io.github.mmm.code.impl.java.expression.literal.JavaLiteralNull;
import io.github.mmm.code.impl.java.expression.literal.JavaLiteralString;

/**
 * Implementation of {@link CodeConstant} for Java.
 *
 * @author hohwille
 * @since 1.0.0
 * @param <T> type of {@link #getValue()}.
 */
public abstract class JavaConstant<T> extends BaseExpression implements CodeConstant, JavaExpression {

  /** @see #getValue() */
  protected final T value;

  /**
   * The constructor.
   *
   * @param value the {@link #getValue() value}.
   */
  public JavaConstant(T value) {

    super();
    this.value = value;
  }

  @Override
  public T getValue() {

    return this.value;
  }

  /**
   * @return the {@link Class} reflecting the type of this constant.
   */
  @SuppressWarnings("unchecked")
  public Class<? extends T> getJavaClass() {

    return (Class<? extends T>) this.value.getClass();
  }

  /**
   * @return {@code true} if the {@link #getJavaClass() type} is primitive.
   */
  public boolean isPrimitive() {

    return false;
  }

  @Override
  public JavaConstant<T> evaluate() {

    return this;
  }

  @Override
  public abstract String getSourceCode();

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent,
      CodeLanguage language) throws IOException {

    sink.append(getSourceCode());
  }

  /**
   * @param newValue the new {@link #getValue() value}. May not be {@code null}.
   * @return the corresponding {@link JavaConstant} of the same type with the given value.
   */
  public abstract JavaConstant<T> withValue(T newValue);

  /**
   * @param value the literal value.
   * @param primitive {@code true} if the value is primitive and a {@link JavaLiteral#isPrimitive() primitve}
   *        {@link JavaLiteral} shall be created, {@code false} otherwise.
   * @return the {@link JavaConstant} for the given {@code value}. May be {@code null} if the type of the given
   *         {@code value} is not supported.
   */
  public static JavaConstant<?> of(Object value, boolean primitive) {

    return of(value, primitive, false);
  }

  /**
   * @param value the literal value.
   * @param primitive {@code true} if the value is primitive and a {@link JavaLiteral#isPrimitive() primitve}
   *        {@link JavaLiteral} shall be created, {@code false} otherwise.
   * @param qualified - {@code true} to use the {@link Class#getName() qualified name}, {@code false} otherwise (for
   *        {@link Class#getSimpleName() simple name}). Only relevant for {@link Class} or {@link Enum} literals.
   * @return the {@link JavaConstant} for the given {@code value}. May be {@code null} if the type of the given
   *         {@code value} is not supported.
   */
  public static JavaConstant<?> of(Object value, boolean primitive, boolean qualified) {

    if (value == null) {
      assert (!primitive);
      return JavaLiteralNull.NULL;
    } else if (primitive) {
      return JavaLiteral.of(value);
    } else if (value instanceof String) {
      return JavaLiteralString.of((String) value);
    } else if (value instanceof Class) {
      return JavaLiteralClass.of((Class<?>) value, qualified);
    } else if (value instanceof Enum) {
      return JavaLiteralEnum.of((Enum<?>) value, qualified);
    } else if (value instanceof Boolean) {
      return JavaConstantBoolean.of((Boolean) value);
    } else if (value instanceof Character) {
      return JavaConstantCharacter.of((Character) value);
    } else if (value instanceof Integer) {
      return JavaConstantInteger.of((Integer) value);
    } else if (value instanceof Long) {
      return JavaConstantLong.of((Long) value);
    } else if (value instanceof Double) {
      return JavaConstantDouble.of((Double) value);
    } else if (value instanceof Float) {
      return JavaConstantFloat.of((Float) value);
    } else if (value instanceof Short) {
      return JavaConstantShort.of((Short) value);
    } else if (value instanceof Byte) {
      return JavaConstantByte.of((Byte) value);
    }
    return null;
  }
}
