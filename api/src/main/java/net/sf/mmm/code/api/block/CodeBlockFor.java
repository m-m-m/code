/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.block;

import net.sf.mmm.code.api.expression.CodeForExpression;

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
public interface CodeBlockFor extends CodeBlockStatement {

  /**
   * @return the {@link CodeForExpression}. May not be {@code null}.
   */
  CodeForExpression getExpression();

}
