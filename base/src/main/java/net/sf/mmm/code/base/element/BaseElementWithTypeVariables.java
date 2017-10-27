/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.element;

import net.sf.mmm.code.api.element.CodeElementWithTypeVariables;
import net.sf.mmm.code.base.type.BaseTypeVariables;

/**
 * Base implementation of {@link CodeElementWithTypeVariables}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface BaseElementWithTypeVariables extends CodeElementWithTypeVariables, BaseElement {

  @Override
  BaseTypeVariables getTypeParameters();

  @Override
  BaseElementWithTypeVariables copy();

}
