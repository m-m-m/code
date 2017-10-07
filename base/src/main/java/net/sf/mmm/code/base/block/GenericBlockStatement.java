/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.block;

import java.util.List;

import net.sf.mmm.code.api.block.CodeBlock;
import net.sf.mmm.code.api.block.CodeBlockStatement;
import net.sf.mmm.code.api.expression.CodeVariable;
import net.sf.mmm.code.api.statement.CodeStatement;

/**
 * Generic implementation of {@link CodeBlockStatement}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class GenericBlockStatement extends GenericBlock implements CodeBlockStatement {

  private final CodeBlock parent;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param statements the {@link #getStatements() statements}.
   */
  public GenericBlockStatement(CodeBlock parent, CodeStatement... statements) {

    super(statements);
    this.parent = parent;
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param statements the {@link #getStatements() statements}.
   */
  public GenericBlockStatement(CodeBlock parent, List<CodeStatement> statements) {

    super(statements);
    this.parent = parent;
  }

  /**
   * The copy-constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param template the {@link GenericBlockStatement} to copy.
   */
  public GenericBlockStatement(GenericBlockStatement template, CodeBlock parent) {

    super(template);
    this.parent = parent;
  }

  @Override
  public CodeBlock getParent() {

    return this.parent;
  }

  @Override
  protected CodeVariable getVariableFromParent(String name) {

    int index = this.parent.getStatements().indexOf(this);
    return this.parent.getVariable(name, index);
  }

}
