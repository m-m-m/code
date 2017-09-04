/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.item;

import net.sf.mmm.code.api.element.CodeElement;
import net.sf.mmm.code.api.type.CodeType;

/**
 * {@link CodeItemWithDeclaringType} that has a {@link #getDeclaringElement() declaring element}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeItemWithDeclaringElement extends CodeItemWithDeclaringType {

  /**
   * @return the {@link CodeElement} declaring this item.
   */
  CodeElement getDeclaringElement();

  @Override
  default CodeType getDeclaringType() {

    CodeElement element = getDeclaringElement();
    if (element instanceof CodeType) {
      return (CodeType) element;
    }
    return element.getDeclaringType();
  }

  /**
   * @see #copy(CodeElement)
   */
  @Override
  CodeItemWithDeclaringElement copy(CodeType newDeclaringType);

  /**
   * @param newDeclaringElement the new {@link #getDeclaringElement() declaring element}.
   * @return a new {@link #isImmutable() mutable} copy.
   * @see #copy(CodeType)
   */
  CodeItemWithDeclaringElement copy(CodeElement newDeclaringElement);

}
