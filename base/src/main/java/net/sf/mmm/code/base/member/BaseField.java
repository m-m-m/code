/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.member;

import java.io.IOException;
import java.lang.reflect.Field;

import net.sf.mmm.code.api.expression.CodeConstant;
import net.sf.mmm.code.api.expression.CodeExpression;
import net.sf.mmm.code.api.member.CodeField;
import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.base.type.BaseGenericType;

/**
 * Base implementation of {@link CodeField}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseField extends BaseMember implements CodeField, CodeNodeItemWithGenericParent<BaseFields, BaseField> {

  private final BaseFields parent;

  private final Field reflectiveObject;

  private BaseGenericType type;

  private CodeExpression initializer;

  private BaseField sourceCodeObject;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param name the {@link #getName() name}.
   */
  public BaseField(BaseFields parent, String name) {

    this(parent, name, null);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May be {@code null}.
   */
  public BaseField(BaseFields parent, Field reflectiveObject) {

    this(parent, reflectiveObject.getName(), reflectiveObject);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param name the {@link #getName() name}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May be {@code null}.
   */
  private BaseField(BaseFields parent, String name, Field reflectiveObject) {

    super(CodeModifiers.MODIFIERS_PRIVATE, name);
    this.parent = parent;
    this.reflectiveObject = reflectiveObject;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseField} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public BaseField(BaseField template, BaseFields parent) {

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
  public BaseFields getParent() {

    return this.parent;
  }

  @Override
  public BaseGenericType getType() {

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
    this.type = (BaseGenericType) type;
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
  public CodeConstant evaluate() {

    if (this.initializer == null) {
      return null;
    }
    return this.initializer.evaluate();
  }

  @Override
  public Field getReflectiveObject() {

    return this.reflectiveObject;
  }

  @Override
  public BaseField getSourceCodeObject() {

    if (this.sourceCodeObject != null) {
      return this.sourceCodeObject;
    }
    if (isInitialized()) {
      return null;
    }
    BaseFields sourceFields = this.parent.getSourceCodeObject();
    if (sourceFields != null) {
      this.sourceCodeObject = sourceFields.getDeclared(getName());
    }
    return this.sourceCodeObject;
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    super.doWrite(sink, newline, defaultIndent, currentIndent, language);
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
  public BaseField copy() {

    return copy(this.parent);
  }

  @Override
  public BaseField copy(BaseFields newParent) {

    return new BaseField(this, newParent);
  }

}
