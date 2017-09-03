/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.member;

import java.util.List;

import net.sf.mmm.code.api.arg.CodeException;
import net.sf.mmm.code.api.arg.CodeParameter;
import net.sf.mmm.code.api.statement.CodeBody;
import net.sf.mmm.code.api.type.CodeTypeVariables;

/**
 * Abstract interface for an invokable operation such as a {@link CodeMethod} or {@link CodeConstructor}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeOperation extends CodeMember {

  /**
   * @return the {@link List} with the {@link CodeParameter}s of this method. May be {@link List#isEmpty()
   *         empty} but never <code>null</code>.
   */
  List<? extends CodeParameter> getParameters();

  /**
   * @return the {@link List} with the {@link CodeException}s of this method. May be {@link List#isEmpty()
   *         empty} but never <code>null</code>.
   */
  List<? extends CodeException> getExceptions();

  /**
   * @return the {@link CodeBody} of this method. Will be {@code null} if not present (in case of an
   *         {@link net.sf.mmm.code.api.modifier.CodeModifiers#KEY_ABSTRACT abstract} {@link CodeMethod}).
   */
  CodeBody getBody();

  /**
   * @return the {@link CodeTypeVariables} containing the {@link net.sf.mmm.code.api.type.CodeTypeVariable}s.
   */
  CodeTypeVariables getTypeVariables();

}
