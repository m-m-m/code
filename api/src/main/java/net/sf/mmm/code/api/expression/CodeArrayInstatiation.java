/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.expression;

import java.util.List;

/**
 * {@link CodeExpression} for an array instatiation.<br>
 * Syntax example: <pre>
 * {&lt;«{@link #getValues() value-1}», ..., «{@link #getValues() value-N}»}
 * </pre>
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeArrayInstatiation extends CodeExpression {

  /**
   * @return the {@link List} of {@link CodeExpression}s with the initial values of the array. May be
   *         {@link List#isEmpty() empty} but will never be {@code null}.
   */
  List<CodeExpression> getValues();

}
