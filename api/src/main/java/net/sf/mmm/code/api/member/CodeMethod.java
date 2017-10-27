/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.member;

import java.lang.reflect.Method;

import net.sf.mmm.code.api.arg.CodeReturn;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * {@link CodeOperation} that represents a method of a {@link net.sf.mmm.code.api.type.CodeType}.
 *
 * @see net.sf.mmm.code.api.type.CodeType#getMethods()
 * @see CodeMethods#getDeclared()
 * @see java.lang.reflect.Method
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeMethod extends CodeOperation {

  @Override
  CodeMethods<?> getParent();

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

  /**
   * <b>Attention:</b><br>
   * This method is expensive as it has to traverse all methods of the entire type hierarchy recursively.
   *
   * @return the {@link CodeMethod} inherited from the closest
   *         {@link net.sf.mmm.code.api.type.CodeType#getSuperTypes() super type} that is {@link Override
   *         overridden} by this method or {@code null} if this method does not override any method. Here
   *         closest means that {@link net.sf.mmm.code.api.type.CodeSuperTypes#getSuperClass() class
   *         hierarchy} is searched first, then the interface hierarchy in left recursive order.
   */
  CodeMethod getParentMethod();

  @Override
  Method getReflectiveObject();

  @Override
  CodeMethod copy();

}
