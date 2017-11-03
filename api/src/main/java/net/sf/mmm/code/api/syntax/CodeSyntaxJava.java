/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.syntax;

import java.io.IOException;

import net.sf.mmm.code.api.expression.CodeVariable;
import net.sf.mmm.code.api.expression.CodeVariableThis;
import net.sf.mmm.code.api.statement.CodeLocalVariable;
import net.sf.mmm.code.api.type.CodeTypeCategory;

/**
 * The default implementation of {@link CodeSyntax} (for Java).
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class CodeSyntaxJava implements CodeSyntax {

  /** The {@link #getLanguageName() language name}. */
  public static final String LANGUAGE_NAME_JAVA = "Java";

  /** The singleton instance. */
  public static final CodeSyntaxJava INSTANCE = new CodeSyntaxJava();

  @Override
  public String getLanguageName() {

    return LANGUAGE_NAME_JAVA;
  }

  @Override
  public String getVariableNameThis() {

    return CodeVariableThis.NAME_THIS;
  }

  @Override
  public String getKeywordForVariable(CodeLocalVariable variable) {

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
  public String getKeywordForCategory(CodeTypeCategory category) {

    if (category.isAnnotation()) {
      return "@interface";
    } else if (category.isEnumeration()) {
      return "enum";
    } else {
      return category.toString();
    }
  }

  @Override
  public String getStatementTerminator() {

    return ";";
  }

}
