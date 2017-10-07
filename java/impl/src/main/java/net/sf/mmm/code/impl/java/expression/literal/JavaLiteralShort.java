/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.expression.literal;

import net.sf.mmm.code.api.expression.CodeLiteral;

/**
 * Implementation of {@link JavaLiteral} for {@code short} literal.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class JavaLiteralShort extends JavaLiteral<Short> {

  /** {@link JavaLiteralShort} for {@code 0}. */
  public static final JavaLiteralShort VALUE_0 = new JavaLiteralShort(Short.valueOf((short) 0));

  /** {@link JavaLiteralShort} for {@code 1}. */
  public static final JavaLiteralShort VALUE_1 = new JavaLiteralShort(Short.valueOf((short) 1));

  /** {@link JavaLiteralShort} for {@code Short#MIN_VALUE}. */
  public static final JavaLiteralShort VALUE_MIN = new JavaLiteralShort(Short.valueOf(Short.MIN_VALUE));

  /** {@link JavaLiteralShort} for {@code Short#MAX_VALUE}. */
  public static final JavaLiteralShort VALUE_MAX = new JavaLiteralShort(Short.valueOf(Short.MAX_VALUE));

  private JavaLiteralShort(Short value) {

    super(value);
  }

  @Override
  public JavaLiteralShort withValue(Short newValue) {

    return new JavaLiteralShort(newValue);
  }

  @Override
  public Class<Short> getJavaClass() {

    return short.class;
  }

  @Override
  public String getSourceCode() {

    if (this == VALUE_MIN) {
      return "Short.MIN_VALUE";
    } else if (this == VALUE_MAX) {
      return "Short.MAX_VALUE";
    }
    return getValue().toString();
  }

  /**
   * @param value the literal value.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteralShort of(short value) {

    return of(Short.valueOf(value));
  }

  /**
   * @param value the literal value. May not be {@code null}.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteralShort of(Short value) {

    if (value.shortValue() == 0) {
      return VALUE_0;
    } else if (value.shortValue() == 1) {
      return VALUE_1;
    }
    return new JavaLiteralShort(value);
  }
}
