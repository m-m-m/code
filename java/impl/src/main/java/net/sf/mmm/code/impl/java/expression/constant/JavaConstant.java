/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.expression.constant;

import java.io.IOException;

import net.sf.mmm.code.api.expression.CodeConstant;
import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.base.expression.GenericExpression;
import net.sf.mmm.code.impl.java.expression.JavaExpression;
import net.sf.mmm.code.impl.java.expression.literal.JavaLiteral;
import net.sf.mmm.code.impl.java.expression.literal.JavaLiteralClass;
import net.sf.mmm.code.impl.java.expression.literal.JavaLiteralNull;
import net.sf.mmm.code.impl.java.expression.literal.JavaLiteralString;

/**
 * Implementation of {@link CodeConstant} for Java.
 *
 * @author hohwille
 * @since 1.0.0
 * @param <T> type of {@link #getValue()}.
 */
public abstract class JavaConstant<T> extends GenericExpression implements CodeConstant, JavaExpression {

  private final T value;

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
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

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
   * @return the {@link JavaConstant} for the given {@code value}. May be {@code null} if the type of the
   *         given {@code value} is not supported.
   */
  public static JavaConstant<?> of(Object value, boolean primitive) {

    if (value == null) {
      assert (!primitive);
      return JavaLiteralNull.NULL;
    } else if (primitive) {
      return JavaLiteral.of(value);
    } else if (value instanceof String) {
      return JavaLiteralString.of((String) value);
    } else if (value instanceof Class) {
      return JavaLiteralClass.of((Class<?>) value);
    } else {
      return JavaFactoryConstant.of(value);
    }
  }
}
