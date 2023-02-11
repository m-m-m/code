/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java.expression.literal;

import io.github.mmm.code.impl.java.expression.constant.JavaConstant;

/**
 * Implementation of {@link JavaConstant} for {@link Enum} using {@link Enum#valueOf(Class, String)}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class JavaLiteralEnum extends JavaLiteral<Enum<?>> {

  private final boolean qualified;

  private JavaLiteralEnum(Enum<?> value, boolean qualified) {

    super(value);
    this.qualified = qualified;
  }

  @Override
  public JavaLiteralEnum withValue(Enum<?> newValue) {

    return new JavaLiteralEnum(newValue, this.qualified);
  }

  @Override
  public String getSourceCode() {

    String name;
    if (this.qualified) {
      name = this.value.getDeclaringClass().getName();
    } else {
      name = this.value.getDeclaringClass().getSimpleName();
    }
    return name + "." + this.value.name();
  }

  @Override
  public boolean isPrimitive() {

    return false;
  }

  /**
   * @param value the constant value.
   * @return the {@link JavaConstant} for the given {@code value}.
   */
  public static JavaLiteralEnum of(Enum<?> value) {

    return new JavaLiteralEnum(value, false);
  }

  /**
   * @param value the constant value.
   * @param qualified - {@code true} to use the {@link Class#getName() qualified name}, {@code false} otherwise (for
   *        {@link Class#getSimpleName() simple name}).
   * @return the {@link JavaConstant} for the given {@code value}.
   */
  public static JavaLiteralEnum of(Enum<?> value, boolean qualified) {

    return new JavaLiteralEnum(value, qualified);
  }

}
