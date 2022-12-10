/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.expression;

import java.io.IOException;

import io.github.mmm.code.api.expression.CodeComparisonOperatorExpression;
import io.github.mmm.code.api.expression.CodeExpression;
import io.github.mmm.code.api.expression.CodeLiteral;
import io.github.mmm.code.api.language.CodeLanguage;
import io.github.mmm.code.api.operator.CodeComparisonOperator;

/**
 * Generic implementation of {@link CodeComparisonOperatorExpression}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseComparisonOperatorExpression extends BaseOperatorExpression implements CodeComparisonOperatorExpression {

  private final CodeExpression leftArg;

  private final CodeComparisonOperator operator;

  private final CodeExpression rightArg;

  /**
   * The constructor.
   *
   * @param leftArg the {@link #getLeftArg() left argument}.
   * @param operator the {@link #getOperator() operator}.
   * @param rightArg the {@link #getRightArg() right argument}.
   */
  public BaseComparisonOperatorExpression(CodeExpression leftArg, CodeComparisonOperator operator, CodeExpression rightArg) {

    super();
    this.leftArg = leftArg;
    this.operator = operator;
    this.rightArg = rightArg;
  }

  @Override
  public CodeLiteral evaluate() {

    // can only be implemented in language specific sub-class
    return null;
  }

  @Override
  public CodeExpression getLeftArg() {

    return this.leftArg;
  }

  @Override
  public CodeComparisonOperator getOperator() {

    return this.operator;
  }

  @Override
  public CodeExpression getRightArg() {

    return this.rightArg;
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    this.leftArg.write(sink, newline, defaultIndent, currentIndent);
    sink.append(' ');
    sink.append(this.operator.getName());
    sink.append(' ');
    this.rightArg.write(sink, newline, defaultIndent, currentIndent);
  }

}
