/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.expression.literal;

import net.sf.mmm.code.api.expression.CodeLiteral;

/**
 * Implementation of {@link JavaLiteral} for {@link String} literal.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class JavaLiteralString extends JavaLiteral<String> {

  /** {@link JavaLiteral} for {@code true}. */
  public static final JavaLiteralString EMPTY = new JavaLiteralString("");

  private JavaLiteralString(String value) {

    super(value);
  }

  @Override
  public Class<String> getJavaClass() {

    return String.class;
  }

  @Override
  public boolean isPrimitive() {

    return false;
  }

  @Override
  public String getSourceCode() {

    return "\"" + getValue().replaceAll("\"", "\\\\\"") + "\"";
  }

  @Override
  public JavaLiteralString withValue(String newValue) {

    return of(newValue);
  }

  /**
   * @param value the literal value.
   * @return the {@link CodeLiteral} for the given {@code value}.
   */
  public static JavaLiteralString of(String value) {

    return new JavaLiteralString(value);
  }
}
