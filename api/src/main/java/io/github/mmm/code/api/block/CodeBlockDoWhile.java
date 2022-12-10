/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.block;

import io.github.mmm.code.api.copy.CodeNodeItemCopyable;

/**
 * {@link CodeBlock} for a {@code do while} statement.<br>
 * <pre>
 * <code>do {
 *   {@link #getStatements() ...}
 * } while ({@link #getCondition() condition});</code> </pre>
 *
 * @author hohwille
 * @since 1.0.0
 */
public interface CodeBlockDoWhile extends CodeBlockWithCondition, CodeNodeItemCopyable<CodeBlock, CodeBlockDoWhile> {

  @Override
  CodeBlockDoWhile copy();
}
