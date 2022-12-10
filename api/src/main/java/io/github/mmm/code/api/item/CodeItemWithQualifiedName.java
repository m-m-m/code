/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.item;

/**
 * {@link CodeItem} that has a {@link #getSimpleName() simple name} and a {@link #getQualifiedName() qualified
 * name}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeItemWithQualifiedName extends CodeItem {

  /**
   * @return the simple name of this element.
   * @see Class#getSimpleName()
   */
  String getSimpleName();

  /**
   * @return the qualified name of this element.
   */
  String getQualifiedName();

}