/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.expression;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

  private static final Logger LOG = LoggerFactory.getLogger(JavaLiteral.class);

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
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent) throws IOException {

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
    return new JavaLiteral<>(createStringLiteral(value), value);
  }

  private static String createStringLiteral(String value) {

    return '"' + value.replaceAll("\"", "\\\\\"") + '"';
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
  public static JavaLiteral<Short> of(short value) {

    return new JavaLiteral<>(Short.toString(value), Short.valueOf(value));
  }

  /**
   * @param value the literal value.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteral<Short> of(Short value) {

    if (value == null) {
      return NULL;
    }
    return new JavaLiteral<>("Short.valueOf(" + value + ")", value);
  }

  /**
   * @param value the literal value.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteral<Byte> of(byte value) {

    return new JavaLiteral<>(Byte.toString(value), Byte.valueOf(value));
  }

  /**
   * @param value the literal value.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteral<Byte> of(Byte value) {

    if (value == null) {
      return NULL;
    }
    return new JavaLiteral<>("Byte.valueOf(" + value + ")", value);
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

    if (value == null) {
      return NULL;
    }
    return new JavaLiteral<>("Double.valueOf(" + value + "d)", value);
  }

  /**
   * @param value the literal value.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteral<Float> of(float value) {

    return new JavaLiteral<>(Float.toString(value) + "f", Float.valueOf(value));
  }

  /**
   * @param value the literal value.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteral<Float> of(Float value) {

    if (value == null) {
      return NULL;
    }
    return new JavaLiteral<>("Float.valueOf(" + value + "f)", value);
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

  /**
   * @param value the literal value.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteral<BigDecimal> of(BigDecimal value) {

    if (value == null) {
      return NULL;
    }
    return new JavaLiteral<>("new BigDecimal(" + createStringLiteral(value.toString()) + ")", value);
  }

  /**
   * @param value the literal value.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteral<BigInteger> of(BigInteger value) {

    if (value == null) {
      return NULL;
    }
    return new JavaLiteral<>("new BigInteger(" + createStringLiteral(value.toString()) + ")", value);
  }

  /**
   * @param value the literal value.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteral<Instant> of(Instant value) {

    if (value == null) {
      return NULL;
    }
    return new JavaLiteral<>("Instant.ofEpochMilli(" + value.toEpochMilli() + "l)", value);
  }

  /**
   * @param value the literal value.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteral<LocalDate> of(LocalDate value) {

    if (value == null) {
      return NULL;
    }
    return new JavaLiteral<>("LocalDate.of(" + value.getYear() + ", " + value.getMonthValue() + ", " + value.getDayOfMonth() + ")", value);
  }

  /**
   * @param value the literal value.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteral<LocalDateTime> of(LocalDateTime value) {

    if (value == null) {
      return NULL;
    }
    return new JavaLiteral<>("LocalDateTime.of(" + value.getYear() + ", " + value.getMonthValue() + ", " + value.getDayOfMonth() + ", " + value.getHour() + ", "
        + value.getMinute() + ", " + value.getSecond() + ", " + value.getNano() + ")", value);
  }

  /**
   * @param <T> type of the given {@link Class}.
   * @param value the literal value.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static <T> JavaLiteral<Class<T>> of(Class<T> value) {

    if (value == null) {
      return NULL;
    }
    return new JavaLiteral<>(value.getName() + ".class", value);
  }

  /**
   * @param value the literal value.
   * @param primitive - {@code true} if the given {@code value} should be interpreted as primitive value.
   * @return the {@link CodeLiteral} for the given {@code value}. May be {@code null} if the type of the given
   *         {@code value} is not supported.
   */
  public static JavaLiteral<?> of(Object value, boolean primitive) {

    if (value == null) {
      return NULL;
    } else if (value instanceof Boolean) {
      Boolean b = (Boolean) value;
      if (primitive) {
        return of(b.booleanValue());
      } else {
        return of(b);
      }
    } else if (value instanceof Integer) {
      Integer i = (Integer) value;
      if (primitive) {
        return of(i.intValue());
      } else {
        return of(i);
      }
    } else if (value instanceof Long) {
      Long l = (Long) value;
      if (primitive) {
        return of(l.longValue());
      } else {
        return of(l);
      }
    } else if (value instanceof Short) {
      Short s = (Short) value;
      if (primitive) {
        return of(s.shortValue());
      } else {
        return of(s);
      }
    } else if (value instanceof Float) {
      Float f = (Float) value;
      if (primitive) {
        return of(f.floatValue());
      } else {
        return of(f);
      }
    } else if (value instanceof Double) {
      Double d = (Double) value;
      if (primitive) {
        return of(d.doubleValue());
      } else {
        return of(d);
      }
    } else if (value instanceof Character) {
      Character c = (Character) value;
      if (primitive) {
        return of(c.charValue());
      } else {
        return of(c);
      }
    } else if (value instanceof String) {
      return of((String) value);
    } else if (value instanceof Class) {
      return of((Class<?>) value);
    } else if (value instanceof BigInteger) {
      return of((BigInteger) value);
    } else if (value instanceof BigDecimal) {
      return of((BigDecimal) value);
    } else if (value instanceof Instant) {
      return of((Instant) value);
    } else {
      LOG.debug("Undefined value type for literal: {}", value);
      return null;
    }
  }

}
