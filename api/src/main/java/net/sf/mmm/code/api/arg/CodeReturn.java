/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.arg;

import java.lang.reflect.AnnotatedType;

import net.sf.mmm.code.api.member.CodeMethod;
import net.sf.mmm.code.api.member.CodeOperation;
import net.sf.mmm.code.api.merge.CodeSimpleMergeableItem;

/**
 * {@link CodeOperationArg} for the returned result of a {@link CodeMethod}.
 *
 * @see CodeMethod#getReturns()
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeReturn extends CodeOperationArg, CodeSimpleMergeableItem<CodeReturn> {

  @Override
  CodeOperation getParent();

  @Override
  AnnotatedType getReflectiveObject();

  @Override
  CodeReturn copy();

}
