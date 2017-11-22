/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.arg;

import net.sf.mmm.code.api.member.CodeOperation;
import net.sf.mmm.code.api.merge.CodeSimpleMergeableItem;
import net.sf.mmm.code.api.node.CodeNodeItemContainer;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeType;

/**
 * {@link CodeNodeItemContainer} containing the {@link CodeException}s of a {@link CodeOperation}.
 *
 * @see java.lang.reflect.Executable#getAnnotatedExceptionTypes()
 * @see CodeException
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <E> the type of the contained {@link CodeException}s.
 * @since 1.0.0
 */
public abstract interface CodeExceptions<E extends CodeException> extends CodeOperationArgs<E>, CodeSimpleMergeableItem<CodeExceptions<?>> {

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
   * @return the {@link #get(CodeGenericType) matching} {@link CodeException} or the
   *         {@link #add(CodeGenericType) new} one that was added.
   */
  default CodeException getOrCreate(CodeGenericType type) {

    CodeException exception = get(type);
    if (exception == null) {
      exception = add(type);
    }
    return exception;
  }

  @Override
  CodeExceptions<E> copy();

}
