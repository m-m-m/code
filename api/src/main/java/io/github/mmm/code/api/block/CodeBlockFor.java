/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.block;

import io.github.mmm.code.api.copy.CodeNodeItemCopyable;
import io.github.mmm.code.api.expression.CodeForExpression;

/**
 * {@link CodeBlock} for an {@code for} block. <br>
 * Syntax: <pre>
 * <code>for («{@link #getExpression() expression}») {
 *   «{@link #getStatements() statements}»
 * }</code> </pre>
 *
 * @author hohwille
 * @since 1.0.0
 */
public interface CodeBlockFor extends CodeBlockStatement, CodeNodeItemCopyable<CodeBlock, CodeBlockFor> {

  /**
   * @return the {@link CodeForExpression}. May not be {@code null}.
   */
  CodeForExpression getExpression();

  @Override
  CodeBlockFor copy();
}
