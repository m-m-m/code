/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.member;

import java.io.IOException;
import java.lang.reflect.Constructor;

import io.github.mmm.base.exception.ReadOnlyException;
import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.copy.CodeCopyType;
import io.github.mmm.code.api.language.CodeLanguage;
import io.github.mmm.code.api.member.CodeConstructor;
import io.github.mmm.code.api.member.CodeConstructors;
import io.github.mmm.code.api.merge.CodeMergeStrategy;
import io.github.mmm.code.api.merge.CodeMergeStrategyDecider;
import io.github.mmm.code.base.type.BaseTypeVariables;

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
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseConstructor(BaseConstructor template, CodeCopyMapper mapper) {

    super(template, mapper);
    this.parent = mapper.map(template.parent, CodeCopyType.PARENT);
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
  public CodeConstructor merge(CodeConstructor other, CodeMergeStrategyDecider decider,
      CodeMergeStrategy parentStrategy) {

    CodeMergeStrategy strategy = decider.decide(this, other, parentStrategy);
    doMerge(other, strategy);
    return this;
  }

  @Override
  public BaseConstructor copy() {

    return copy(getDefaultCopyMapper());
  }

  @Override
  public BaseConstructor copy(CodeCopyMapper mapper) {

    return new BaseConstructor(this, mapper);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent,
      CodeLanguage language) throws IOException {

    language.writeConstructor(this, sink, newline, defaultIndent, currentIndent);
  }

}
