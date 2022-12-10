/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.element;

import io.github.mmm.code.api.item.CodeItemWithName;

/**
 * {@link CodeElement} that has a {@link #getName() name}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeElementWithName extends CodeElementWithDeclaringType, CodeItemWithName {

  /**
   * @param name the new {@link #getName() name}.
   * @throws io.github.mmm.base.exception.ReadOnlyException if {@link #isImmutable() immutable}.
   */
  void setName(String name);

  @Override
  CodeElementWithName copy();

}
