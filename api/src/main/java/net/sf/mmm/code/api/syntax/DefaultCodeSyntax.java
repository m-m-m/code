/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.syntax;

import java.io.IOException;

import net.sf.mmm.code.api.expression.CodeVariable;
import net.sf.mmm.code.api.expression.CodeVariableThis;
import net.sf.mmm.code.api.statement.CodeLocalVariable;

/**
 * The default implementation of {@link CodeSyntax} (suitable for Java or C#).
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class DefaultCodeSyntax implements CodeSyntax {

  /** The singleton instance. */
  public static final DefaultCodeSyntax INSTANCE = new DefaultCodeSyntax();

  @Override
  public String getVariableNameThis() {

    return CodeVariableThis.NAME_THIS;
  }

  @Override
  public String getKeyword(CodeLocalVariable variable) {

    if (variable.isFinal()) {
      return "final ";
    }
    return "";
  }

  @Override
  public void writeDeclaration(CodeVariable variable, Appendable sink) throws IOException {

    variable.getType().writeReference(sink, false);
    sink.append(' ');
    sink.append(variable.getName());
  }

  @Override
  public String getStatementTerminator() {

    return ";";
  }

}
