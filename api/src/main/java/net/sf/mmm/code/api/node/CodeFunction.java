/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.node;

import net.sf.mmm.code.api.block.CodeBlockBody;
import net.sf.mmm.code.api.item.CodeItemWithVariables;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * {@link CodeNodeItem} representing a function such as a {@link net.sf.mmm.code.api.member.CodeMethod} or a
 * {@link net.sf.mmm.code.api.expression.CodeLambdaExpression}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeFunction extends CodeNodeItem, CodeItemWithVariables {

  /**
   * @return the {@link CodeBlockBody} of this function. May be empty (have no {@link CodeBlockBody#getStatements()
   *         statements}) but never {@code null}.
   */
  CodeBlockBody getBody();

  /**
   * @return {@code true} if this function can and should have a {@link #getBody() body}, {@code false} otherwise (e.g.
   *         if {@link net.sf.mmm.code.api.modifier.CodeModifiers#isAbstract() abstract}).
   */
  boolean canHaveBody();

  /**
   * @param body the new value of {@link #getBody()}.
   * @throws ReadOnlyException if {@link #isImmutable() immutable}.
   */
  void setBody(CodeBlockBody body);

}
