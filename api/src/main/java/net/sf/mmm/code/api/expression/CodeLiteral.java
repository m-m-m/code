/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.expression;

import net.sf.mmm.code.api.CodeType;

/**
 * {@link CodeExpression} for a literal value.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeLiteral extends CodeExpression {

  /**
   * @return the literal {@link String}. Shall not be {@code null}.
   */
  String getLiteral();

  /**
   * @return the literal value. May be {@code null} for literals that do not exist in Java (in case of a
   *         foreign language).
   */
  Object getValue();

  @Override
  default Object evaluate(CodeType type) {

    return getValue();
  }

}
