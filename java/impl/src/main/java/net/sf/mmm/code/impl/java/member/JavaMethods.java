/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.member;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sf.mmm.code.api.member.CodeMethods;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.impl.java.arg.JavaReturn;
import net.sf.mmm.code.impl.java.type.JavaGenericType;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link CodeMethods} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaMethods extends JavaOperations<JavaMethod> implements CodeMethods<JavaMethod>, CodeNodeItemWithGenericParent<JavaType, JavaMethods> {

  /**
   * The constructor.
   *
   * @param declaringType the {@link #getDeclaringType()}.
   */
  public JavaMethods(JavaType declaringType) {

    super(declaringType);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaMethods} to copy.
   * @param declaringType the {@link #getDeclaringType()}.
   */
  public JavaMethods(JavaMethods template, JavaType declaringType) {

    super(template, declaringType);
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
    Class<?> reflectiveObject = getParent().getReflectiveObject();
    if (reflectiveObject != null) {
      for (Method method : reflectiveObject.getDeclaredMethods()) {
        JavaMethod javaMethod = new JavaMethod(this, method);
        addInternal(javaMethod);
      }
    }
  }

  @Override
  public List<? extends JavaMethod> getDeclared() {

    initialize();
    return getList();
  }

  @Override
  public Iterable<? extends JavaMethod> getAll() {

    List<JavaMethod> list = new ArrayList<>(getDeclared());
    collectMethods(list);
    return list;
  }

  private void collectMethods(List<JavaMethod> list) {

    for (JavaGenericType superType : getDeclaringType().getSuperTypes().getDeclared()) {
      JavaMethods javaMethods = superType.asType().getMethods();
      list.addAll(javaMethods.getDeclared());
      javaMethods.collectMethods(list);
    }
  }

  @Override
  public JavaMethod get(JavaMethod method) {

    String name = method.getName();
    for (JavaMethod myMethod : getDeclared()) {
      if (myMethod.getName().equals(name)) {
        if (myMethod.getParameters().isInvokable(method.getParameters())) {
          return myMethod;
        }
      }
    }
    for (JavaGenericType superType : getDeclaringType().getSuperTypes().getDeclared()) {
      JavaMethod myMethod = superType.asType().getMethods().get(method);
      if (myMethod != null) {
        return myMethod;
      }
    }
    return null;
  }

  @Override
  public JavaMethod getDeclared(String name, CodeGenericType... parameterTypes) {

    for (JavaMethod method : getDeclared()) {
      if (method.getName().equals(name)) {
        if (method.getParameters().isInvokable(parameterTypes)) {
          return method;
        }
      }
    }
    return null;
  }

  @Override
  public JavaMethod add(String name) {

    verifyMutalbe();
    JavaMethod method = new JavaMethod(this, name);
    JavaReturn returns = new JavaReturn(method, null);
    returns.setType(getContext().getVoidType());
    method.setReturns(returns);
    add(method);
    return method;
  }

  @Override
  public JavaMethods copy() {

    return copy(getParent());
  }

  @Override
  public JavaMethods copy(JavaType newParent) {

    return new JavaMethods(this, newParent);
  }

}
