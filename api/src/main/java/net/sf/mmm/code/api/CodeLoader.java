/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api;

import net.sf.mmm.code.api.type.CodeType;

/**
 * Abstract interface to get (or load) {@link #getType(String) types}.
 *
 * @author hohwille
 * @since 1.0.0
 */
public abstract interface CodeLoader extends CodeWithContext {

  /**
   * @param qualifiedName the {@link CodeType#getQualifiedName() qualified name} of the requested
   *        {@link CodeType}.
   * @return the requested {@link CodeType} or {@code null} if not found.
   */
  CodeType getType(String qualifiedName);

  /**
   * @param qualifiedName the {@link CodeName} of the requested {@link CodeType}.
   * @return the requested {@link CodeType} or {@code null} if not found.
   */
  CodeType getType(CodeName qualifiedName);

}
