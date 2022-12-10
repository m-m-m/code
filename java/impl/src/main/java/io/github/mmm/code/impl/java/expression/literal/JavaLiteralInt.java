/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java.expression.literal;

import io.github.mmm.code.api.expression.CodeLiteral;

/**
 * Implementation of {@link JavaLiteral} for {@code int} literal.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class JavaLiteralInt extends JavaLiteral<Integer> {

  /** {@link JavaLiteralInt} for {@code 0}. */
  public static final JavaLiteralInt VALUE_0 = new JavaLiteralInt(Integer.valueOf(0));

  /** {@link JavaLiteralInt} for {@code 1}. */
  public static final JavaLiteralInt VALUE_1 = new JavaLiteralInt(Integer.valueOf(1));

  /** {@link JavaLiteralInt} for {@code Integer#MIN_VALUE}. */
  public static final JavaLiteralInt VALUE_MIN = new JavaLiteralInt(Integer.valueOf(Integer.MIN_VALUE));

  /** {@link JavaLiteralInt} for {@code Integer#MAX_VALUE}. */
  public static final JavaLiteralInt VALUE_MAX = new JavaLiteralInt(Integer.valueOf(Integer.MAX_VALUE));

  private JavaLiteralInt(Integer value) {

    super(value);
  }

  @Override
  public JavaLiteralInt withValue(Integer newValue) {

    return new JavaLiteralInt(newValue);
  }

  @Override
  public Class<Integer> getJavaClass() {

    return int.class;
  }

  @Override
  public String getSourceCode() {

    if (this == VALUE_MIN) {
      return "Integer.MIN_VALUE";
    } else if (this == VALUE_MAX) {
      return "Integer.MAX_VALUE";
    }
    return getValue().toString();
  }

  /**
   * @param value the literal value.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteralInt of(int value) {

    return of(Integer.valueOf(value));
  }

  /**
   * @param value the literal value. May not be {@code null}.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteralInt of(Integer value) {

    if (value.intValue() == 0) {
      return VALUE_0;
    } else if (value.intValue() == 1) {
      return VALUE_1;
    }
    return new JavaLiteralInt(value);
  }
}
