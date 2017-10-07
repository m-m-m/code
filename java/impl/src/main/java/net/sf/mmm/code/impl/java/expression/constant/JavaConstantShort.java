/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.expression.constant;

/**
 * Implementation of {@link JavaFactoryConstant} for {@link Short} using {@link Short#valueOf(short)}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaConstantShort extends JavaFactoryConstant<Short> {

  /**
   * The constructor.
   *
   * @param value the {@link #getValue() value}.
   */
  private JavaConstantShort(Short value) {

    super(value);
  }

  @Override
  public JavaConstant<Short> withValue(Short newValue) {

    return new JavaConstantShort(newValue);
  }

  @Override
  public String getSourceCode() {

    return "Short.valueOf((short) " + getValue().toString() + ")";
  }

  /**
   * @param value the constant value.
   * @return the {@link JavaConstant} for the given {@code value}.
   */
  public static JavaConstant<Short> of(Short value) {

    return new JavaConstantShort(value);
  }

}
