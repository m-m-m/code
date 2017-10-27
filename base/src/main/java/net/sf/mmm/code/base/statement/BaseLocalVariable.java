/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.statement;

import java.io.IOException;

import net.sf.mmm.code.api.expression.CodeConstant;
import net.sf.mmm.code.api.expression.CodeExpression;
import net.sf.mmm.code.api.statement.CodeLocalVariable;
import net.sf.mmm.code.api.syntax.CodeSyntax;
import net.sf.mmm.code.api.type.CodeGenericType;

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
   * @param name the {@link #getName() name}.
   * @param type the {@link #getType() type}.
   * @param expression the assignment {@link #getExpression() expression}.
   * @param finalFlag the {@link #isFinal() final} flag.
   */
  public BaseLocalVariable(String name, CodeGenericType type, CodeExpression expression, boolean finalFlag) {

    super();
    verifyName(name, NAME_PATTERN);
    this.name = name;
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
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeSyntax syntax) throws IOException {

    sink.append(currentIndent);
    sink.append(syntax.getKeywordForVariable(this));
    syntax.writeDeclaration(this, sink);
    if (this.expression != null) {
      sink.append(" = ");
      this.expression.write(sink, newline, defaultIndent, currentIndent, syntax);
    }
    sink.append(syntax.getStatementTerminator());
    sink.append(newline);
  }

}
