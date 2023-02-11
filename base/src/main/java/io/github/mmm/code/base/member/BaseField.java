/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.member;

import java.io.IOException;
import java.lang.reflect.Field;

import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.copy.CodeCopyType;
import io.github.mmm.code.api.expression.CodeConstant;
import io.github.mmm.code.api.expression.CodeExpression;
import io.github.mmm.code.api.language.CodeLanguage;
import io.github.mmm.code.api.member.CodeField;
import io.github.mmm.code.api.member.CodeFields;
import io.github.mmm.code.api.member.CodeMethod;
import io.github.mmm.code.api.member.CodeProperty;
import io.github.mmm.code.api.merge.CodeMergeStrategy;
import io.github.mmm.code.api.merge.CodeMergeStrategyDecider;
import io.github.mmm.code.api.modifier.CodeModifiers;
import io.github.mmm.code.api.type.CodeGenericType;

/**
 * Base implementation of {@link CodeField}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseField extends BaseMember implements CodeField {

  private final BaseFields parent;

  private final Field reflectiveObject;

  private CodeGenericType type;

  private CodeExpression initializer;

  private CodeField sourceCodeObject;

  private CodeMethod getter;

  private CodeMethod setter;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param name the {@link #getName() name}.
   */
  public BaseField(BaseFields parent, String name) {

    this(parent, name, CodeModifiers.MODIFIERS_PRIVATE, null);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May be {@code null}.
   */
  public BaseField(BaseFields parent, Field reflectiveObject) {

    this(parent, reflectiveObject.getName(), CodeModifiers.of(reflectiveObject.getModifiers()), reflectiveObject);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param name the {@link #getName() name}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May be {@code null}.
   */
  private BaseField(BaseFields parent, String name, CodeModifiers modifiers, Field reflectiveObject) {

    super(parent, modifiers, name);
    this.parent = parent;
    this.reflectiveObject = reflectiveObject;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseField} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseField(BaseField template, CodeCopyMapper mapper) {

    super(template, mapper);
    this.parent = mapper.map(template.parent, CodeCopyType.PARENT);
    this.reflectiveObject = null;
    this.type = mapper.map(template.type, CodeCopyType.REFERENCE);
    this.initializer = template.initializer; // TODO mapper.map(template.initializer, CodeCopyType.CHILD);
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
  public CodeField getSourceCodeObject() {

    if (this.sourceCodeObject != null) {
      return this.sourceCodeObject;
    }
    if (isInitialized()) {
      return null;
    }
    CodeFields sourceFields = this.parent.getSourceCodeObject();
    if (sourceFields != null) {
      this.sourceCodeObject = sourceFields.getDeclared(getName());
    }
    return this.sourceCodeObject;
  }

  @Override
  public CodeField merge(CodeField other, CodeMergeStrategyDecider decider, CodeMergeStrategy parentStrategy) {

    CodeMergeStrategy strategy = decider.decide(this, other, parentStrategy);
    if (strategy == CodeMergeStrategy.KEEP) {
      return this;
    }
    doMerge(other, strategy);
    boolean override = (strategy == CodeMergeStrategy.OVERRIDE);
    if (override) {
      this.type = other.getType();
      setName(other.getName());
    }
    if (override || (strategy == CodeMergeStrategy.MERGE_OVERRIDE_BODY)) {
      this.initializer = other.getInitializer();
    }
    return this;
  }

  @Override
  public BaseField copy() {

    return copy(getDefaultCopyMapper());
  }

  @Override
  public BaseField copy(CodeCopyMapper mapper) {

    return new BaseField(this, mapper);
  }

  @Override
  public CodeMethod getGetter() {

    if (this.getter == null) {
      CodeProperty property = this.parent.getParent().getProperties().get(getName());
      if (property != null) {
        this.getter = property.getGetter();
      }
    }
    return this.getter;
  }

  @Override
  public CodeMethod getOrCreateGetter() {

    getGetter();
    if (this.getter == null) {
      this.getter = getContext().getFactory().createGetter(getDeclaringType(), getName(), getType(), true,
          getDoc().getLinesAsArray());
      this.parent.getParent().getMethods().add(this.getter);
    }
    return this.getter;
  }

  @Override
  public CodeMethod getSetter() {

    if (this.setter == null) {
      CodeProperty property = this.parent.getParent().getProperties().get(getName());
      if (property != null) {
        this.setter = property.getSetter();
      }
    }
    return this.setter;
  }

  @Override
  public CodeMethod getOrCreateSetter() {

    getSetter();
    if (this.setter == null) {
      this.setter = getContext().getFactory().createSetter(getDeclaringType(), getName(), getType(), true,
          getDoc().getLinesAsArray());
      this.parent.getParent().getMethods().add(this.setter);
    }
    return this.setter;
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent,
      CodeLanguage language) throws IOException {

    language.writeField(this, sink, newline, defaultIndent, currentIndent);
  }

}
