/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.expression.constant;

/**
 * Implementation of {@link JavaConstant} using a standard factory method such as
 * {@link Integer#valueOf(int)}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 * @param <T> type of {@link #getValue() value}.
 */
public abstract class JavaFactoryConstant<T> extends JavaConstant<T> {

  /**
   * The constructor.
   *
   * @param value the {@link #getValue() value}.
   */
  public JavaFactoryConstant(T value) {

    super(value);
  }

  /**
   * @param value the literal value.
   * @return the {@link JavaConstant} for the given {@code value}. May be {@code null} if the type of the
   *         given {@code value} is not supported.
   */
  public static JavaConstant<?> of(Object value) {

    if (value instanceof Boolean) {
      return JavaConstantBoolean.of((Boolean) value);
    } else if (value instanceof Character) {
      return JavaConstantCharacter.of((Character) value);
    } else if (value instanceof Integer) {
      return JavaConstantInteger.of((Integer) value);
    } else if (value instanceof Long) {
      return JavaConstantLong.of((Long) value);
    } else if (value instanceof Double) {
      return JavaConstantDouble.of((Double) value);
    } else if (value instanceof Float) {
      return JavaConstantFloat.of((Float) value);
    } else if (value instanceof Short) {
      return JavaConstantShort.of((Short) value);
    } else if (value instanceof Byte) {
      return JavaConstantByte.of((Byte) value);
    }
    return null;
  }

}
