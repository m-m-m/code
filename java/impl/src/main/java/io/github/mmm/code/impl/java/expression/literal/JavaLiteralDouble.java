/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java.expression.literal;

import io.github.mmm.code.api.expression.CodeLiteral;

/**
 * Implementation of {@link JavaLiteral} for {@code double} literal.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class JavaLiteralDouble extends JavaLiteral<Double> {

  /** {@link JavaLiteralDouble} for {@code 0}. */
  public static final JavaLiteralDouble VALUE_0 = new JavaLiteralDouble(Double.valueOf(0D));

  /** {@link JavaLiteralDouble} for {@code 1}. */
  public static final JavaLiteralDouble VALUE_1 = new JavaLiteralDouble(Double.valueOf(1D));

  /** {@link JavaLiteralDouble} for {@code Double#MIN_VALUE}. */
  public static final JavaLiteralDouble VALUE_MIN = new JavaLiteralDouble(Double.valueOf(Double.MIN_VALUE));

  /** {@link JavaLiteralDouble} for {@code Double#MAX_VALUE}. */
  public static final JavaLiteralDouble VALUE_MAX = new JavaLiteralDouble(Double.valueOf(Double.MAX_VALUE));

  private JavaLiteralDouble(Double value) {

    super(value);
  }

  @Override
  public JavaLiteralDouble withValue(Double newValue) {

    return new JavaLiteralDouble(newValue);
  }

  @Override
  public Class<Double> getJavaClass() {

    return double.class;
  }

  @Override
  public String getSourceCode() {

    if (this == VALUE_MIN) {
      return "Double.MIN_VALUE";
    } else if (this == VALUE_MAX) {
      return "Double.MAX_VALUE";
    }
    return getValue().toString() + "D";
  }

  /**
   * @param value the literal value.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteralDouble of(double value) {

    return of(Double.valueOf(value));
  }

  /**
   * @param value the literal value. May not be {@code null}.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteralDouble of(Double value) {

    if (value.doubleValue() == 0) {
      return VALUE_0;
    } else if (value.doubleValue() == 1) {
      return VALUE_1;
    }
    return new JavaLiteralDouble(value);
  }
}
