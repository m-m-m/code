/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java.expression.literal;

import io.github.mmm.code.api.expression.CodeLiteral;

/**
 * Implementation of {@link JavaLiteral} for {@link Class} literal.
 *
 * @param <T> the type of the {@link Class} {@code value}.
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class JavaLiteralClass<T> extends JavaLiteral<Class<T>> {

  private final boolean qualified;

  private JavaLiteralClass(Class<T> value, boolean qualified) {

    super(value);
    this.qualified = qualified;
  }

  @Override
  public JavaLiteralClass<T> withValue(Class<T> newValue) {

    return new JavaLiteralClass<>(newValue, this.qualified);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public Class<? extends Class<T>> getJavaClass() {

    return (Class) Class.class;
  }

  @Override
  public String getSourceCode() {

    String name;
    if (this.qualified) {
      name = this.value.getSimpleName();
    } else {
      name = this.value.getName();
    }
    return name + ".class";
  }

  @Override
  public boolean isPrimitive() {

    return false;
  }

  /**
   * @param <T> the type of the {@link Class} {@code value}.
   * @param value the literal value. May not be {@code null}.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static <T> JavaLiteralClass<T> of(Class<T> value) {

    return of(value, false);
  }

  /**
   * @param <T> the type of the {@link Class} {@code value}.
   * @param value the literal value. May not be {@code null}.
   * @param qualified - {@code true} to use the {@link Class#getName() qualified name}, {@code false} otherwise (for
   *        {@link Class#getSimpleName() simple name}).
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static <T> JavaLiteralClass<T> of(Class<T> value, boolean qualified) {

    return new JavaLiteralClass<>(value, qualified);
  }
}
