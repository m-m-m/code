/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java.expression.constant;

import io.github.mmm.code.impl.java.expression.literal.JavaLiteralChar;

/**
 * Implementation of {@link JavaFactoryConstant} for {@link Character} using {@link Character#valueOf(char)}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaConstantCharacter extends JavaFactoryConstant<Character> {

  /**
   * The constructor.
   *
   * @param value the {@link #getValue() value}.
   */
  private JavaConstantCharacter(Character value) {

    super(value);
  }

  @Override
  public JavaConstant<Character> withValue(Character newValue) {

    return new JavaConstantCharacter(newValue);
  }

  @Override
  public String getSourceCode() {

    return "Character.valueOf(" + JavaLiteralChar.toCharLiteral(getValue().charValue()) + ")";
  }

  /**
   * @param value the constant value.
   * @return the {@link JavaConstant} for the given {@code value}.
   */
  public static JavaConstant<Character> of(Character value) {

    return new JavaConstantCharacter(value);
  }

}
