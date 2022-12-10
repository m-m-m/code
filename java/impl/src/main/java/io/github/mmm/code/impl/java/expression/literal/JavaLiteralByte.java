/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java.expression.literal;

import io.github.mmm.code.api.expression.CodeLiteral;

/**
 * Implementation of {@link JavaLiteral} for {@code byte} literal.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class JavaLiteralByte extends JavaLiteral<Byte> {

  /** {@link JavaLiteralByte} for {@code 0}. */
  public static final JavaLiteralByte VALUE_0 = new JavaLiteralByte(Byte.valueOf((byte) 0));

  /** {@link JavaLiteralByte} for {@code 1}. */
  public static final JavaLiteralByte VALUE_1 = new JavaLiteralByte(Byte.valueOf((byte) 1));

  /** {@link JavaLiteralByte} for {@code Byte#MIN_VALUE}. */
  public static final JavaLiteralByte VALUE_MIN = new JavaLiteralByte(Byte.valueOf(Byte.MIN_VALUE));

  /** {@link JavaLiteralByte} for {@code Byte#MAX_VALUE}. */
  public static final JavaLiteralByte VALUE_MAX = new JavaLiteralByte(Byte.valueOf(Byte.MAX_VALUE));

  private JavaLiteralByte(Byte value) {

    super(value);
  }

  @Override
  public String getSourceCode() {

    if (this == VALUE_MIN) {
      return "Byte.MIN_VALUE";
    } else if (this == VALUE_MAX) {
      return "Byte.MAX_VALUE";
    }
    return getValue().toString();
  }

  @Override
  public JavaLiteralByte withValue(Byte newValue) {

    return new JavaLiteralByte(newValue);
  }

  @Override
  public Class<Byte> getJavaClass() {

    return byte.class;
  }

  /**
   * @param value the literal value.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteralByte of(byte value) {

    return of(Byte.valueOf(value));
  }

  /**
   * @param value the literal value. May not be {@code null}.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteralByte of(Byte value) {

    if (value.byteValue() == 0) {
      return VALUE_0;
    } else if (value.byteValue() == 1) {
      return VALUE_1;
    }
    return new JavaLiteralByte(value);
  }
}
