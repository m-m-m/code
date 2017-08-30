/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.member;

import java.io.IOException;

import net.sf.mmm.code.api.CodeGenericType;
import net.sf.mmm.code.api.CodeType;
import net.sf.mmm.code.api.expression.CodeExpression;
import net.sf.mmm.code.api.member.CodeField;
import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.impl.java.JavaType;

/**
 * Implementation of {@link CodeField} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaField extends JavaProperty implements CodeField {

  private CodeExpression initializer;

  /**
   * The constructor.
   *
   * @param declaringType the {@link #getDeclaringType()}.
   */
  public JavaField(JavaType declaringType) {

    super(declaringType, CodeModifiers.MODIFIERS_PRIVATE);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaField} to copy.
   */
  public JavaField(JavaField template) {

    super(template);
    this.initializer = template.initializer;
  }

  @Override
  public CodeExpression getInitializer() {

    return this.initializer;
  }

  @Override
  public void setInitializer(CodeExpression initializer) {

    verifyMutalbe();
    this.initializer = initializer;
  }

  @Override
  public JavaField inherit(CodeType declaring) {

    CodeGenericType currentType = getType();
    CodeGenericType newType = currentType.resolve(declaring);
    if (newType == currentType) {
      return this;
    }
    JavaField inherited = new JavaField(this);
    inherited.setType(newType);
    return inherited;
  }

  @Override
  protected void doWrite(Appendable sink, String defaultIndent, String currentIndent) throws IOException {

    super.doWrite(sink, defaultIndent, currentIndent);
    if (this.initializer != null) {
      sink.append(" = ");
      this.initializer.write(sink, "", "");
    }
    sink.append(';');
    writeNewline(sink);
  }

}
