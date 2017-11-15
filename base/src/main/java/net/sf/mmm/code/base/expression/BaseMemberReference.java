/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.expression;

import java.io.IOException;

import net.sf.mmm.code.api.expression.CodeConstant;
import net.sf.mmm.code.api.expression.CodeExpression;
import net.sf.mmm.code.api.expression.CodeMemberReference;
import net.sf.mmm.code.api.member.CodeMember;
import net.sf.mmm.code.api.language.CodeLanguage;

/**
 * Generic implementation of {@link CodeMemberReference}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BaseMemberReference extends BaseExpression implements CodeMemberReference {

  private final CodeExpression expression;

  /**
   * The constructor.
   *
   * @param expression the {@link #getExpression() expression}.
   */
  public BaseMemberReference(CodeExpression expression) {

    super();
    this.expression = expression;
  }

  @Override
  public CodeConstant evaluate() {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public CodeExpression getExpression() {

    return this.expression;
  }

  @Override
  public CodeMember getMember() {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    if (this.expression != null) {
      this.expression.write(sink, newline, defaultIndent, currentIndent, language);
      sink.append('.');
    }
  }

}
