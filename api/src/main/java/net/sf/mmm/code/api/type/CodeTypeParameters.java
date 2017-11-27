/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.type;

import net.sf.mmm.code.api.copy.CodeNodeItemCopyable;

/**
 * {@link CodeGenericTypeParameters} with generic bound to {@link CodeGenericType}.
 *
 * @see CodeTypeVariables
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeTypeParameters extends CodeGenericTypeParameters<CodeGenericType>, CodeNodeItemCopyable<CodeParameterizedType, CodeTypeParameters> {

  @Override
  CodeTypeParameters copy();

}
