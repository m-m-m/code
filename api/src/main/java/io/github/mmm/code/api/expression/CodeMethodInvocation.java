/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.expression;

import io.github.mmm.code.api.member.CodeConstructor;
import io.github.mmm.code.api.member.CodeMethod;

/**
 * {@link CodeOperationInvocation} representing the invocation of a {@link CodeConstructor}.<br>
 * Syntax: <pre>
 * [(«{@link #getExpression() expression}»|«{@link #getType() type}»).][&lt;«{@link #getTypeParameters() typeParam-1}», ..., «{@link #getTypeParameters() typeParam-N}»&gt; ]«{@link #getMember() method}»(«{@link #getArguments() arg-1}»,...,«{@link #getArguments() arg-N}»)
 * </pre> Example: <pre>
 * this.format(format, value)
 * </pre>
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeMethodInvocation extends CodeMemberReference, CodeOperationInvocation {

  @Override
  CodeMethod getMember();

}
