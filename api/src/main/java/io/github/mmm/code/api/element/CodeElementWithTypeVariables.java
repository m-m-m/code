/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.element;

import java.lang.reflect.GenericDeclaration;

import io.github.mmm.code.api.type.CodeTypeVariables;

/**
 * {@link CodeElement} that optionally has {@link #getTypeParameters() type variables}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeElementWithTypeVariables extends CodeElement {

  /**
   * @return the {@link CodeTypeVariables} containing the {@link io.github.mmm.code.api.type.CodeTypeVariable}s. May be
   *         empty but never {@code null}.
   */
  CodeTypeVariables getTypeParameters();

  @Override
  GenericDeclaration getReflectiveObject();

  @Override
  CodeElementWithTypeVariables copy();

}
