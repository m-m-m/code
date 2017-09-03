/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.modifier;

import net.sf.mmm.code.api.element.CodeElement;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * {@link CodeElement} that has {@link #getModifiers() modifiers}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeElementWithModifiers extends CodeElement {

  /**
   * @return the {@link CodeModifiers} of this element.
   */
  CodeModifiers getModifiers();

  /**
   * @param modifiers the new {@link #getModifiers() modifiers}.
   * @throws ReadOnlyException if {@link #isImmutable() immutable}.
   */
  void setModifiers(CodeModifiers modifiers);

}
