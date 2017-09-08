/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.element;

import net.sf.mmm.code.api.element.CodeElementWithTypeVariables;
import net.sf.mmm.code.impl.java.type.JavaTypeVariables;

/**
 * {@link CodeElementWithTypeVariables} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface JavaElementWithTypeVariables extends CodeElementWithTypeVariables {

  @Override
  JavaTypeVariables getTypeVariables();

}
