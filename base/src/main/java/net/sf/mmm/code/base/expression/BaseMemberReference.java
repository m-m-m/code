/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.expression;

import java.io.IOException;

import net.sf.mmm.code.api.expression.CodeExpression;
import net.sf.mmm.code.api.expression.CodeMemberReference;
import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.base.member.BaseMember;

/**
 * Generic implementation of {@link CodeMemberReference}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BaseMemberReference extends BaseExpression implements CodeMemberReference {

  private final CodeExpression expression;

  private final CodeGenericType type;

  /**
   * The constructor.
   *
   * @param type the {@link #getType() type}.
   */
  public BaseMemberReference(CodeGenericType type) {

    this(null, type);
  }

  /**
   * The constructor.
   *
   * @param expression the {@link #getExpression() expression}.
   */
  public BaseMemberReference(CodeExpression expression) {

    this(expression, null);
  }

  /**
   * The constructor.
   *
   * @param expression the {@link #getExpression() expression}.
   * @param type the {@link #getType() type}.
   */
  protected BaseMemberReference(CodeExpression expression, CodeGenericType type) {

    super();
    this.expression = expression;
    this.type = type;
  }

  @Override
  public CodeExpression getExpression() {

    return this.expression;
  }

  @Override
  public CodeGenericType getType() {

    return this.type;
  }

  @Override
  public abstract BaseMember getMember();

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    if (this.expression != null) {
      this.expression.write(sink, newline, defaultIndent, currentIndent, language);
      sink.append('.');
    } else if (this.type != null) {
      this.type.writeReference(sink, false);
      sink.append('.');
    }
  }

}
