/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.member;

import java.lang.reflect.AccessibleObject;

import io.github.mmm.code.api.element.CodeElementWithModifiers;
import io.github.mmm.code.api.element.CodeElementWithName;
import io.github.mmm.code.api.item.CodeItemWithType;

/**
 * {@link CodeElementWithModifiers} representing a {@link java.lang.reflect.Member} of a
 * {@link io.github.mmm.code.api.type.CodeType} that is either a {@link CodeOperation} or a {@link CodeField}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeMember extends CodeElementWithModifiers, CodeElementWithName, CodeItemWithType {

  @Override
  CodeMembers<?> getParent();

  /**
   * @see java.lang.reflect.Member
   */
  @Override
  AccessibleObject getReflectiveObject();

  @Override
  CodeMember copy();

}
