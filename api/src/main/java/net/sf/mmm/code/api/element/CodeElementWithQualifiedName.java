/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.element;

import net.sf.mmm.code.api.item.CodeItemWithQualifiedName;
import net.sf.mmm.code.api.modifier.CodeElementWithModifiers;
import net.sf.mmm.code.api.type.CodeType;

/**
 * Combines {@link CodeElementWithModifiers} and {@link CodeItemWithQualifiedName}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeElementWithQualifiedName extends CodeElement, CodeItemWithQualifiedName {

  @Override
  CodeElementWithQualifiedName copy(CodeType newDeclaringType);

}
