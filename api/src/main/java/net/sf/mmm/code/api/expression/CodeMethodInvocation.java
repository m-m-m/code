/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.expression;

import net.sf.mmm.code.api.member.CodeConstructor;
import net.sf.mmm.code.api.member.CodeMethod;
import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeType;

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

  /**
   * @return the optional {@link CodeGenericType} (typically {@link CodeType} but may be
   *         {@link CodeType#isQualified() qualified}) on which the method shall be invoked statically. Should
   *         be {@code null} if {@link #getExpression() expression} is not {@code null} or if
   *         {@link #getMember() method} is not {@link CodeModifiers#isStatic() static}.
   */
  CodeGenericType getType();

  @Override
  CodeMethod getMember();

}
