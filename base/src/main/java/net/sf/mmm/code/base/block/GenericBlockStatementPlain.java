/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.block;

import java.util.List;

import net.sf.mmm.code.api.block.CodeBlock;
import net.sf.mmm.code.api.block.CodeBlockStatement;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.statement.CodeStatement;

/**
 * Generic implementation of {@link CodeBlockStatement}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class GenericBlockStatementPlain extends GenericBlockStatement implements CodeNodeItemWithGenericParent<CodeBlock, GenericBlockStatementPlain> {

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param statements the {@link #getStatements() statements}.
   */
  public GenericBlockStatementPlain(CodeBlock parent, CodeStatement... statements) {

    super(parent, statements);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param statements the {@link #getStatements() statements}.
   */
  public GenericBlockStatementPlain(CodeBlock parent, List<CodeStatement> statements) {

    super(parent, statements);
  }

  /**
   * The copy-constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param template the {@link GenericBlockStatementPlain} to copy.
   */
  public GenericBlockStatementPlain(GenericBlockStatementPlain template, CodeBlock parent) {

    super(template, parent);
  }

  @Override
  public GenericBlockStatementPlain copy() {

    return copy(getParent());
  }

  @Override
  public GenericBlockStatementPlain copy(CodeBlock newParent) {

    return new GenericBlockStatementPlain(this, newParent);
  }

}
