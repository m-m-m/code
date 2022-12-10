/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java.expression.literal;

import io.github.mmm.code.api.expression.CodeLiteral;

/**
 * Implementation of {@link JavaLiteral} for {@code long} literal.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class JavaLiteralLong extends JavaLiteral<Long> {

  /** {@link JavaLiteralLong} for {@code 0}. */
  public static final JavaLiteralLong VALUE_0 = new JavaLiteralLong(Long.valueOf(0L));

  /** {@link JavaLiteralLong} for {@code 1}. */
  public static final JavaLiteralLong VALUE_1 = new JavaLiteralLong(Long.valueOf(1L));

  /** {@link JavaLiteralLong} for {@code Long#MIN_VALUE}. */
  public static final JavaLiteralLong VALUE_MIN = new JavaLiteralLong(Long.valueOf(Long.MIN_VALUE));

  /** {@link JavaLiteralLong} for {@code Long#MAX_VALUE}. */
  public static final JavaLiteralLong VALUE_MAX = new JavaLiteralLong(Long.valueOf(Long.MAX_VALUE));

  private JavaLiteralLong(Long value) {

    super(value);
  }

  @Override
  public JavaLiteralLong withValue(Long newValue) {

    return new JavaLiteralLong(newValue);
  }

  @Override
  public Class<Long> getJavaClass() {

    return long.class;
  }

  @Override
  public String getSourceCode() {

    if (this == VALUE_MIN) {
      return "Long.MIN_VALUE";
    } else if (this == VALUE_MAX) {
      return "Long.MAX_VALUE";
    }
    return getValue().toString() + "L";
  }

  /**
   * @param value the literal value.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteralLong of(long value) {

    return of(Long.valueOf(value));
  }

  /**
   * @param value the literal value. May not be {@code null}.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteralLong of(Long value) {

    if (value.longValue() == 0) {
      return VALUE_0;
    } else if (value.longValue() == 1) {
      return VALUE_1;
    }
    return new JavaLiteralLong(value);
  }
}
