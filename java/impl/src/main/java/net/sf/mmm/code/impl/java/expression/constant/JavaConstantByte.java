/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.expression.constant;

/**
 * Implementation of {@link JavaFactoryConstant} for {@link Byte} using {@link Byte#valueOf(byte)}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaConstantByte extends JavaFactoryConstant<Byte> {

  /**
   * The constructor.
   *
   * @param value the {@link #getValue() value}.
   */
  private JavaConstantByte(Byte value) {

    super(value);
  }

  @Override
  public JavaConstant<Byte> withValue(Byte newValue) {

    return new JavaConstantByte(newValue);
  }

  @Override
  public String getSourceCode() {

    return "Byte.valueOf((byte) " + getValue().toString() + ")";
  }

  /**
   * @param value the constant value.
   * @return the {@link JavaConstant} for the given {@code value}.
   */
  public static JavaConstant<Byte> of(Byte value) {

    return new JavaConstantByte(value);
  }

}
