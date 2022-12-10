/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java.expression.literal;

import io.github.mmm.code.api.expression.CodeLiteral;

/**
 * Implementation of {@link JavaLiteral} for {@code char} literal.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class JavaLiteralChar extends JavaLiteral<Character> {

  /** {@link JavaLiteralChar} for {@code 0}. */
  public static final JavaLiteralChar VALUE_0 = new JavaLiteralChar(Character.valueOf('\0'));

  /** {@link JavaLiteralChar} for {@code Character#MIN_VALUE}. */
  public static final JavaLiteralChar VALUE_MIN = new JavaLiteralChar(Character.valueOf(Character.MIN_VALUE));

  /** {@link JavaLiteralChar} for {@code Character#MAX_VALUE}. */
  public static final JavaLiteralChar VALUE_MAX = new JavaLiteralChar(Character.valueOf(Character.MAX_VALUE));

  private JavaLiteralChar(Character value) {

    super(value);
  }

  @Override
  public String getSourceCode() {

    if (this == VALUE_MIN) {
      return "Character.MIN_VALUE";
    } else if (this == VALUE_MAX) {
      return "Character.MAX_VALUE";
    }
    return toCharLiteral(getValue().charValue());
  }

  /**
   * @param value the character value.
   * @return the literal {@link String} for the given {@code value}.
   */
  public static String toCharLiteral(char value) {

    String literal;
    if (value == '\n') {
      literal = "'\n'";
    } else if (value == '\r') {
      literal = "'\r'";
    } else if (value == '\t') {
      literal = "'\t'";
    } else if (value == '\"') {
      literal = "'\\\"'";
    } else if (value == '\0') {
      literal = "'\0'";
    } else if ((value >= 32) && (value < 255) && (value != 127)) {
      literal = "'" + value + '\'';
    } else {
      String hex = Integer.toString(value, 16);
      while (hex.length() < 4) {
        hex = '0' + hex;
      }
      literal = "'\\u" + hex + '\'';
    }
    return literal;
  }

  @Override
  public JavaLiteralChar withValue(Character newValue) {

    return new JavaLiteralChar(newValue);
  }

  @Override
  public Class<Character> getJavaClass() {

    return char.class;
  }

  /**
   * @param value the literal value.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteralChar of(char value) {

    return of(Character.valueOf(value));
  }

  /**
   * @param value the literal value. May not be {@code null}.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteralChar of(Character value) {

    char c = value.charValue();
    if (c == 0) {
      return VALUE_0;
    }
    return new JavaLiteralChar(value);
  }
}
