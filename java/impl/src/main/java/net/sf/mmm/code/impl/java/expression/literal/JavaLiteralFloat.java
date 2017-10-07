/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.expression.literal;

import net.sf.mmm.code.api.expression.CodeLiteral;

/**
 * Implementation of {@link JavaLiteral} for {@code float} literal.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class JavaLiteralFloat extends JavaLiteral<Float> {

  /** {@link JavaLiteralFloat} for {@code 0}. */
  public static final JavaLiteralFloat VALUE_0 = new JavaLiteralFloat(Float.valueOf(0F));

  /** {@link JavaLiteralFloat} for {@code 1}. */
  public static final JavaLiteralFloat VALUE_1 = new JavaLiteralFloat(Float.valueOf(1F));

  /** {@link JavaLiteralFloat} for {@code Float#MIN_VALUE}. */
  public static final JavaLiteralFloat VALUE_MIN = new JavaLiteralFloat(Float.valueOf(Float.MIN_VALUE));

  /** {@link JavaLiteralFloat} for {@code Float#MAX_VALUE}. */
  public static final JavaLiteralFloat VALUE_MAX = new JavaLiteralFloat(Float.valueOf(Float.MAX_VALUE));

  private JavaLiteralFloat(Float value) {

    super(value);
  }

  @Override
  public JavaLiteralFloat withValue(Float newValue) {

    return new JavaLiteralFloat(newValue);
  }

  @Override
  public Class<Float> getJavaClass() {

    return float.class;
  }

  @Override
  public String getSourceCode() {

    if (this == VALUE_MIN) {
      return "Float.MIN_VALUE";
    } else if (this == VALUE_MAX) {
      return "Float.MAX_VALUE";
    }
    return getValue().toString() + "F";
  }

  /**
   * @param value the literal value.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteralFloat of(float value) {

    return of(Float.valueOf(value));
  }

  /**
   * @param value the literal value. May not be {@code null}.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteralFloat of(Float value) {

    if (value.floatValue() == 0) {
      return VALUE_0;
    } else if (value.floatValue() == 1) {
      return VALUE_1;
    }
    return new JavaLiteralFloat(value);
  }
}
