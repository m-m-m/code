/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.block;

import java.util.ArrayList;
import java.util.List;

import io.github.mmm.code.api.block.CodeBlockBody;
import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.copy.CodeCopyType;
import io.github.mmm.code.api.expression.CodeVariable;
import io.github.mmm.code.api.node.CodeFunction;
import io.github.mmm.code.api.statement.CodeStatement;

/**
 * Base implementation of {@link CodeBlockBody}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseBlockBody extends BaseBlock implements CodeBlockBody {

  private CodeFunction parent;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public BaseBlockBody(CodeFunction parent) {

    this(parent, new ArrayList<>());
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param statements the {@link #getStatements() statements}.
   */
  public BaseBlockBody(CodeFunction parent, CodeStatement... statements) {

    super(statements);
    this.parent = parent;
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param statements the {@link #getStatements() statements}.
   */
  public BaseBlockBody(CodeFunction parent, List<CodeStatement> statements) {

    super(statements);
    this.parent = parent;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseBlock} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseBlockBody(BaseBlockBody template, CodeCopyMapper mapper) {

    super(template, mapper);
    this.parent = mapper.map(template.parent, CodeCopyType.PARENT);
  }

  @Override
  public CodeFunction getParent() {

    return this.parent;
  }

  /**
   * @param parent the new value of {@link #getParent()}.
   */
  public void setParent(CodeFunction parent) {

    verifyMutalbe();
    this.parent = parent;
  }

  @Override
  protected CodeVariable getVariableFromParent(String name) {

    return this.parent.getVariable(name);
  }

  @Override
  public BaseBlockBody copy() {

    return copy(getDefaultCopyMapper());
  }

  @Override
  public BaseBlockBody copy(CodeCopyMapper mapper) {

    return new BaseBlockBody(this, mapper);
  }

}
