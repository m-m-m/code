/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.expression;

import java.io.IOException;
import java.util.Objects;

import io.github.mmm.code.api.expression.CodeConstant;
import io.github.mmm.code.api.expression.CodeExpression;
import io.github.mmm.code.api.expression.CodeFieldReference;
import io.github.mmm.code.api.language.CodeLanguage;
import io.github.mmm.code.api.type.CodeGenericType;
import io.github.mmm.code.base.member.BaseField;

/**
 * Base implementation of {@link CodeFieldReference}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseFieldReference extends BaseMemberReference implements CodeFieldReference {

  private final BaseField field;

  /**
   * The constructor.
   *
   * @param expression the {@link #getExpression() expression}.
   * @param field the referenced {@link #getMember() field}.
   */
  public BaseFieldReference(CodeExpression expression, BaseField field) {

    super(expression);
    Objects.requireNonNull(field, "field");
    this.field = field;
  }

  /**
   * The constructor.
   *
   * @param type the {@link #getType() type}.
   * @param field the referenced {@link #getMember() field}.
   */
  public BaseFieldReference(CodeGenericType type, BaseField field) {

    super(type);
    Objects.requireNonNull(field, "field");
    assert (field.getDeclaringType().isAssignableFrom(type));
    this.field = field;
  }

  @Override
  public BaseField getMember() {

    return this.field;
  }

  @Override
  public CodeConstant evaluate() {

    if (this.field.getModifiers().isStatic()) {
      CodeExpression initializer = this.field.getInitializer();
      if (initializer != null) {
        return initializer.evaluate();
      }
    }
    return null;
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    super.doWrite(sink, newline, defaultIndent, currentIndent, language);
    sink.append(this.field.getName());
  }

  /**
   * @param field the {@link BaseField} to reference.
   * @return a new instance of {@link BaseFieldReference} for the given {@link BaseField}.
   */
  public static BaseFieldReference of(BaseField field) {

    if (field.getModifiers().isStatic()) {
      return new BaseFieldReference(field.getDeclaringType(), field);
    } else {
      return new BaseFieldReference(new BaseVariableThis(field.getDeclaringType()), field);
    }
  }

}
