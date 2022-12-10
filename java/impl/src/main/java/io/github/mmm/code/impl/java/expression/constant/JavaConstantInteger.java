/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java.expression.constant;

/**
 * Implementation of {@link JavaFactoryConstant} for {@link Integer} using {@link Integer#valueOf(int)}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaConstantInteger extends JavaFactoryConstant<Integer> {

  /**
   * The constructor.
   *
   * @param value the {@link #getValue() value}.
   */
  private JavaConstantInteger(Integer value) {

    super(value);
  }

  @Override
  public JavaConstant<Integer> withValue(Integer newValue) {

    return new JavaConstantInteger(newValue);
  }

  @Override
  public String getSourceCode() {

    return "Integer.valueOf(" + getValue().toString() + ")";
  }

  /**
   * @param value the constant value.
   * @return the {@link JavaConstant} for the given {@code value}.
   */
  public static JavaConstant<Integer> of(Integer value) {

    return new JavaConstantInteger(value);
  }

}
