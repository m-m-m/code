/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.statement;

/**
 * {@link CodeBlock} for an {@code if} block. <br>
 * <pre>
 * <code>if ({@link #getCondition() condition}) {
 *   {@link #getStatements() ...}
 * } {@link #getElse() else} if ({@link #getCondition() condition}) {
 *   {@link #getStatements() ...}
 * } {@link #getElse() else} {
 *   {@link #getStatements() ...}
 * }</code> </pre>
 *
 * @author hohwille
 * @since 1.0.0
 */
public interface CodeIfBlock extends CodeBlockWithCondition {

  /**
   * @return the next {@code else} ({@code if}) block or {@code null} for none (if the end has been reached).
   */
  CodeIfBlock getElse();

}
