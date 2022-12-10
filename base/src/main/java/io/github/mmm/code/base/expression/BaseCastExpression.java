/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.expression;

import java.io.IOException;
import java.util.Objects;

import io.github.mmm.code.api.expression.CodeCastExpression;
import io.github.mmm.code.api.expression.CodeConstant;
import io.github.mmm.code.api.expression.CodeExpression;
import io.github.mmm.code.api.language.CodeLanguage;
import io.github.mmm.code.api.type.CodeGenericType;

/**
 * Generic implementation of {@link CodeCastExpression}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseCastExpression extends BaseExpression implements CodeCastExpression {

  private final CodeGenericType type;

  private final CodeExpression expression;

  /**
   * The constructor.
   *
   * @param type the {@link #getType() type}.
   * @param expression the {@link #getExpression()}.
   */
  public BaseCastExpression(CodeGenericType type, CodeExpression expression) {

    super();
    Objects.requireNonNull(type, "type");
    Objects.requireNonNull(expression, "expression");
    this.type = type;
    this.expression = expression;
    assert !(expression instanceof CodeCastExpression);
  }

  @Override
  public CodeConstant evaluate() {

    // can only be implemented in language specific sub-class
    return null;
  }

  @Override
  public CodeGenericType getType() {

    return this.type;
  }

  @Override
  public CodeExpression getExpression() {

    return this.expression;
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    sink.append('(');
    this.type.writeReference(sink, false);
    sink.append(") ");
    this.expression.write(sink, newline, defaultIndent, currentIndent);
  }

}
