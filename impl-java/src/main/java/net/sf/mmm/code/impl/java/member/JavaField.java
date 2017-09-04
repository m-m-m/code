/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.member;

import java.io.IOException;

import net.sf.mmm.code.api.expression.CodeExpression;
import net.sf.mmm.code.api.member.CodeField;
import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.impl.java.type.JavaType;
import net.sf.mmm.util.exception.api.DuplicateObjectException;

/**
 * Implementation of {@link CodeField} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaField extends JavaMember implements CodeField {

  private CodeGenericType type;

  private CodeExpression initializer;

  /**
   * The constructor.
   *
   * @param declaringType the {@link #getDeclaringType()}.
   */
  public JavaField(JavaType declaringType) {

    super(declaringType, CodeModifiers.MODIFIERS_PRIVATE);
    this.type = getContext().getRootType();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaField} to copy.
   * @param declaringType the {@link #getDeclaringType()}.
   */
  public JavaField(JavaField template, JavaType declaringType) {

    super(template, declaringType);
    this.type = template.type;
    this.initializer = template.initializer;
  }

  @Override
  public CodeGenericType getType() {

    return this.type;
  }

  @Override
  public void setType(CodeGenericType type) {

    verifyMutalbe();
    this.type = type;
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
  public void setName(String name) {

    JavaType declaringType = getDeclaringType();
    JavaFields fields = declaringType.getFields();
    JavaField duplicate = fields.getDeclared(name);
    if (duplicate != null) {
      throw new DuplicateObjectException(declaringType.getSimpleName() + ".fields", name);
    }
    String oldName = getName();
    super.setName(name);
    if (oldName.equals(name)) {
      return;
    }
    fields.rename(this, oldName);
    declaringType.getProperties().rename(this, oldName);
  }

  @Override
  protected void doWrite(Appendable sink, String defaultIndent, String currentIndent) throws IOException {

    super.doWrite(sink, defaultIndent, currentIndent);
    this.type.writeReference(sink, false);
    sink.append(' ');
    sink.append(getName());
    if (this.initializer != null) {
      sink.append(" = ");
      this.initializer.write(sink, "", "");
    }
    sink.append(';');
    writeNewline(sink);
  }

  @Override
  public JavaField copy(CodeType newDeclaringType) {

    return new JavaField(this, (JavaType) newDeclaringType);
  }

}
