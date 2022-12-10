/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.node;

import io.github.mmm.code.api.element.CodeElement;
import io.github.mmm.code.api.element.CodeElementWithDeclaringType;
import io.github.mmm.code.api.item.CodeItemWithDeclaringType;
import io.github.mmm.code.api.type.CodeType;

/**
 * {@link CodeItemWithDeclaringType} that has a {@link #getDeclaringElement() declaring element}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeNodeItemWithDeclaringElement extends CodeItemWithDeclaringType {

  /**
   * @return the {@link CodeElement} declaring this item.
   */
  CodeElementWithDeclaringType getDeclaringElement();

  @Override
  default CodeType getDeclaringType() {

    CodeElementWithDeclaringType element = getDeclaringElement();
    if (element instanceof CodeType) {
      return (CodeType) element;
    }
    return element.getDeclaringType();
  }

}
