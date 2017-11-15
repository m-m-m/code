/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.expression;

import net.sf.mmm.code.api.item.CodeItemWithQualifiedFlag;
import net.sf.mmm.code.api.member.CodeConstructor;

/**
 * {@link CodeOperationInvocation} representing the invocation of a {@link CodeConstructor}.<br>
 * Syntax example: <pre>
 * new «{@link #getMember() type}»[&lt;«{@link #getTypeParameters() typeParam-1}», ..., «{@link #getTypeParameters() typeParam-N}»&gt;](«{@link #getArguments() arg-1}»,...,«{@link #getArguments() arg-N}»)
 * </pre> Example: <pre>
 * new StringTokenizer("a,b,c", ",", false)
 * </pre>
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeConstructorInvocation extends CodeOperationInvocation, CodeItemWithQualifiedFlag {

  @Override
  CodeConstructor getMember();

}
