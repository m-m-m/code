/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.block;

import java.io.IOException;
import java.util.List;

import net.sf.mmm.code.api.block.CodeBlock;
import net.sf.mmm.code.api.block.CodeBlockDoWhile;
import net.sf.mmm.code.api.expression.CodeCondition;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.statement.CodeStatement;

/**
 * Generic implementation of {@link CodeBlockDoWhile}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class GenericBlockDoWhile extends GenericBlockStatementWithCondition
    implements CodeBlockDoWhile, CodeNodeItemWithGenericParent<CodeBlock, GenericBlockDoWhile> {

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param condition the {@link #getCondition() condition}.
   * @param statements the {@link #getStatements() statements}.
   */
  public GenericBlockDoWhile(CodeBlock parent, CodeCondition condition, CodeStatement... statements) {

    super(parent, condition, statements);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param condition the {@link #getCondition() condition}.
   * @param statements the {@link #getStatements() statements}.
   */
  public GenericBlockDoWhile(CodeBlock parent, CodeCondition condition, List<CodeStatement> statements) {

    super(parent, condition, statements);
  }

  /**
   * The copy-constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param template the {@link GenericBlockStatementWithCondition} to copy.
   */
  public GenericBlockDoWhile(GenericBlockStatementWithCondition template, CodeBlock parent) {

    super(template, parent);
  }

  @Override
  public GenericBlockDoWhile copy() {

    return copy(getParent());
  }

  @Override
  public GenericBlockDoWhile copy(CodeBlock newParent) {

    return new GenericBlockDoWhile(this, newParent);
  }

  @Override
  protected void writePrefix(Appendable sink, String newline, String defaultIndent, String currentIndent) throws IOException {

    sink.append("do ");
  }

  @Override
  protected void writeSuffix(Appendable sink, String newline, String defaultIndent, String currentIndent) throws IOException {

    sink.append(" while (");
    getCondition().write(sink, newline, defaultIndent, currentIndent);
    sink.append(");");
    super.writeSuffix(sink, newline, defaultIndent, currentIndent);
  }

}
