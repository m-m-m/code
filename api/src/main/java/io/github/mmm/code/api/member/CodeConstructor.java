/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.member;

import java.lang.reflect.Constructor;

import io.github.mmm.base.exception.ReadOnlyException;
import io.github.mmm.code.api.copy.CodeNodeItemCopyable;
import io.github.mmm.code.api.merge.CodeAdvancedMergeableItem;
import io.github.mmm.code.api.type.CodeGenericType;
import io.github.mmm.code.api.type.CodeType;

/**
 * {@link CodeOperation} representing a constructor of a {@link CodeType}.
 *
 * @see CodeType#getConstructors()
 * @see CodeConstructors#getDeclared()
 * @see java.lang.reflect.Constructor
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeConstructor extends CodeOperation, CodeAdvancedMergeableItem<CodeConstructor>,
    CodeNodeItemCopyable<CodeConstructors, CodeConstructor> {

  @Override
  default CodeGenericType getType() {

    return getDeclaringType();
  }

  /**
   * @deprecated the {@link #getName() name} of a {@link CodeConstructor} has to be equal to the
   *             {@link CodeType#getSimpleName() simple name} of its {@link #getDeclaringType() declaring type}.
   */
  @Deprecated
  @Override
  default void setName(String name) {

    throw new ReadOnlyException(getClass().getSimpleName(), "name");
  }

  @Override
  Constructor<?> getReflectiveObject();

  @Override
  CodeConstructor copy();
}
