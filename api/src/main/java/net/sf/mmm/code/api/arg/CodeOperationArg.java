/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.arg;

import net.sf.mmm.code.api.element.CodeElement;
import net.sf.mmm.code.api.item.CodeItemWithDeclaringMember;
import net.sf.mmm.code.api.item.CodeItemWithType;
import net.sf.mmm.code.api.member.CodeMember;
import net.sf.mmm.code.api.member.CodeOperation;
import net.sf.mmm.code.api.type.CodeType;

/**
 * A {@link CodeItemWithType} representing a argument of a {@link CodeOperation} such as
 * {@link CodeParameter}, {@link CodeException} or {@link CodeReturn}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeOperationArg extends CodeElement, CodeItemWithType, CodeItemWithDeclaringMember {

  /**
   * @deprecated use {@link #copy(CodeMember)} instead.
   */
  @Deprecated
  @Override
  default CodeOperationArg copy(CodeType newDeclaringType) {

    return copy(getDeclaringMember());
  }

  /**
   * @param newDeclaringMember the new {@link #getDeclaringMember() declaring member}.
   * @return a new {@link #isImmutable() mutable} copy.
   */
  @Override
  CodeOperationArg copy(CodeMember newDeclaringMember);

}
