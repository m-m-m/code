/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.member;

import java.lang.reflect.Executable;
import java.util.List;

import net.sf.mmm.code.api.arg.CodeException;
import net.sf.mmm.code.api.arg.CodeExceptions;
import net.sf.mmm.code.api.arg.CodeParameter;
import net.sf.mmm.code.api.arg.CodeParameters;
import net.sf.mmm.code.api.element.CodeElementWithTypeVariables;
import net.sf.mmm.code.api.node.CodeFunction;

/**
 * {@link CodeMember} representing an invokable operation such as a {@link CodeMethod} or
 * {@link CodeConstructor}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeOperation extends CodeMember, CodeElementWithTypeVariables, CodeFunction {

  @Override
  CodeOperations<?> getParent();

  /**
   * @return the {@link CodeParameters} containing the {@link CodeParameter}s of this operation. May be
   *         {@link List#isEmpty() empty} but never <code>null</code>.
   */
  CodeParameters getParameters();

  /**
   * @return the {@link CodeExceptions} containing the {@link CodeException}s of this operation. May be
   *         {@link List#isEmpty() empty} but never <code>null</code>.
   */
  CodeExceptions getExceptions();

  @Override
  Executable getReflectiveObject();

  @Override
  CodeOperation copy();

}
