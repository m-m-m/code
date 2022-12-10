/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.expression;

import java.util.List;

import io.github.mmm.code.api.member.CodeOperation;
import io.github.mmm.code.api.type.CodeGenericType;

/**
 * {@link CodeExpression} representing the invocation of a {@link CodeOperation}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeOperationInvocation extends CodeExpression {

  /**
   * @return the {@link CodeOperation} to invoke.
   */
  CodeOperation getMember();

  /**
   * @return the {@link List} of {@link CodeExpression expressions} applied as arguments to the
   *         {@link #getMember() operation}.
   */
  List<? extends CodeExpression> getArguments();

  /**
   * @return the {@link List} of type parameters. Will be {@code null} if not a generic invocation. Will be
   *         the empty {@link List} for the diamond operator. Otherwise the list contains the actual type
   *         parameters.
   */
  List<? extends CodeGenericType> getTypeParameters();

}
