/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.statement;

import java.io.IOException;

import io.github.mmm.code.api.expression.CodeConstant;
import io.github.mmm.code.api.expression.CodeExpression;
import io.github.mmm.code.api.language.CodeLanguage;
import io.github.mmm.code.api.statement.CodeLocalVariable;
import io.github.mmm.code.api.type.CodeGenericType;
import io.github.mmm.code.base.source.BaseSource;

/**
 * Base implementation of {@link CodeLocalVariable}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseLocalVariable extends BaseAtomicStatement implements CodeLocalVariable {

  private final String name;

  private final CodeGenericType type;

  private final CodeExpression expression;

  private final boolean finalFlag;

  /**
   * The constructor.
   *
   * @param source the {@link BaseSource} defining this variable.
   * @param name the {@link #getName() name}.
   * @param type the {@link #getType() type}.
   * @param expression the assignment {@link #getExpression() expression}.
   * @param finalFlag the {@link #isFinal() final} flag.
   */
  public BaseLocalVariable(BaseSource source, String name, CodeGenericType type, CodeExpression expression, boolean finalFlag) {

    super();
    this.name = source.getContext().getLanguage().verifyName(this, name);
    this.type = type;
    this.finalFlag = finalFlag;
    this.expression = expression;
  }

  @Override
  public String getName() {

    return this.name;
  }

  @Override
  public CodeGenericType getType() {

    return this.type;
  }

  @Override
  public boolean isFinal() {

    return this.finalFlag;
  }

  @Override
  public CodeExpression getExpression() {

    return this.expression;
  }

  @Override
  public CodeLocalVariable getVariable(String variableName) {

    if (this.name.equals(variableName)) {
      return this;
    }
    return null;
  }

  @Override
  public CodeConstant evaluate() {

    return null;
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    sink.append(currentIndent);
    sink.append(language.getKeywordForVariable(this));
    language.writeDeclaration(this, sink);
    if (this.expression != null) {
      sink.append(" = ");
      this.expression.write(sink, newline, defaultIndent, currentIndent, language);
    }
    sink.append(language.getStatementTerminator());
    sink.append(newline);
  }

}
