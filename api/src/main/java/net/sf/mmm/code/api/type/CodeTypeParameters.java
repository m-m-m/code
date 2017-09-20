/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.type;

/**
 * {@link CodeGenericTypeParameters} with generic bound to {@link CodeGenericType}.
 *
 * @see CodeTypeVariables
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <P> the type of the contained type parameters.
 * @since 1.0.0
 */
public interface CodeTypeParameters<P extends CodeGenericType> extends CodeGenericTypeParameters<P> {

  @Override
  CodeParameterizedType getParent();

  @Override
  CodeTypeParameters<P> copy();

}
