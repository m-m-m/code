/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.element;

import io.github.mmm.code.api.item.CodeItemWithModifiers;
import io.github.mmm.code.api.modifier.CodeModifiers;

/**
 * {@link CodeElement} that has {@link #getModifiers() modifiers}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeElementWithModifiers extends CodeElementWithDeclaringType, CodeItemWithModifiers {

  /**
   * @param modifiers the new {@link #getModifiers() modifiers}.
   * @throws io.github.mmm.base.exception.ReadOnlyException if {@link #isImmutable() immutable}.
   */
  void setModifiers(CodeModifiers modifiers);

  @Override
  CodeElementWithModifiers copy();

}
