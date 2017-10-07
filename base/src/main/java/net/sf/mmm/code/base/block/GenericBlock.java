/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.block;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sf.mmm.code.api.block.CodeBlock;
import net.sf.mmm.code.api.expression.CodeVariable;
import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.statement.CodeReturnStatement;
import net.sf.mmm.code.api.statement.CodeStatement;
import net.sf.mmm.code.api.syntax.CodeSyntax;
import net.sf.mmm.code.base.node.AbstractCodeNodeItem;

/**
 * Generic implementation of {@link CodeBlock}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class GenericBlock extends AbstractCodeNodeItem implements CodeBlock {

  private List<CodeStatement> statements;

  /**
   * The constructor.
   *
   * @param statements the {@link #getStatements() statements}.
   */
  public GenericBlock(CodeStatement... statements) {

    this(Arrays.asList(statements));
  }

  /**
   * The constructor.
   *
   * @param statements the {@link #getStatements() statements}.
   */
  public GenericBlock(List<CodeStatement> statements) {

    super();
    this.statements = Collections.unmodifiableList(statements);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link GenericBlock} to copy.
   */
  public GenericBlock(GenericBlock template) {

    super(template);
  }

  @Override
  public CodeVariable getVariable(String name) {

    return getVariable(name, Integer.MAX_VALUE);
  }

  @Override
  public CodeVariable getVariable(String name, int statementIndex) {

    int max = this.statements.size() - 1;
    if (statementIndex < max) {
      max = statementIndex;
    }
    for (int i = 0; i <= max; i++) {
      CodeStatement statement = this.statements.get(i);
      CodeVariable variable = statement.getVariable(name);
      if (variable != null) {
        return variable;
      }
    }
    return getVariableFromParent(name);
  }

  /**
   * @param name the {@link CodeVariable#getName() name} of the requested {@link CodeVariable}.
   * @return the {@link CodeVariable} declared by the {@link #getParent() parent node} with the given
   *         {@link CodeVariable#getName() name} or {@code null} if not found.
   */
  protected abstract CodeVariable getVariableFromParent(String name);

  @Override
  protected void doSetImmutable() {

    super.doSetImmutable();
    this.statements = makeImmutable(this.statements);
  }

  @Override
  public List<CodeStatement> getStatements() {

    return this.statements;
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeSyntax syntax) throws IOException {

    if (this.statements.size() == 1) {
      CodeStatement statement = this.statements.get(0);
      if (statement instanceof CodeReturnStatement) {
        if (((CodeReturnStatement) statement).isOmitReturn()) {
          statement.write(sink, newline, defaultIndent, currentIndent);
          return;
        }
      }
    }
    writePrefix(sink, newline, defaultIndent, currentIndent);
    if (this.statements.isEmpty()) {
      sink.append("{}");
    } else {
      sink.append('{');
      sink.append(newline);
      String newIndent = currentIndent;
      if (defaultIndent != null) {
        newIndent = currentIndent + defaultIndent;
      }
      for (CodeStatement statement : this.statements) {
        statement.write(sink, newline, defaultIndent, newIndent);
        sink.append(newline);
      }
      sink.append(currentIndent);
      sink.append('}');
    }
    writeSuffix(sink, newline, defaultIndent, currentIndent);
  }

  /**
   * Writes a prefix e.g. for loops.
   *
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the code from
   *        this {@link CodeItem}.
   * @param newline the newline {@link String}.
   * @param defaultIndent the {@link String} used for indentation (e.g. a number of spaces to insert per
   *        indent level).
   * @param currentIndent the current indent (number of spaces). Initially the empty string ({@code ""}).
   *        Before a recursion the {@code indent} will be appended.
   * @throws IOException if thrown by {@link Appendable}.
   */
  protected void writePrefix(Appendable sink, String newline, String defaultIndent, String currentIndent) throws IOException {

    // nothing by default...
  }

  /**
   * Writes a suffix e.g. for do-while loops.
   *
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the code from
   *        this {@link CodeItem}.
   * @param newline the newline {@link String}.
   * @param defaultIndent the {@link String} used for indentation (e.g. a number of spaces to insert per
   *        indent level).
   * @param currentIndent the current indent (number of spaces). Initially the empty string ({@code ""}).
   *        Before a recursion the {@code indent} will be appended.
   * @throws IOException if thrown by {@link Appendable}.
   */
  protected void writeSuffix(Appendable sink, String newline, String defaultIndent, String currentIndent) throws IOException {

    sink.append(newline);
  }

}
