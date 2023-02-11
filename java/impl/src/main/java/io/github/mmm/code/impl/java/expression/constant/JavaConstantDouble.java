/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java.expression.constant;

/**
 * Implementation of {@link JavaConstant} for {@link Double} using {@link Double#valueOf(double)}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaConstantDouble extends JavaConstant<Double> {

  /**
   * The constructor.
   *
   * @param value the {@link #getValue() value}.
   */
  private JavaConstantDouble(Double value) {

    super(value);
  }

  @Override
  public JavaConstant<Double> withValue(Double newValue) {

    return new JavaConstantDouble(newValue);
  }

  @Override
  public String getSourceCode() {

    return "Double.valueOf(" + this.value.toString() + "D)";
  }

  /**
   * @param value the constant value.
   * @return the {@link JavaConstant} for the given {@code value}.
   */
  public static JavaConstant<Double> of(Double value) {

    return new JavaConstantDouble(value);
  }

}
