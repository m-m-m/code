/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.element;

import net.sf.mmm.code.api.node.CodeNodeItemWithQualifiedName;

/**
 * Combines {@link CodeElementWithModifiers} and {@link CodeNodeItemWithQualifiedName}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeElementWithQualifiedName extends CodeElement, CodeNodeItemWithQualifiedName {

  @Override
  CodeElementWithQualifiedName copy();

}
