/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.member;

import java.lang.reflect.Constructor;

import net.sf.mmm.code.api.copy.CodeCopyMapper;
import net.sf.mmm.code.api.member.CodeConstructor;
import net.sf.mmm.code.api.member.CodeConstructors;
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
public class BaseConstructors extends BaseOperations<CodeConstructor> implements CodeConstructors {

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
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseConstructors(BaseConstructors template, CodeCopyMapper mapper) {

    super(template, mapper);
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
  public CodeConstructor get(CodeConstructor constructor) {

    for (CodeConstructor myConstructor : getDeclared()) {
      if (myConstructor.getParameters().isInvokable(constructor.getParameters())) {
        return constructor;
      }
    }
    return null;
  }

  @Override
  public CodeConstructor get(CodeGenericType... parameterTypes) {

    for (CodeConstructor constructor : getDeclared()) {
      if (constructor.getParameters().isInvokable(parameterTypes)) {
        return constructor;
      }
    }
    return null;
  }

  @Override
  public CodeConstructor add() {

    BaseConstructor constructor = new BaseConstructor(this);
    add(constructor);
    return constructor;
  }

  @Override
  public CodeConstructors getSourceCodeObject() {

    CodeType sourceType = getParent().getSourceCodeObject();
    if (sourceType == null) {
      return null;
    }
    return sourceType.getConstructors();
  }

  @Override
  public CodeConstructors merge(CodeConstructors o, CodeMergeStrategyDecider decider, CodeMergeStrategy parentStrategy) {

    if (parentStrategy == CodeMergeStrategy.KEEP) {
      return this;
    }
    BaseConstructors other = (BaseConstructors) o;
    if (parentStrategy == CodeMergeStrategy.OVERRIDE) {
      clear();
      for (CodeConstructor otherConstructor : other.getDeclared()) {
        add(doCopyNode(otherConstructor, this));
      }
    } else {
      for (CodeConstructor otherConstructor : other.getDeclared()) {
        CodeConstructor myConstructor = get(otherConstructor);
        if (myConstructor == null) {
          add(doCopyNode(otherConstructor, this));
        } else {
          myConstructor.merge(otherConstructor, decider, parentStrategy);
        }
      }
    }
    return this;
  }

  @Override
  public BaseConstructors copy() {

    return copy(getDefaultCopyMapper());
  }

  @Override
  public BaseConstructors copy(CodeCopyMapper mapper) {

    return new BaseConstructors(this, mapper);
  }

}
