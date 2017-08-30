/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api;

import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * {@link CodeElement} that has a {@link #getName() name}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeElementWithName extends CodeElement {

  /**
   * @return the name of this element.
   */
  String getName();

  /**
   * @param name the new {@link #getName() name}.
   * @throws ReadOnlyException if {@link #isImmutable() immutable}.
   */
  void setName(String name);

}