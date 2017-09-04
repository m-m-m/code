/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.arg;

import net.sf.mmm.code.api.element.CodeElementWithName;
import net.sf.mmm.code.api.member.CodeMember;
import net.sf.mmm.code.api.member.CodeOperation;
import net.sf.mmm.code.api.type.CodeType;

/**
 * {@link CodeOperationArg} for a parameter (argument) of a {@link CodeOperation}.
 *
 * @see CodeOperation#getParameters()
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeParameter extends CodeOperationArg, CodeElementWithName {

  /**
   * @deprecated use {@link #copy(CodeMember)} instead.
   */
  @Deprecated
  @Override
  default CodeParameter copy(CodeType newDeclaringType) {

    return copy(getDeclaringMember());
  }

  @Override
  CodeParameter copy(CodeMember newDeclaringMemeber);

}
