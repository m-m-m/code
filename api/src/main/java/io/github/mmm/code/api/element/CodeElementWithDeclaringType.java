/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.element;

import io.github.mmm.code.api.item.CodeItemWithDeclaringType;

/**
 * {@link CodeElement} that has a {@link #getDeclaringType() declaring type}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeElementWithDeclaringType extends CodeElement, CodeItemWithDeclaringType {

  @Override
  CodeElementWithDeclaringType copy();

}
