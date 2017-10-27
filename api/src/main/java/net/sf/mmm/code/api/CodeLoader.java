/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api;

import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeType;

/**
 * Abstract interface to load {@link #getPackage(String) packages} or {@link #getType(String) types}.
 *
 * @author hohwille
 * @since 1.0.0
 */
public abstract interface CodeLoader {

  /**
   * @param qualifiedName the {@link CodePackage#getQualifiedName() qualified name} of the requested
   *        {@link CodePackage}.
   * @return the requested {@link CodePackage} or {@code null} if not found.
   */
  CodePackage getPackage(String qualifiedName);

  /**
   * @param qualifiedName the {@link CodeGenericType#getQualifiedName() qualified name} of the requested
   *        {@link CodeGenericType}.
   * @return the requested {@link CodeGenericType}, typically a {@link net.sf.mmm.code.api.type.CodeType} or
   *         {@code null} if not found.
   */
  CodeType getType(String qualifiedName);

}
