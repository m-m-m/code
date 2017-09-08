/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.member;

import net.sf.mmm.code.api.element.CodeElementWithModifiers;
import net.sf.mmm.code.api.element.CodeElementWithName;

/**
 * {@link CodeElementWithModifiers} representing a member of a {@link net.sf.mmm.code.api.type.CodeType} that
 * is either a {@link CodeOperation} or a {@link CodeField}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeMember extends CodeElementWithModifiers, CodeElementWithName {

  @Override
  CodeMembers<?> getParent();

  @Override
  CodeMember copy();

}
