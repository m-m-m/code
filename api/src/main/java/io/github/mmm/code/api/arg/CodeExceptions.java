/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.arg;

import io.github.mmm.code.api.copy.CodeNodeItemCopyable;
import io.github.mmm.code.api.member.CodeOperation;
import io.github.mmm.code.api.merge.CodeSimpleMergeableItem;
import io.github.mmm.code.api.node.CodeNodeItemContainer;
import io.github.mmm.code.api.type.CodeGenericType;
import io.github.mmm.code.api.type.CodeType;

/**
 * {@link CodeNodeItemContainer} containing the {@link CodeException}s of a {@link CodeOperation}.
 *
 * @see java.lang.reflect.Executable#getAnnotatedExceptionTypes()
 * @see CodeException
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeExceptions
    extends CodeOperationArgs<CodeException>, CodeSimpleMergeableItem<CodeExceptions>, CodeNodeItemCopyable<CodeOperation, CodeExceptions> {

  /**
   * @param type the {@link CodeType} reflection the {@link CodeType#isException() exception}.
   * @return the {@link CodeException} that has the given type or {@code null} if not found.
   */
  CodeException get(CodeGenericType type);

  /**
   * @param type the {@link CodeType} reflection the {@link CodeType#isException() exception}.
   * @return the new {@link CodeException} that has been added.
   */
  CodeException add(CodeGenericType type);

  /**
   * @param type the {@link CodeType} reflection the {@link CodeType#isException() exception}.
   * @return the {@link #get(CodeGenericType) matching} {@link CodeException} or the {@link #add(CodeGenericType) new}
   *         one that was added.
   */
  default CodeException getOrCreate(CodeGenericType type) {

    CodeException exception = get(type);
    if (exception == null) {
      exception = add(type);
    }
    return exception;
  }

  @Override
  CodeExceptions copy();

}
