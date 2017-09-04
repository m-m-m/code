/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.item;

import net.sf.mmm.code.api.member.CodeMember;

/**
 * {@link CodeItem} that has a {@link #getDeclaringMember() declaring member}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeItemWithDeclaringMember extends CodeItem {

  /**
   * @return the {@link CodeMember} declaring this item.
   */
  CodeMember getDeclaringMember();

  /**
   * @param newDeclaringMember the new {@link #getDeclaringMember() declaring member}.
   * @return a new {@link #isImmutable() mutable} copy.
   */
  CodeItemWithDeclaringMember copy(CodeMember newDeclaringMember);

}
