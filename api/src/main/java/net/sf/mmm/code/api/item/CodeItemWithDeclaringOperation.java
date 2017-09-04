/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.item;

import net.sf.mmm.code.api.member.CodeOperation;
import net.sf.mmm.code.api.type.CodeType;

/**
 * {@link CodeItemWithDeclaringType} that has an optional {@link #getDeclaringOperation() declaring
 * operation}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeItemWithDeclaringOperation extends CodeItemWithDeclaringType {

  /**
   * @return the {@link CodeOperation} declaring this item or {@code null} if {@link #getDeclaringType()
   *         declared by a type}.
   */
  CodeOperation getDeclaringOperation();

  /**
   * @see #copy(CodeOperation)
   */
  @Override
  CodeItemWithDeclaringOperation copy(CodeType newDeclaringType);

  /**
   * @param newDeclaringOperation the new {@link #getDeclaringOperation() declaring operation}.
   * @return a new {@link #isImmutable() mutable} copy.
   * @see #copy(CodeType)
   */
  CodeItemWithDeclaringOperation copy(CodeOperation newDeclaringOperation);

}
