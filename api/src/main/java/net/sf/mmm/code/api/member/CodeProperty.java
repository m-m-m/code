/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.member;

import net.sf.mmm.code.api.copy.CodeNodeItemCopyable;
import net.sf.mmm.code.api.item.CodeMutableItemWithType;
import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.api.modifier.CodeVisibility;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * Abstract interface for a field of a {@link CodeType}.
 *
 * @see CodeType#getProperties()
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeProperty extends CodeMember, CodeMutableItemWithType, CodeNodeItemCopyable<CodeProperties<?>, CodeProperty> {

  /**
   * @return the {@link CodeField} corresponding to this property or {@code null} if no such field exists.
   */
  CodeField getField();

  /**
   * @return the {@link CodeMethod} representing the getter of this property or {@code null} if no such method
   *         exists.
   */
  CodeMethod getGetter();

  /**
   * @return the {@link CodeMethod} representing the setter of this property or {@code null} if no such method
   *         exists.
   */
  CodeMethod getSetter();

  /**
   * @return {@code true} if this property is {@link CodeVisibility#PUBLIC public}
   *         {@link #isReadable(CodeVisibility) readable}, {@code false} otherwise.
   */
  default boolean isPublicReadable() {

    return isReadable(CodeVisibility.PUBLIC);
  }

  /**
   * @return {@code true} if this property is {@link CodeVisibility#PRIVATE private}
   *         {@link #isReadable(CodeVisibility) readable}, {@code false} otherwise.
   */
  default boolean isPrivateReadable() {

    return isReadable(CodeVisibility.PRIVATE);
  }

  /**
   * @param visibility the expected {@link CodeVisibility}.
   * @return {@code true} if this property is readable through {@link #getField() field} or
   *         {@link #getGetter() getter} with the given {@link CodeVisibility}, {@code false} otherwise.
   */
  boolean isReadable(CodeVisibility visibility);

  /**
   * @return {@code true} if this property is {@link CodeVisibility#PUBLIC public}
   *         {@link #isWritable(CodeVisibility) writable}, {@code false} otherwise.
   */
  default boolean isPublicWritable() {

    return isWritable(CodeVisibility.PUBLIC);
  }

  /**
   * @return {@code true} if this property is {@link CodeVisibility#PRIVATE private}
   *         {@link #isWritable(CodeVisibility) writable}, {@code false} otherwise.
   */
  default boolean isPrivateWritable() {

    return isWritable(CodeVisibility.PRIVATE);
  }

  /**
   * @param visibility the expected {@link CodeVisibility}.
   * @return {@code true} if this property is writable through
   *         non-{@link net.sf.mmm.code.api.modifier.CodeModifiers#KEY_FINAL final} {@link #getField() field}
   *         or {@link #getSetter() setter} with the given {@link CodeVisibility}, {@code false} otherwise.
   */
  boolean isWritable(CodeVisibility visibility);

  /**
   * @param declaring the new potential {@link #getDeclaringType()}.
   * @return a new {@link CodeProperty} with its {@link #getType() type} being
   *         {@link CodeGenericType#resolve(CodeGenericType) resolved} or this property itself if the type
   *         could not be resolved any further.
   */
  CodeProperty inherit(CodeType declaring);

  /**
   * @deprecated the {@link #getModifiers() modifiers} of a {@link CodeProperty} are calculated and cannot be
   *             modified.
   * @throws ReadOnlyException on every call.
   */
  @Deprecated
  @Override
  default void setModifiers(CodeModifiers modifiers) {

    throw new ReadOnlyException(getDeclaringType().getSimpleName() + ".properties." + getName(), "modifiers");
  }

}
