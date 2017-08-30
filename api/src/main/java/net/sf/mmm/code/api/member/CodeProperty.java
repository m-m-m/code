/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.member;

import net.sf.mmm.code.api.CodeGenericType;
import net.sf.mmm.code.api.CodeItemWithType;
import net.sf.mmm.code.api.CodeType;

/**
 * Abstract interface for a field of a {@link CodeType}.
 *
 * @see CodeType#getFields(CodeMemberSelector)
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeProperty extends CodeMember, CodeItemWithType {

  /**
   * @return {@code true} if this property is readable (field or getter available), {@code false} otherwise.
   */
  boolean isReadable();

  /**
   * @return {@code true} if this property is writable (non-final field or setter available), {@code false}
   *         otherwise.
   */
  boolean isWritable();

  /**
   * @param declaring the new potential {@link #getDeclaringType()}.
   * @return a new {@link CodeProperty} with its {@link #getType() type} being
   *         {@link CodeGenericType#resolve(CodeGenericType) resolved} or this property itself if the type
   *         could not be resolved any further.
   */
  CodeProperty inherit(CodeType declaring);

}
