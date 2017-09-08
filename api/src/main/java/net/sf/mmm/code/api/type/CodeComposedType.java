/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.type;

import java.util.List;

/**
 * {@link CodeGenericType} {@link #getTypes() composing multiple other types (interfaces)} to a virtual type
 * that does not actually exist as raw {@link net.sf.mmm.code.api.type.CodeType}.
 *
 * @see java.lang.reflect.TypeVariable
 * @see CodeType#getTypeVariables()
 * @see CodeGenericType#asTypeVariable()
 * @see CodeTypeVariables
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeComposedType extends CodeGenericType {

  /**
   * @return the {@link List} of {@link CodeGenericType}s this virtual type is composed of.
   */
  List<? extends CodeType> getTypes();

  @Override
  default CodeComposedType asComposedType() {

    return this;
  }

  @Override
  CodeComposedType copy();

}
