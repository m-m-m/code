/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.block;

import java.util.List;

import net.sf.mmm.code.api.block.CodeBlock;
import net.sf.mmm.code.api.block.CodeBlockStatement;
import net.sf.mmm.code.api.block.CodeBlockWithCondition;
import net.sf.mmm.code.api.expression.CodeCondition;
import net.sf.mmm.code.api.statement.CodeStatement;

/**
 * Generic implementation of {@link CodeBlockStatement}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class GenericBlockStatementWithCondition extends GenericBlockStatement implements CodeBlockWithCondition {

  private CodeCondition condition;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param condition the {@link #getCondition() condition}.
   * @param statements the {@link #getStatements() statements}.
   */
  public GenericBlockStatementWithCondition(CodeBlock parent, CodeCondition condition, CodeStatement... statements) {

    super(parent, statements);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param condition the {@link #getCondition() condition}.
   * @param statements the {@link #getStatements() statements}.
   */
  public GenericBlockStatementWithCondition(CodeBlock parent, CodeCondition condition, List<CodeStatement> statements) {

    super(parent, statements);
  }

  /**
   * The copy-constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param template the {@link GenericBlockStatementWithCondition} to copy.
   */
  public GenericBlockStatementWithCondition(GenericBlockStatementWithCondition template, CodeBlock parent) {

    super(template, parent);
  }

  @Override
  public CodeCondition getCondition() {

    return this.condition;
  }

}
