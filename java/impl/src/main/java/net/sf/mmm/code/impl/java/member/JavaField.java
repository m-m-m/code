/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.member;

import java.io.IOException;
import java.lang.reflect.Field;

import net.sf.mmm.code.api.expression.CodeExpression;
import net.sf.mmm.code.api.member.CodeField;
import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.syntax.CodeSyntax;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.impl.java.expression.constant.JavaConstant;
import net.sf.mmm.code.impl.java.item.JavaReflectiveObject;

/**
 * Implementation of {@link CodeField} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaField extends JavaMember implements CodeField, CodeNodeItemWithGenericParent<JavaFields, JavaField>, JavaReflectiveObject<Field> {

  private final JavaFields parent;

  private final Field reflectiveObject;

  private CodeGenericType type;

  private CodeExpression initializer;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param name the {@link #getName() name}.
   */
  public JavaField(JavaFields parent, String name) {

    this(parent, name, null);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May be {@code null}.
   */
  public JavaField(JavaFields parent, Field reflectiveObject) {

    this(parent, reflectiveObject.getName(), reflectiveObject);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param name the {@link #getName() name}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May be {@code null}.
   */
  private JavaField(JavaFields parent, String name, Field reflectiveObject) {

    super(CodeModifiers.MODIFIERS_PRIVATE, name);
    this.parent = parent;
    this.reflectiveObject = reflectiveObject;
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
    this.reflectiveObject = null;
    this.type = template.type;
    this.initializer = template.initializer;
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
    getType();
  }

  @Override
  public JavaFields getParent() {

    return this.parent;
  }

  @Override
  public CodeGenericType getType() {

    if (this.type == null) {
      if (this.reflectiveObject != null) {
        this.type = getContext().getType(this.reflectiveObject.getGenericType(), this);
      } else {
        this.type = getContext().getRootType();
      }
    }
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
  public JavaConstant<?> evaluate() {

    if (this.initializer == null) {
      return null;
    }
    return (JavaConstant<?>) this.initializer.evaluate();
  }

  @Override
  public Field getReflectiveObject() {

    return this.reflectiveObject;
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeSyntax syntax) throws IOException {

    super.doWrite(sink, newline, defaultIndent, currentIndent, syntax);
    getType().writeReference(sink, false);
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
