/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.block;

import java.util.List;

import io.github.mmm.code.api.expression.CodeVariable;
import io.github.mmm.code.api.item.CodeItemWithVariables;
import io.github.mmm.code.api.node.CodeNodeItem;
import io.github.mmm.code.api.statement.CodeLocalVariable;
import io.github.mmm.code.api.statement.CodeStatement;

/**
 * {@link CodeStatement} for a block that groups multiple {@link #getStatements() statements}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeBlock extends CodeNodeItem, CodeItemWithVariables {

  @Override
  default CodeVariable getVariable(String name) {

    return getVariable(name, Integer.MAX_VALUE);
  }

  /**
   * @param name the {@link CodeVariable#getName() name} of the requested {@link CodeVariable}.
   * @param statementMaxIndex the maximum index in the {@link #getStatements() statements} where to resolve the
   *        {@link CodeVariable}. Therefore a {@link CodeLocalVariable} defined by a {@link #getStatements() statement}
   *        after the given index will not be considered.
   * @return the {@link CodeVariable} with the given {@link CodeVariable#getName() name} or {@code null} if not found.
   */
  CodeVariable getVariable(String name, int statementMaxIndex);

  /**
   * @return the {@link List} of {@link CodeStatement} contained inside the block (typically within curly braces). May
   *         be {@link List#isEmpty() empty} but never {@code null}.
   */
  List<CodeStatement> getStatements();

  /**
   * @return {@code true} if empty, {@code false} otherwise.
   */
  default boolean isEmpty() {

    return getStatements().isEmpty();
  }

  /**
   * @param statements the {@link CodeStatement}s to add.
   * @throws io.github.mmm.base.exception.ReadOnlyException if {@link #isImmutable() immutable}.
   */
  void add(CodeStatement... statements);

  /**
   * @param statements the plain textual statements to add as {@link CodeStatement}s.
   * @throws io.github.mmm.base.exception.ReadOnlyException if {@link #isImmutable() immutable}.
   */
  void addText(String... statements);

  @Override
  CodeBlock copy();

}
