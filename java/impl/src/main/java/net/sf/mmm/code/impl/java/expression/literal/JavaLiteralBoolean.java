/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.expression.literal;

import net.sf.mmm.code.api.expression.CodeCondition;
import net.sf.mmm.code.api.expression.CodeLiteral;

/**
 * Implementation of {@link JavaLiteral} for {@code boolean} literal.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class JavaLiteralBoolean extends JavaLiteral<Boolean> implements CodeCondition {

  /** {@link JavaLiteral} for {@code true}. */
  public static final JavaLiteralBoolean TRUE = new JavaLiteralBoolean(Boolean.TRUE);

  /** {@link JavaLiteral} for {@code false}. */
  public static final JavaLiteralBoolean FALSE = new JavaLiteralBoolean(Boolean.FALSE);

  private JavaLiteralBoolean(Boolean value) {

    super(value);
  }

  @Override
  public String getSourceCode() {

    if (getValue().booleanValue()) {
      return "true";
    } else {
      return "false";
    }
  }

  @Override
  public JavaLiteral<Boolean> withValue(Boolean newValue) {

    if (newValue.booleanValue()) {
      return TRUE;
    } else {
      return FALSE;
    }
  }

  @Override
  public Class<Boolean> getJavaClass() {

    return boolean.class;
  }

  /**
   * @param value the literal value.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteralBoolean of(boolean value) {

    if (value) {
      return TRUE;
    } else {
      return FALSE;
    }
  }
}
