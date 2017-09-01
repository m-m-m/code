/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.expression;

import java.io.IOException;

import net.sf.mmm.code.api.expression.CodeLiteral;
import net.sf.mmm.code.base.AbstractCodeItem;

/**
 * Implementation of {@link CodeLiteral}.
 *
 * @author hohwille
 * @param <T> type of {@link #getValue()}.
 * @since 1.0.0
 */
public class JavaLiteral<T> extends AbstractCodeItem implements CodeLiteral {

  /** {@link JavaLiteral} for {@code null}. */
  @SuppressWarnings("rawtypes")
  public static final JavaLiteral NULL = new JavaLiteral<>("null", null);

  /** {@link JavaLiteral} for {@code true}. */
  public static final JavaLiteral<Boolean> TRUE = new JavaLiteral<>("true", Boolean.TRUE);

  /** {@link JavaLiteral} for {@code false}. */
  public static final JavaLiteral<Boolean> FALSE = new JavaLiteral<>("false", Boolean.FALSE);

  /** {@link JavaLiteral} for {@link Boolean#TRUE}. */
  public static final JavaLiteral<Boolean> BOOLEAN_TRUE = new JavaLiteral<>("Boolean.TRUE", Boolean.TRUE);

  /** {@link JavaLiteral} for {@link Boolean#FALSE}. */
  public static final JavaLiteral<Boolean> BOOLEAN_FALSE = new JavaLiteral<>("Boolean.FALSE", Boolean.FALSE);

  /** {@link JavaLiteral} for {@code 0}. */
  public static final JavaLiteral<Integer> INT_0 = new JavaLiteral<>("0", Integer.valueOf(0));

  /** {@link JavaLiteral} for {@code 1}. */
  public static final JavaLiteral<Integer> INT_1 = new JavaLiteral<>("1", Integer.valueOf(1));

  /** {@link JavaLiteral} for {@code Integer#MIN_VALUE}. */
  public static final JavaLiteral<Integer> INT_MIN = new JavaLiteral<>("Integer.MIN_VALUE", Integer.valueOf(Integer.MIN_VALUE));

  /** {@link JavaLiteral} for {@code Integer#MAX_VALUE}. */
  public static final JavaLiteral<Integer> INT_MAX = new JavaLiteral<>("Integer.MAX_VALUE", Integer.valueOf(Integer.MAX_VALUE));

  /** {@link JavaLiteral} for {@code Integer.valueOf(0)}. */
  public static final JavaLiteral<Integer> INTEGER_0 = new JavaLiteral<>("Integer.valueOf(0)", Integer.valueOf(0));

  /** {@link JavaLiteral} for {@code Integer.valueOf(1)}. */
  public static final JavaLiteral<Integer> INTEGER_1 = new JavaLiteral<>("Integer.valueOf(1)", Integer.valueOf(1));

  private final String literal;

  private final T value;

  /**
   * The constructor.
   *
   * @param literal the {@link #getLiteral() literal}.
   * @param value the {@link #getValue() value}.
   */
  public JavaLiteral(String literal, T value) {

    super();
    this.literal = literal;
    this.value = value;
  }

  @Override
  public String getLiteral() {

    return this.literal;
  }

  @Override
  public T getValue() {

    return this.value;
  }

  @Override
  protected void doWrite(Appendable sink, String defaultIndent, String currentIndent) throws IOException {

    sink.append(getLiteral());
  }

  /**
   * @param value the literal value.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteral<String> of(String value) {

    if (value == null) {
      return NULL;
    }
    String literal = '"' + value.replaceAll("\"", "\\\\\"") + '"';
    return new JavaLiteral<>(literal, value);
  }

  /**
   * @param value the literal value.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteral<Boolean> of(boolean value) {

    if (value) {
      return TRUE;
    } else {
      return FALSE;
    }
  }

  /**
   * @param value the literal value.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteral<Boolean> of(Boolean value) {

    if (value == null) {
      return NULL;
    } else if (value.booleanValue()) {
      return BOOLEAN_TRUE;
    } else {
      return BOOLEAN_FALSE;
    }
  }

  /**
   * @param value the literal value.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteral<Integer> of(int value) {

    if (value == 0) {
      return INT_0;
    } else if (value == 1) {
      return INT_1;
    } else if (value == Integer.MAX_VALUE) {
      return INT_MAX;
    } else if (value == Integer.MIN_VALUE) {
      return INT_MIN;
    }
    return new JavaLiteral<>(Integer.toString(value), Integer.valueOf(value));
  }

  /**
   * @param value the literal value.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteral<Integer> of(Integer value) {

    if (value == null) {
      return NULL;
    }
    int i = value.intValue();
    if (i == 0) {
      return INTEGER_0;
    } else if (i == 1) {
      return INTEGER_1;
    }
    return new JavaLiteral<>("Integer.valueOf(" + value + ")", value);
  }

  /**
   * @param value the literal value.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteral<Character> of(char value) {

    String literal = toCharLiteral(value);
    return new JavaLiteral<>(literal, Character.valueOf(value));
  }

  private static String toCharLiteral(char value) {

    String literal;
    if ((value >= 32) && (value < 255) && (value != 127)) {
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

  /**
   * @param value the literal value.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteral<Character> of(Character value) {

    if (value == null) {
      return NULL;
    }
    return new JavaLiteral<>("Character.valueOf(" + toCharLiteral(value.charValue()) + ")", value);
  }

  /**
   * @param value the literal value.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteral<Double> of(double value) {

    return new JavaLiteral<>(Double.toString(value) + "d", Double.valueOf(value));
  }

  /**
   * @param value the literal value.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteral<Double> of(Double value) {

    return new JavaLiteral<>("Double.valueOf(" + value + "d)", value);
  }

  /**
   * @param value the literal value.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteral<Long> of(long value) {

    return new JavaLiteral<>(Long.toString(value) + "l", Long.valueOf(value));
  }

  /**
   * @param value the literal value.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteral<Long> of(Long value) {

    return new JavaLiteral<>("Long.valueOf(" + value + "l)", value);
  }

}
