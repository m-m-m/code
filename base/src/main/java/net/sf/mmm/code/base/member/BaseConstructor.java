/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.member;

import java.lang.reflect.Constructor;

import net.sf.mmm.code.api.copy.CodeCopyMapper;
import net.sf.mmm.code.api.copy.CodeCopyMapperNone;
import net.sf.mmm.code.api.member.CodeConstructor;
import net.sf.mmm.code.api.member.CodeConstructors;
import net.sf.mmm.code.api.merge.CodeMergeStrategy;
import net.sf.mmm.code.api.merge.CodeMergeStrategyDecider;
import net.sf.mmm.code.base.type.BaseTypeVariables;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * Base implementation of {@link CodeConstructor}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseConstructor extends BaseOperation implements CodeConstructor {

  private static final String CONSTRUCTOR_NAME = "UnnamedConstructor";

  private final BaseConstructors parent;

  private final Constructor<?> reflectiveObject;

  private CodeConstructor sourceCodeObject;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public BaseConstructor(BaseConstructors parent) {

    this(parent, (Constructor<?>) null);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May be {@code null}.
   */
  public BaseConstructor(BaseConstructors parent, Constructor<?> reflectiveObject) {

    super(parent, CONSTRUCTOR_NAME);
    this.parent = parent;
    this.reflectiveObject = reflectiveObject;
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param typeVariables the {@link #getTypeParameters() type variables}.
   */
  public BaseConstructor(BaseConstructors parent, BaseTypeVariables typeVariables) {

    super(parent, CONSTRUCTOR_NAME, typeVariables);
    this.parent = parent;
    this.reflectiveObject = null;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseConstructor} to copy.
   * @param parent the {@link #getParent() parent}.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseConstructor(BaseConstructor template, BaseConstructors parent, CodeCopyMapper mapper) {

    super(template, mapper);
    this.parent = parent;
    this.reflectiveObject = null;
  }

  @Override
  public BaseConstructors getParent() {

    return this.parent;
  }

  @Override
  public Constructor<?> getReflectiveObject() {

    return this.reflectiveObject;
  }

  @Override
  public CodeConstructor getSourceCodeObject() {

    if (this.sourceCodeObject != null) {
      return this.sourceCodeObject;
    }
    if (isInitialized()) {
      return null;
    }
    CodeConstructors sourceConstructors = this.parent.getSourceCodeObject();
    if (sourceConstructors != null) {
      this.sourceCodeObject = sourceConstructors.get(this);
    }
    return this.sourceCodeObject;
  }

  @Override
  public String getName() {

    return getDeclaringType().getSimpleName();
  }

  @Override
  public void setName(String name) {

    throw new ReadOnlyException(getClass().getSimpleName(), "name");
  }

  @Override
  public CodeConstructor merge(CodeConstructor other, CodeMergeStrategyDecider decider, CodeMergeStrategy parentStrategy) {

    CodeMergeStrategy strategy = decider.decide(this, other, parentStrategy);
    doMerge(other, strategy);
    return this;
  }

  @Override
  public BaseConstructor copy() {

    return copy(this.parent);
  }

  @Override
  public BaseConstructor copy(CodeConstructors newParent) {

    return copy(newParent, CodeCopyMapperNone.INSTANCE);
  }

  @Override
  public BaseConstructor copy(CodeConstructors newParent, CodeCopyMapper mapper) {

    return new BaseConstructor(this, (BaseConstructors) newParent, mapper);
  }

}
