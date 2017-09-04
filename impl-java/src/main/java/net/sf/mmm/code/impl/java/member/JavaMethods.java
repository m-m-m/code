/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.member;

import java.util.ArrayList;
import java.util.List;

import net.sf.mmm.code.api.member.CodeMethod;
import net.sf.mmm.code.api.member.CodeMethods;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.impl.java.arg.JavaReturn;
import net.sf.mmm.code.impl.java.type.JavaGenericType;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link CodeMethods} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaMethods extends JavaMembers<CodeMethod> implements CodeMethods {

  private List<JavaMethod> methods;

  /**
   * The constructor.
   *
   * @param declaringType the {@link #getDeclaringType()}.
   */
  public JavaMethods(JavaType declaringType) {

    super(declaringType);
    this.methods = new ArrayList<>();
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
  protected void doSetImmutable() {

    super.doSetImmutable();
    this.methods = makeImmutable(this.methods);
  }

  @Override
  public List<? extends JavaMethod> getDeclared() {

    return this.methods;
  }

  @Override
  public Iterable<? extends JavaMethod> getAll() {

    List<JavaMethod> list = new ArrayList<>(this.methods);
    collectMethods(list);
    return list;
  }

  private void collectMethods(List<JavaMethod> list) {

    for (JavaGenericType superType : getDeclaringType().getSuperTypes().getDeclared()) {
      JavaMethods javaMethods = superType.asType().getMethods();
      list.addAll(javaMethods.methods);
      javaMethods.collectMethods(list);
    }
  }

  @Override
  public JavaMethod get(String name, CodeType... parameterTypes) {

    for (JavaMethod method : this.methods) {
      if (method.getName().equals(name)) {

      }
    }
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public JavaMethod add(String name) {

    JavaMethod method = new JavaMethod(getDeclaringType());
    method.setName(name);
    JavaReturn returns = new JavaReturn(method);
    returns.setType(getContext().getVoidType());
    method.setReturns(returns);
    this.methods.add(method);
    return method;
  }

  @Override
  public JavaMethods copy(CodeType newDeclaringType) {

    return new JavaMethods(this, (JavaType) newDeclaringType);
  }

}
