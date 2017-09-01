/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.member;

import java.util.List;

import net.sf.mmm.code.api.CodeGenericType;
import net.sf.mmm.code.api.CodeType;
import net.sf.mmm.code.api.arg.CodeReturn;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * Represents a {@link java.lang.reflect.Method} of a {@link CodeType}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeMethod extends CodeOperation {

  /**
   * @return the {@link List} of {@link CodeGenericType generic type} {@link CodeGenericType#getTypeVariable()
   *         variables} declared by this method. May be {@link List#isEmpty() empty} but is never
   *         {@code null}.
   * @see Class#getTypeParameters()
   */
  List<? extends CodeGenericType> getTypeParameters();

  /**
   * @param typeParameter the {@link CodeGenericType} to add as type parameter.
   * @throws ReadOnlyException if {@link #isImmutable() immutable}.
   */
  void addTypeParameter(CodeGenericType typeParameter);

  /**
   * @return the {@link CodeReturn} with the information about the returned result of this method. Will never
   *         be <code>null</code>. In case of a programming language that supports multiple return types a
   *         single {@link CodeReturn} will be returned that reflects a tuple of the actual returned types.
   */
  CodeReturn getReturns();

  /**
   * @param returns the new {@link #getReturns() returns}.
   * @throws ReadOnlyException if {@link #isImmutable() immutable}.
   */
  void setReturns(CodeReturn returns);

}
