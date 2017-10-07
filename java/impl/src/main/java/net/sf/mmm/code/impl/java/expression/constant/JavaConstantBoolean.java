/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.expression.constant;

/**
 * Implementation of {@link JavaFactoryConstant} for {@link Boolean} using {@link Boolean#valueOf(boolean)}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaConstantBoolean extends JavaFactoryConstant<Boolean> {

  /** {@link JavaConstantBoolean} for {@link Boolean#TRUE}. */
  public static final JavaConstantBoolean TRUE = new JavaConstantBoolean(Boolean.TRUE);

  /** {@link JavaConstantBoolean} for {@link Boolean#FALSE}. */
  public static final JavaConstantBoolean FALSE = new JavaConstantBoolean(Boolean.FALSE);

  /**
   * The constructor.
   *
   * @param value the {@link #getValue() value}.
   */
  private JavaConstantBoolean(Boolean value) {

    super(value);
  }

  @Override
  public JavaConstant<Boolean> withValue(Boolean newValue) {

    return new JavaConstantBoolean(newValue);
  }

  @Override
  public String getSourceCode() {

    return "Boolean.valueOf(" + getValue().toString() + ")";
  }

  /**
   * @param value the constant value.
   * @return the {@link JavaConstant} for the given {@code value}.
   */
  public static JavaConstant<Boolean> of(Boolean value) {

    if (value.booleanValue()) {
      return TRUE;
    } else {
      return FALSE;
    }
  }

}
