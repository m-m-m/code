/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api;

import net.sf.mmm.code.api.element.CodeElement;
import net.sf.mmm.code.api.type.CodeType;

/**
 * The top-level context used to retrieve existing {@link CodeElement}s or create new ones.
 *
 * @author hohwille
 * @since 1.0.0
 */
public interface CodeLoader {

  /**
   * @param qualifiedName the {@link CodePackage#getQualifiedName() qualified name} of the requested
   *        {@link CodePackage}.
   * @return the requested {@link CodePackage} or {@code null} if not found.
   */
  CodePackage getPackage(String qualifiedName);

  /**
   * @param qualifiedName the {@link CodeType#getQualifiedName() qualified name} of the requested
   *        {@link CodeType}.
   * @return the requested {@link CodeType} or {@code null} if not found.
   */
  CodeType getType(String qualifiedName);

}
