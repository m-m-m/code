/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.member;

import java.lang.reflect.Executable;

import io.github.mmm.code.api.arg.CodeParameter;
import io.github.mmm.code.api.block.CodeBlockBody;
import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.copy.CodeCopyType;
import io.github.mmm.code.api.element.CodeElement;
import io.github.mmm.code.api.expression.CodeVariable;
import io.github.mmm.code.api.member.CodeField;
import io.github.mmm.code.api.member.CodeOperation;
import io.github.mmm.code.api.merge.CodeMergeStrategy;
import io.github.mmm.code.api.modifier.CodeModifiers;
import io.github.mmm.code.base.arg.BaseExceptions;
import io.github.mmm.code.base.arg.BaseParameters;
import io.github.mmm.code.base.block.BaseBlockBody;
import io.github.mmm.code.base.type.BaseTypeVariables;

/**
 * Base implementation of {@link CodeOperation}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BaseOperation extends BaseMember implements CodeOperation {

  private final BaseTypeVariables typeVariables;

  private BaseParameters parameters;

  private BaseExceptions exceptions;

  private CodeBlockBody body;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param name the {@link #getName() name}.
   */
  public BaseOperation(BaseOperations<?> parent, String name) {

    super(parent, CodeModifiers.MODIFIERS_PUBLIC, name);
    this.typeVariables = new BaseTypeVariables(this);
    this.parameters = new BaseParameters(this);
    this.exceptions = new BaseExceptions(this);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param name the {@link #getName() name}.
   * @param typeVariables the {@link #getTypeParameters() type variables}.
   */
  protected BaseOperation(BaseOperations<?> parent, String name, BaseTypeVariables typeVariables) {

    super(parent, CodeModifiers.MODIFIERS_PUBLIC, name);
    typeVariables.setParent(this);
    typeVariables.setImmutable();
    this.typeVariables = typeVariables;
    this.parameters = new BaseParameters(this);
    this.exceptions = new BaseExceptions(this);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseOperation} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseOperation(BaseOperation template, CodeCopyMapper mapper) {

    super(template, mapper);
    this.typeVariables = template.typeVariables.copy(mapper);
    this.parameters = template.parameters.copy(mapper);
    this.exceptions = template.exceptions.copy(mapper);
    this.body = mapper.map(template.body, CodeCopyType.CHILD);
  }

  @Override
  public abstract BaseOperations<?> getParent();

  @Override
  public CodeVariable getVariable(String name) {

    CodeParameter parameter = this.parameters.get(name);
    if (parameter != null) {
      return parameter;
    }
    CodeField javaField = getParent().getParent().getFields().get(name);
    return javaField;
  }

  @Override
  protected void doSetImmutable() {

    super.doSetImmutable();
    this.typeVariables.setImmutableIfNotSystemImmutable();
    this.parameters.setImmutableIfNotSystemImmutable();
    this.exceptions.setImmutableIfNotSystemImmutable();
  }

  @Override
  public BaseTypeVariables getTypeParameters() {

    return this.typeVariables;
  }

  @Override
  public BaseParameters getParameters() {

    return this.parameters;
  }

  @Override
  public BaseExceptions getExceptions() {

    return this.exceptions;
  }

  @Override
  public CodeBlockBody getBody() {

    if (this.body == null) {
      this.body = new BaseBlockBody(this);
    }
    return this.body;
  }

  @Override
  public void setBody(CodeBlockBody body) {

    verifyMutalbe();
    if (body.getParent() != this) {
      throw new IllegalArgumentException();
    }
    this.body = body;
  }

  @Override
  public abstract Executable getReflectiveObject();

  @Override
  public abstract CodeOperation getSourceCodeObject();

  /**
   * @see #doMerge(CodeElement, CodeMergeStrategy)
   * @param other the {@link CodeOperation} to merge.
   * @param strategy the {@link CodeMergeStrategy}.
   */
  protected void doMerge(CodeOperation other, CodeMergeStrategy strategy) {

    if (strategy == CodeMergeStrategy.KEEP) {
      return;
    }
    super.doMerge(other, strategy);
    getParameters().merge(other.getParameters(), strategy);
    getExceptions().merge(other.getExceptions(), strategy);
    if (strategy != CodeMergeStrategy.MERGE_KEEP_BODY) {
      BaseBlockBody otherBody = (BaseBlockBody) other.getBody();
      this.body = doCopyNode(otherBody, this);
    }
  }

  @Override
  public abstract BaseOperation copy();

  @Override
  public boolean canHaveBody() {

    return true;
  }

}
