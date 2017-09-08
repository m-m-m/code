/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.member;

import java.io.IOException;
import java.util.Objects;

import net.sf.mmm.code.api.expression.CodeExpression;
import net.sf.mmm.code.api.member.CodeField;
import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.type.CodeGenericType;

/**
 * Implementation of {@link CodeField} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaField extends JavaMember implements CodeField, CodeNodeItemWithGenericParent<JavaFields, JavaField> {

  private final JavaFields parent;

  private CodeGenericType type;

  private CodeExpression initializer;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param name the {@link #getName() name}.
   */
  public JavaField(JavaFields parent, String name) {

    super(CodeModifiers.MODIFIERS_PRIVATE, name);
    this.parent = parent;
    this.type = getContext().getRootType();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaField} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaField(JavaField template, JavaFields parent) {

    super(template);
    this.parent = parent;
    this.type = template.type;
    this.initializer = template.initializer;
  }

  @Override
  public JavaFields getParent() {

    return this.parent;
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
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    JavaField other = (JavaField) obj;
    if (!Objects.equals(this.type, other.type)) {
      return false;
    }
    if (!Objects.equals(this.initializer, other.initializer)) {
      return false;
    }
    return true;
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent) throws IOException {

    super.doWrite(sink, newline, defaultIndent, currentIndent);
    this.type.writeReference(sink, false);
    sink.append(' ');
    sink.append(getName());
    if (this.initializer != null) {
      sink.append(" = ");
      this.initializer.write(sink, "", "");
    }
    sink.append(';');
    sink.append(newline);
  }

  @Override
  public JavaField copy() {

    return copy(this.parent);
  }

  @Override
  public JavaField copy(JavaFields newParent) {

    return new JavaField(this, newParent);
  }

}
