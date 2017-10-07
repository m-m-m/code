/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.expression;

import java.io.IOException;

import net.sf.mmm.code.api.expression.CodeCondition;
import net.sf.mmm.code.api.expression.CodeExpression;
import net.sf.mmm.code.api.expression.CodeTernaryExpression;
import net.sf.mmm.code.api.syntax.CodeSyntax;

/**
 * Generic implementation of {@link CodeTernaryExpression}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class GenericTernaryExpression extends GenericExpression implements CodeTernaryExpression {

  private final CodeCondition condition;

  private final CodeExpression ifArg;

  private final CodeExpression elseArg;

  /**
   * The constructor.
   *
   * @param condition the {@link #getCondition() condition}.
   * @param ifArg the {@link #getIfArg() if-argument}.
   * @param elseArg the {@link #getElseArg() else-argument}.
   */
  public GenericTernaryExpression(CodeCondition condition, CodeExpression ifArg, CodeExpression elseArg) {

    super();
    this.condition = condition;
    this.ifArg = ifArg;
    this.elseArg = elseArg;
  }

  @Override
  public CodeCondition getCondition() {

    return this.condition;
  }

  @Override
  public CodeExpression getIfArg() {

    return this.ifArg;
  }

  @Override
  public CodeExpression getElseArg() {

    return this.elseArg;
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeSyntax syntax) throws IOException {

    this.condition.write(sink, newline, defaultIndent, currentIndent);
    sink.append(" ? ");
    this.ifArg.write(sink, newline, defaultIndent, currentIndent);
    sink.append(" : ");
    this.elseArg.write(sink, newline, defaultIndent, currentIndent);
  }

}
