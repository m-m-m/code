/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.arg;

import java.util.List;

import net.sf.mmm.code.api.member.CodeOperation;
import net.sf.mmm.code.api.merge.CodeSimpleMergeableItem;
import net.sf.mmm.code.api.node.CodeNodeItemContainer;
import net.sf.mmm.code.api.node.CodeNodeItemContainerFlatWithName;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeType;

/**
 * {@link CodeNodeItemContainer} containing the {@link CodeParameter}s of a {@link CodeOperation}.
 *
 * @see java.lang.reflect.Executable#getParameters()
 * @see CodeParameter
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <P> the type of the contained {@link CodeParameter}s.
 * @since 1.0.0
 */
public abstract interface CodeParameters<P extends CodeParameter>
    extends CodeOperationArgs<P>, CodeNodeItemContainerFlatWithName<P>, CodeSimpleMergeableItem<CodeParameters<?>> {

  /**
   * @param parameterTypes the {@link CodeType}s to use as parameters.
   * @return {@code true} if this operation can be invoked with parameters of the given {@link CodeType}s,
   *         {@code false} otherwise.
   */
  default boolean isInvokable(CodeGenericType... parameterTypes) {

    List<? extends CodeParameter> parameters = getDeclared();
    if (parameters.size() != parameterTypes.length) {
      return false;
    }
    for (int i = 0; i < parameterTypes.length; i++) {
      CodeParameter parameter = parameters.get(i);
      CodeType parameterType = parameter.getType().asType();
      if (!parameterType.equals(parameterTypes[i].asType())) {
        return false;
      }
    }
    return true;
  }

  /**
   * @param parameters the {@link CodeParameters} representing the expected signature to check.
   * @return {@code true} if this operation can be invoked with parameters of the given {@link CodeType}s,
   *         {@code false} otherwise.
   */
  default boolean isInvokable(CodeParameters<P> parameters) {

    List<? extends CodeParameter> myParams = getDeclared();
    List<? extends CodeParameter> otherParams = parameters.getDeclared();
    int size = myParams.size();
    if (size != otherParams.size()) {
      return false;
    }
    for (int i = 0; i < size; i++) {
      CodeType myType = myParams.get(i).getType().asType();
      CodeType otherType = otherParams.get(i).getType().asType();
      if (!myType.equals(otherType)) {
        return false;
      }
    }
    return true;
  }

  @Override
  CodeParameters<P> copy();

}
