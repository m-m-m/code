/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java.expression.constant;

/**
 * Implementation of {@link JavaConstant} for {@link Long} using {@link Long#valueOf(long)}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaConstantLong extends JavaConstant<Long> {

  /**
   * The constructor.
   *
   * @param value the {@link #getValue() value}.
   */
  private JavaConstantLong(Long value) {

    super(value);
  }

  @Override
  public JavaConstant<Long> withValue(Long newValue) {

    return new JavaConstantLong(newValue);
  }

  @Override
  public String getSourceCode() {

    return "Long.valueOf(" + this.value.toString() + "L)";
  }

  /**
   * @param value the constant value.
   * @return the {@link JavaConstant} for the given {@code value}.
   */
  public static JavaConstant<Long> of(Long value) {

    return new JavaConstantLong(value);
  }

}
