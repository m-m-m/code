/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.member;

import java.io.IOException;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

import net.sf.mmm.code.api.arg.CodeReturn;
import net.sf.mmm.code.api.member.CodeMethod;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.syntax.CodeSyntax;
import net.sf.mmm.code.base.arg.BaseReturn;
import net.sf.mmm.code.base.type.BaseGenericType;
import net.sf.mmm.code.base.type.BaseSuperTypes;
import net.sf.mmm.code.base.type.BaseType;
import net.sf.mmm.code.base.type.BaseTypeVariables;

/**
 * Base implementation of {@link CodeMethod}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseMethod extends BaseOperation implements CodeMethod, CodeNodeItemWithGenericParent<BaseMethods, BaseMethod> {

  private final BaseMethods parent;

  private final Method reflectiveObject;

  private BaseMethod sourceCodeObject;

  private BaseReturn returns;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param name the {@link #getName() name}.
   */
  public BaseMethod(BaseMethods parent, String name) {

    this(parent, name, null);
  }

  /**
   * The constructor.
   *
   * @param typeVariables the {@link #getTypeParameters() type variables}.
   * @param parent the {@link #getParent() parent}.
   * @param name the {@link #getName() name}.
   */
  public BaseMethod(BaseTypeVariables typeVariables, BaseMethods parent, String name) {

    super(name, typeVariables);
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

    super(name);
    this.parent = parent;
    this.reflectiveObject = reflectiveObject;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseMethod} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public BaseMethod(BaseMethod template, BaseMethods parent) {

    super(template);
    this.parent = parent;
    this.reflectiveObject = null;
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
    getReturns();
  }

  @Override
  public Pattern getNamePattern() {

    return NAME_PATTERN;
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
  public void setReturns(CodeReturn returns) {

    verifyMutalbe();
    this.returns = (BaseReturn) returns;
  }

  @Override
  public Method getReflectiveObject() {

    return this.reflectiveObject;
  }

  @Override
  public BaseMethod getSourceCodeObject() {

    if (this.sourceCodeObject != null) {
      return this.sourceCodeObject;
    }
    if (isInitialized()) {
      return null;
    }
    BaseMethods sourceMethods = this.parent.getSourceCodeObject();
    if (sourceMethods != null) {
      this.sourceCodeObject = sourceMethods.get(this); // TODO getDeclared instead of get
    }
    return this.sourceCodeObject;
  }

  @Override
  public BaseMethod getParentMethod() {

    return getParentMethod(this.parent.getParent());
  }

  private BaseMethod getParentMethod(BaseType type) {

    BaseMethod parentMethod;
    if (type.isClass() || type.isEnumeration()) { // enumeration can override Object methods
      parentMethod = getParentMethodFromClasses(type);
      if (parentMethod != null) {
        return parentMethod;
      }
    }
    return getParentMethodFromInterfaces(type);
  }

  private BaseMethod getParentMethodFromClasses(BaseType type) {

    BaseGenericType superGenericClass = type.getSuperTypes().getSuperClass();
    if (superGenericClass == null) {
      return null;
    }
    BaseType superClass = superGenericClass.asType();
    BaseMethod parentMethod = superClass.getMethods().get(this);
    if (parentMethod != null) {
      return parentMethod;
    }
    return getParentMethodFromClasses(superClass);
  }

  private BaseMethod getParentMethodFromInterfaces(BaseType type) {

    BaseSuperTypes superTypes = type.getSuperTypes();
    for (BaseGenericType superType : superTypes.getDeclared()) {
      if (superType.isInterface()) {
        BaseType superInterface = superType.asType();
        BaseMethod parentMethod = superInterface.getMethods().get(this);
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
  public BaseMethod copy() {

    return copy(this.parent);
  }

  @Override
  public BaseMethod copy(BaseMethods newParent) {

    return new BaseMethod(this, newParent);
  }

  @Override
  protected void doWriteSignature(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeSyntax syntax) throws IOException {

    sink.append(syntax.getMethodKeyword());
    String start = syntax.getMethodReturnStart();
    if (start != null) {
      sink.append(start);
      getReturns().write(sink, newline, defaultIndent, currentIndent, syntax);
    }
    super.doWriteSignature(sink, newline, defaultIndent, currentIndent, syntax);
  }

  @Override
  protected void doWriteParameters(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeSyntax syntax) throws IOException {

    super.doWriteParameters(sink, newline, defaultIndent, currentIndent, syntax);
    String end = syntax.getMethodReturnEnd();
    if (end != null) {
      sink.append(end);
      getReturns().write(sink, newline, defaultIndent, currentIndent, syntax);
    }
  }

}
