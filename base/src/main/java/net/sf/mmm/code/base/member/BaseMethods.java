/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.member;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sf.mmm.code.api.member.CodeMethods;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.base.type.BaseGenericType;
import net.sf.mmm.code.base.type.BaseType;

/**
 * Base implementation of {@link CodeMethods}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseMethods extends BaseOperations<BaseMethod> implements CodeMethods<BaseMethod>, CodeNodeItemWithGenericParent<BaseType, BaseMethods> {

  /**
   * The constructor.
   *
   * @param declaringType the {@link #getDeclaringType()}.
   */
  public BaseMethods(BaseType declaringType) {

    super(declaringType);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseMethods} to copy.
   * @param declaringType the {@link #getDeclaringType()}.
   */
  public BaseMethods(BaseMethods template, BaseType declaringType) {

    super(template, declaringType);
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
    Class<?> reflectiveObject = getParent().getReflectiveObject();
    if (reflectiveObject != null) {
      for (Method method : reflectiveObject.getDeclaredMethods()) {
        BaseMethod javaMethod = new BaseMethod(this, method);
        addInternal(javaMethod);
      }
    }
  }

  @Override
  public Iterable<? extends BaseMethod> getAll() {

    List<BaseMethod> list = new ArrayList<>(getDeclared());
    collectMethods(list);
    return list;
  }

  private void collectMethods(List<BaseMethod> list) {

    for (BaseGenericType superType : getDeclaringType().getSuperTypes().getDeclared()) {
      BaseMethods javaMethods = superType.asType().getMethods();
      list.addAll(javaMethods.getDeclared());
      javaMethods.collectMethods(list);
    }
  }

  @Override
  public BaseMethod get(BaseMethod method) {

    String name = method.getName();
    for (BaseMethod myMethod : getDeclared()) {
      if (myMethod.getName().equals(name)) {
        if (myMethod.getParameters().isInvokable(method.getParameters())) {
          return myMethod;
        }
      }
    }
    for (BaseGenericType superType : getDeclaringType().getSuperTypes().getDeclared()) {
      BaseMethod myMethod = superType.asType().getMethods().get(method);
      if (myMethod != null) {
        return myMethod;
      }
    }
    return null;
  }

  @Override
  public BaseMethod getDeclared(String name, CodeGenericType... parameterTypes) {

    for (BaseMethod method : getDeclared()) {
      if (method.getName().equals(name)) {
        if (method.getParameters().isInvokable(parameterTypes)) {
          return method;
        }
      }
    }
    return null;
  }

  @Override
  public BaseMethod add(String name) {

    verifyMutalbe();
    BaseMethod method = new BaseMethod(this, name);
    add(method);
    return method;
  }

  @Override
  public BaseMethods getSourceCodeObject() {

    BaseType sourceType = getParent().getSourceCodeObject();
    if (sourceType == null) {
      return null;
    }
    return sourceType.getMethods();
  }

  @Override
  public BaseMethods copy() {

    return copy(getParent());
  }

  @Override
  public BaseMethods copy(BaseType newParent) {

    return new BaseMethods(this, newParent);
  }

}
