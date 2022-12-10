/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.block;

import io.github.mmm.code.api.copy.CodeNodeItemCopyable;

/**
 * {@link CodeBlock} for an {@code if} block. <br>
 * Syntax: <pre>
 * <code>if («{@link #getCondition() condition}») {
 *   «{@link #getStatements() statements}»
 * } {@link #getElse() else} if («{@link #getCondition() condition}») {
 *   «{@link #getStatements() statements}»
 * } {@link #getElse() else} {
 *   «{@link #getStatements() statements}»
 * }</code> </pre>
 *
 * @author hohwille
 * @since 1.0.0
 */
public interface CodeBlockIf extends CodeBlockWithCondition, CodeNodeItemCopyable<CodeBlock, CodeBlockIf> {

  /**
   * @return the next {@code else} ({@code if}) block or {@code null} for none (if the end has been reached).
   */
  CodeBlockIf getElse();

  @Override
  CodeBlockIf copy();
}
