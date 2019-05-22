/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.type;

import net.sf.mmm.code.api.copy.CodeNodeItemCopyable;
import net.sf.mmm.code.api.element.CodeElementWithDeclaringType;

/**
 * {@link CodeGenericType} representing an {@link #isArray() array} of a {@link #getComponentType() component type}.
 *
 * @see #createArray()
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeArrayType extends CodeGenericType, CodeNodeItemCopyable<CodeElementWithDeclaringType, CodeArrayType> {

  @Override
  default boolean isArray() {

    return true;
  }

}
