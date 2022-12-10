/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.block;

import java.util.List;

import io.github.mmm.code.api.block.CodeBlockStatement;
import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.copy.CodeCopyType;
import io.github.mmm.code.api.expression.CodeVariable;
import io.github.mmm.code.api.statement.CodeStatement;

/**
 * Base implementation of {@link CodeBlockStatement}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BaseBlockStatement extends BaseBlock implements CodeBlockStatement {

  private BaseBlock parent;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param statements the {@link #getStatements() statements}.
   */
  public BaseBlockStatement(BaseBlock parent, CodeStatement... statements) {

    super(statements);
    this.parent = parent;
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param statements the {@link #getStatements() statements}.
   */
  public BaseBlockStatement(BaseBlock parent, List<CodeStatement> statements) {

    super(statements);
    this.parent = parent;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseBlockStatement} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseBlockStatement(BaseBlockStatement template, CodeCopyMapper mapper) {

    super(template, mapper);
    this.parent = mapper.map(template.parent, CodeCopyType.PARENT);
  }

  @Override
  public BaseBlock getParent() {

    return this.parent;
  }

  /**
   * @param parent the new value of {@link #getParent()}.
   */
  public void setParent(BaseBlock parent) {

    verifyMutalbe();
    this.parent = parent;
  }

  @Override
  protected CodeVariable getVariableFromParent(String name) {

    int index = this.parent.getStatements().indexOf(this);
    return this.parent.getVariable(name, index);
  }

}
