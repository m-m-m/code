/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.block;

import io.github.mmm.code.api.expression.CodeCondition;

/**
 * {@link CodeBlock} with a {@link #getCondition() condition}.
 *
 * @author hohwille
 * @since 1.0.0
 */
public interface CodeBlockWithCondition extends CodeBlockStatement {

  /**
   * @return the {@link CodeCondition}. May be {@code null} for {@link CodeBlockIf} to represent a pure
   *         {@code else} block.
   */
  CodeCondition getCondition();

}
