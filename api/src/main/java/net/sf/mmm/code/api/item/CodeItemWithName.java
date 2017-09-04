/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.item;

/**
 * {@link CodeItem} that has a {@link #getName() name}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeItemWithName extends CodeItem {

  /**
   * @return the simple name of this element.
   * @see Class#getSimpleName()
   */
  String getName();

}
