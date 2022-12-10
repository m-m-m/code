/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.arg;

import java.lang.reflect.AnnotatedType;

import io.github.mmm.code.api.copy.CodeNodeItemCopyable;
import io.github.mmm.code.api.member.CodeMethod;
import io.github.mmm.code.api.merge.CodeSimpleMergeableItem;

/**
 * {@link CodeOperationArg} for the returned result of a {@link CodeMethod}.
 *
 * @see CodeMethod#getReturns()
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeReturn extends CodeOperationArg, CodeSimpleMergeableItem<CodeReturn>, CodeNodeItemCopyable<CodeMethod, CodeReturn> {

  @Override
  AnnotatedType getReflectiveObject();

  @Override
  CodeReturn copy();

}
