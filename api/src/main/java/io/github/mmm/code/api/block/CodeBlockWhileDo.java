/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.block;

import io.github.mmm.code.api.copy.CodeNodeItemCopyable;

/**
 * {@link CodeBlock} for a {@code while do} statement.<br>
 * Syntax: <pre>
 * <code>while («{@link #getCondition() condition}») {
 *   «{@link #getStatements() statements}»
 * }</code> </pre>
 *
 * @author hohwille
 * @since 1.0.0
 */
public interface CodeBlockWhileDo extends CodeBlockWithCondition, CodeNodeItemCopyable<CodeBlock, CodeBlockWhileDo> {

  @Override
  CodeBlockWhileDo copy();
}
