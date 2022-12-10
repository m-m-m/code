/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java.expression.constant;

/**
 * Implementation of {@link JavaConstant} for an {@link Enum} value.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 * @param <T> type of the {@link Enum} {@link #getValue() value}.
 */
public class JavaEnumConstant<T extends Enum<T>> extends JavaConstant<T> {

  private final boolean unqualified;

  private JavaEnumConstant(T value, boolean qualified) {

    super(value);
    this.unqualified = qualified;
  }

  @Override
  public String getSourceCode() {

    T value = getValue();
    Class<?> enumClass = value.getClass();
    String name;
    if (this.unqualified) {
      name = enumClass.getSimpleName();
    } else {
      name = enumClass.getName();
    }
    return name + "." + value.name();
  }

  @Override
  public JavaConstant<T> withValue(T newValue) {

    return new JavaEnumConstant<>(newValue, this.unqualified);
  }

  /**
   * @param <T> type of the {@link Enum} {@link #getValue() value}.
   * @param value the constant value.
   * @param unqualified - {@code true} to use the {@link Class#getSimpleName() simple name}, {@code false}
   *        otherwise (for {@link Class#getName() qualified name}).
   * @return the {@link JavaConstant} for the given {@code value}.
   */
  public static <T extends Enum<T>> JavaEnumConstant<T> of(T value, boolean unqualified) {

    return new JavaEnumConstant<>(value, unqualified);
  }

}
