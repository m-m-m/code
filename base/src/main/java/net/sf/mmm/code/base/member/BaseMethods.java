/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.member;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sf.mmm.code.api.copy.CodeCopyMapper;
import net.sf.mmm.code.api.member.CodeMethod;
import net.sf.mmm.code.api.member.CodeMethods;
import net.sf.mmm.code.api.merge.CodeMergeStrategy;
import net.sf.mmm.code.api.merge.CodeMergeStrategyDecider;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.base.type.BaseType;

/**
 * Base implementation of {@link CodeMethods}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseMethods extends BaseOperations<CodeMethod> implements CodeMethods {

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
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseMethods(BaseMethods template, CodeCopyMapper mapper) {

    super(template, mapper);
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
  public Iterable<? extends CodeMethod> getAll() {

    List<CodeMethod> list = new ArrayList<>(getDeclared());
    collectMethods(list); // TODO implement with iterator instead and avoid returning parent methods
    return list;
  }

  private void collectMethods(List<CodeMethod> list) {

    for (CodeGenericType superType : getDeclaringType().getSuperTypes().getDeclared()) {
      BaseMethods javaMethods = (BaseMethods) superType.asType().getMethods();
      list.addAll(javaMethods.getDeclared());
      javaMethods.collectMethods(list);
    }
  }

  @Override
  public CodeMethod get(CodeMethod method) {

    String name = method.getName();
    for (CodeMethod myMethod : getDeclared()) {
      if (myMethod.getName().equals(name)) {
        if (myMethod.getParameters().isInvokable(method.getParameters())) {
          return myMethod;
        }
      }
    }
    for (CodeGenericType superType : getDeclaringType().getSuperTypes().getDeclared()) {
      CodeMethod myMethod = superType.asType().getMethods().get(method);
      if (myMethod != null) {
        return myMethod;
      }
    }
    return null;
  }

  @Override
  public CodeMethod getDeclared(String name, CodeGenericType... parameterTypes) {

    for (CodeMethod method : getDeclared()) {
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
  public CodeMethods getSourceCodeObject() {

    CodeType sourceType = getParent().getSourceCodeObject();
    if (sourceType == null) {
      return null;
    }
    return sourceType.getMethods();
  }

  @Override
  public CodeMethods merge(CodeMethods other, CodeMergeStrategyDecider decider, CodeMergeStrategy parentStrategy) {

    if (parentStrategy == CodeMergeStrategy.KEEP) {
      return this;
    }
    if (parentStrategy == CodeMergeStrategy.OVERRIDE) {
      clear();
      for (CodeMethod otherMethod : other.getDeclared()) {
        add(doCopyNode(otherMethod, this));
      }
    } else {
      for (CodeMethod otherMethod : other.getDeclared()) {
        CodeMethod myMethod = get(otherMethod);
        if (myMethod == null) {
          add(doCopyNode(otherMethod, this));
        } else {
          myMethod.merge(otherMethod, decider, parentStrategy);
        }
      }
    }
    return this;
  }

  @Override
  public BaseMethods copy() {

    return copy(getDefaultCopyMapper());
  }

  @Override
  public BaseMethods copy(CodeCopyMapper mapper) {

    return new BaseMethods(this, mapper);
  }

}
