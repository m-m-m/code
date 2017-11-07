/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.member;

import java.lang.reflect.Constructor;

import net.sf.mmm.code.api.member.CodeConstructors;
import net.sf.mmm.code.api.member.CodeMethods;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.base.type.BaseType;

/**
 * Base implementation of {@link CodeMethods}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseConstructors extends BaseOperations<BaseConstructor>
    implements CodeConstructors<BaseConstructor>, CodeNodeItemWithGenericParent<BaseType, BaseConstructors> {

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public BaseConstructors(BaseType parent) {

    super(parent);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseConstructors} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public BaseConstructors(BaseConstructors template, BaseType parent) {

    super(template, parent);
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
    Class<?> reflectiveObject = getParent().getReflectiveObject();
    if (reflectiveObject != null) {
      for (Constructor<?> constructor : reflectiveObject.getDeclaredConstructors()) {
        BaseConstructor javaConstructor = new BaseConstructor(this, constructor);
        addInternal(javaConstructor);
      }
    }
  }

  @Override
  public BaseConstructor get(BaseConstructor constructor) {

    for (BaseConstructor myConstructor : getDeclared()) {
      if (myConstructor.getParameters().isInvokable(constructor.getParameters())) {
        return constructor;
      }
    }
    return null;
  }

  @Override
  public BaseConstructor get(CodeGenericType... parameterTypes) {

    for (BaseConstructor constructor : getDeclared()) {
      if (constructor.getParameters().isInvokable(parameterTypes)) {
        return constructor;
      }
    }
    return null;
  }

  @Override
  public BaseConstructor add() {

    BaseConstructor constructor = new BaseConstructor(this, null);
    add(constructor);
    return constructor;
  }

  @Override
  public BaseConstructors getSourceCodeObject() {

    BaseType sourceType = getParent().getSourceCodeObject();
    if (sourceType == null) {
      return null;
    }
    return sourceType.getConstructors();
  }

  @Override
  public BaseConstructors copy() {

    return copy(getParent());
  }

  @Override
  public BaseConstructors copy(BaseType newParent) {

    return new BaseConstructors(this, newParent);
  }

}