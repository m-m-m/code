/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.expression.literal;

/**
 * Implementation of {@link JavaLiteral} for {@code boolean} literal.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class JavaLiteralNull extends JavaLiteral<Object> {

  /** {@link JavaLiteral} for {@code null}. */
  public static final JavaLiteralNull NULL = new JavaLiteralNull();

  private JavaLiteralNull() {

    super(null);
  }

  @Override
  public Class<Object> getJavaClass() {

    return Object.class;
  }

  @Override
  public boolean isPrimitive() {

    return false;
  }

  @Override
  public String getSourceCode() {

    return "null";
  }

  @Override
  public JavaLiteral<Object> withValue(Object newValue) {

    return this;
  }

  /**
   * @return the {@link JavaLiteralNull}.
   */
  public static JavaLiteralNull of() {

    return NULL;
  }
}
