/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java.expression.constant;

/**
 * Implementation of {@link JavaFactoryConstant} for {@link Float} using {@link Float#valueOf(float)}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaConstantFloat extends JavaFactoryConstant<Float> {

  /**
   * The constructor.
   *
   * @param value the {@link #getValue() value}.
   */
  private JavaConstantFloat(Float value) {

    super(value);
  }

  @Override
  public JavaConstant<Float> withValue(Float newValue) {

    return new JavaConstantFloat(newValue);
  }

  @Override
  public String getSourceCode() {

    return "Float.valueOf(" + getValue().toString() + "F)";
  }

  /**
   * @param value the constant value.
   * @return the {@link JavaConstant} for the given {@code value}.
   */
  public static JavaConstant<Float> of(Float value) {

    return new JavaConstantFloat(value);
  }

}
