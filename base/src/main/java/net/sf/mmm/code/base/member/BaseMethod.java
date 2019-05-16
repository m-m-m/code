/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.member;

import java.io.IOException;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;

import net.sf.mmm.code.api.copy.CodeCopyMapper;
import net.sf.mmm.code.api.copy.CodeCopyType;
import net.sf.mmm.code.api.expression.CodeExpression;
import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.api.member.CodeField;
import net.sf.mmm.code.api.member.CodeMethod;
import net.sf.mmm.code.api.member.CodeMethods;
import net.sf.mmm.code.api.merge.CodeMergeStrategy;
import net.sf.mmm.code.api.merge.CodeMergeStrategyDecider;
import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.api.modifier.CodeVisibility;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeSuperTypes;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.base.arg.BaseReturn;
import net.sf.mmm.code.base.type.BaseType;
import net.sf.mmm.code.base.type.BaseTypeVariables;

/**
 * Base implementation of {@link CodeMethod}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseMethod extends BaseOperation implements CodeMethod {

  private final BaseMethods parent;

  private final Method reflectiveObject;

  private CodeMethod sourceCodeObject;

  private BaseReturn returns;

  private CodeExpression defaultValue;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param name the {@link #getName() name}.
   */
  public BaseMethod(BaseMethods parent, String name) {

    this(parent, name, (Method) null);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param name the {@link #getName() name}.
   * @param typeVariables the {@link #getTypeParameters() type variables}.
   */
  public BaseMethod(BaseMethods parent, String name, BaseTypeVariables typeVariables) {

    super(parent, name, typeVariables);
    this.parent = parent;
    this.reflectiveObject = null;
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May be {@code null}.
   */
  public BaseMethod(BaseMethods parent, Method reflectiveObject) {

    this(parent, reflectiveObject.getName(), reflectiveObject);
  }

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param parent the {@link #getParent() parent}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May be {@code null}.
   */
  private BaseMethod(BaseMethods parent, String name, Method reflectiveObject) {

    super(parent, name);
    this.parent = parent;
    this.reflectiveObject = reflectiveObject;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseMethod} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseMethod(BaseMethod template, CodeCopyMapper mapper) {

    super(template, mapper);
    this.parent = mapper.map(template.parent, CodeCopyType.PARENT);
    this.returns = mapper.map(template.returns, CodeCopyType.CHILD);
    this.reflectiveObject = null;
    this.defaultValue = template.defaultValue;
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
    getReturns();
  }

  @Override
  public BaseMethods getParent() {

    return this.parent;
  }

  @Override
  protected void doSetImmutable() {

    super.doSetImmutable();
    getReturns().setImmutableIfNotSystemImmutable();
  }

  @Override
  public BaseReturn getReturns() {

    if (this.returns == null) {
      AnnotatedType returnType = null;
      if (this.reflectiveObject != null) {
        returnType = this.reflectiveObject.getAnnotatedReturnType();
      }
      this.returns = new BaseReturn(this, returnType);
    }
    return this.returns;
  }

  @Override
  public CodeExpression getDefaultValue() {

    if (this.defaultValue == null) {
      if (this.reflectiveObject != null) {
        Object value = this.reflectiveObject.getDefaultValue();
        if (value != null) {
          boolean primitive = getReturns().getType().asType().isPrimitive();
          this.defaultValue = getContext().getFactory().createExpression(value, primitive);
        }
      }
      if ((this.defaultValue == null) && (getSourceCodeObject() != null)) {
        this.defaultValue = this.sourceCodeObject.getDefaultValue();
      }
    }
    return this.defaultValue;
  }

  @Override
  public void setDefaultValue(CodeExpression defaultValue) {

    verifyMutalbe();
    this.defaultValue = defaultValue;
  }

  @Override
  public Method getReflectiveObject() {

    return this.reflectiveObject;
  }

  @Override
  public boolean canHaveBody() {

    CodeModifiers modifiers = getModifiers();
    if (modifiers.isAbstract()) {
      return false;
    }
    if (this.parent.getParent().getCategory().isInterface()) {
      return false;
    }
    return true;
  }

  @Override
  public CodeMethod getSourceCodeObject() {

    if (this.sourceCodeObject == null) {
      CodeMethods sourceMethods = this.parent.getSourceCodeObject();
      if (sourceMethods != null) {
        this.sourceCodeObject = sourceMethods.get(this); // TODO getDeclared instead of get
      }
    }
    return this.sourceCodeObject;
  }

  @Override
  public CodeMethod getParentMethod() {

    return getParentMethod(this.parent.getParent());
  }

  private CodeMethod getParentMethod(BaseType type) {

    CodeMethod parentMethod;
    if (type.isClass() || type.isEnumeration()) { // enumeration can override Object methods
      parentMethod = getParentMethodFromClasses(type);
      if (parentMethod != null) {
        return parentMethod;
      }
    }
    return getParentMethodFromInterfaces(type);
  }

  private CodeMethod getParentMethodFromClasses(CodeType type) {

    CodeGenericType superGenericClass = type.getSuperTypes().getSuperClass();
    if (superGenericClass == null) {
      return null;
    }
    CodeType superClass = superGenericClass.asType();
    CodeMethod parentMethod = superClass.getMethods().get(this);
    if (parentMethod != null) {
      return parentMethod;
    }
    return getParentMethodFromClasses(superClass);
  }

  private CodeMethod getParentMethodFromInterfaces(CodeType type) {

    CodeSuperTypes superTypes = type.getSuperTypes();
    for (CodeGenericType superType : superTypes.getDeclared()) {
      if (superType.isInterface()) {
        CodeType superInterface = superType.asType();
        CodeMethod parentMethod = superInterface.getMethods().get(this);
        if (parentMethod != null) {
          return parentMethod;
        }
        parentMethod = getParentMethodFromInterfaces(superInterface);
        if (parentMethod != null) {
          return parentMethod;
        }
      }
    }
    return null;
  }

  @Override
  public CodeField getAccessorField() {

    // not supported by default. Languages like C# have to extend this class and override this method
    return null;
  }

  @Override
  public CodeMethod merge(CodeMethod other, CodeMergeStrategyDecider decider, CodeMergeStrategy parentStrategy) {

    CodeMergeStrategy strategy = decider.decide(this, other, parentStrategy);
    if (strategy == CodeMergeStrategy.KEEP) {
      return this;
    }
    doMerge(other, strategy);
    boolean override = (strategy == CodeMergeStrategy.OVERRIDE);
    if (override) {
      setName(other.getName());
    }
    getReturns().merge(other.getReturns(), strategy);
    if (override || (strategy == CodeMergeStrategy.MERGE_OVERRIDE_BODY)) {
      this.defaultValue = other.getDefaultValue();
    }
    return this;
  }

  @Override
  public BaseMethod copy() {

    return copy(getDefaultCopyMapper());
  }

  @Override
  public BaseMethod copy(CodeCopyMapper mapper) {

    return new BaseMethod(this, mapper);
  }

  @Override
  protected void doWriteSignature(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language)
      throws IOException {

    sink.append(language.getMethodKeyword());
    String start = language.getMethodReturnStart();
    if (start != null) {
      sink.append(start);
      getReturns().write(sink, newline, defaultIndent, currentIndent, language);
      sink.append(' ');
    }
    super.doWriteSignature(sink, newline, defaultIndent, currentIndent, language);
  }

  @Override
  protected void doWriteParameters(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language)
      throws IOException {

    super.doWriteParameters(sink, newline, defaultIndent, currentIndent, language);
    String end = language.getMethodReturnEnd();
    if (end != null) {
      sink.append(end);
      getReturns().write(sink, newline, defaultIndent, currentIndent, language);
    }
  }

  @Override
  protected void doWriteModifiers(Appendable sink) throws IOException {

    CodeModifiers modifiers = getModifiers();
    if (getDeclaringType().isInterface() && CodeVisibility.PUBLIC.equals(modifiers.getVisibility())) {
      modifiers.formatModifiers(sink);
    } else {
      sink.append(modifiers.toString());
    }
  }

}
