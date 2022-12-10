/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.arg;

import java.lang.reflect.Parameter;

import io.github.mmm.code.api.copy.CodeNodeItemCopyable;
import io.github.mmm.code.api.element.CodeElementWithName;
import io.github.mmm.code.api.expression.CodeVariable;
import io.github.mmm.code.api.member.CodeOperation;
import io.github.mmm.code.api.merge.CodeSimpleMergeableItem;

/**
 * {@link CodeOperationArg} for a parameter (argument) of a {@link CodeOperation}.
 *
 * @see CodeOperation#getParameters()
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeParameter extends CodeOperationArg, CodeElementWithName, CodeVariable,
    CodeSimpleMergeableItem<CodeParameter>, CodeNodeItemCopyable<CodeParameters, CodeParameter> {

  /**
   * @return {@code true} in case of a var-arg parameter (e.g. {{@code String...}), {@code false} otherwise. If a
   *         var-arg parameter this has to be the last parameter in the signature and the {@link #getType() type} has to
   *         be an array. For invocations this parameter can take any number of actual arguments of the
   *         {@link #getType() type}'s {@link io.github.mmm.code.api.type.CodeType#getComponentType() component-type}
   *         including none. These parameters will technically be converted to an array passed for this parameter.
   * @see java.lang.reflect.Parameter#isVarArgs()
   */
  boolean isVarArgs();

  /**
   * @param varArgs the new value for {@link #isVarArgs()}.
   * @throws io.github.mmm.base.exception.ReadOnlyException if {@link #isImmutable() immutable}.
   */
  void setVarArgs(boolean varArgs);

  @Override
  Parameter getReflectiveObject();

  @Override
  CodeParameter copy();

}
