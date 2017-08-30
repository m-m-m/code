/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api;

import net.sf.mmm.util.exception.api.ObjectNotFoundException;

/**
 * The top-level entry-point to retrieve code.
 *
 * @author hohwille
 * @since 1.0.0
 */
public interface Code {

  /**
   * @param qualifiedName the {@link CodeType#getQualifiedName() qualified name} of the requested
   *        {@link CodeType}.
   * @return the requested {@link CodeType} or {@code null} if not found.
   */
  CodeType getType(String qualifiedName);

  /**
   * @param qualifiedName the {@link CodeType#getQualifiedName() qualified name} of the requested
   *        {@link CodeType}.
   * @return the requested {@link CodeType}.
   * @throws ObjectNotFoundException if the type was not found.
   */
  default CodeType getRequiredType(String qualifiedName) {

    CodeType type = getType(qualifiedName);
    if (type == null) {
      throw new ObjectNotFoundException(CodeType.class, qualifiedName);
    }
    return type;
  }

  /**
   * @param qualifiedName the {@link CodePackage#getQualifiedName() qualified name} of the requested
   *        {@link CodePackage}.
   * @return the requested {@link CodePackage} or {@code null} if not found.
   */
  CodePackage getPackage(String qualifiedName);

  /**
   * @param qualifiedName the {@link CodePackage#getQualifiedName() qualified name} of the requested
   *        {@link CodePackage}.
   * @return the requested {@link CodePackage}.
   * @throws ObjectNotFoundException if the type was not found.
   */
  default CodePackage getRequiredPackage(String qualifiedName) {

    CodePackage pkg = getPackage(qualifiedName);
    if (pkg == null) {
      throw new ObjectNotFoundException(CodePackage.class, qualifiedName);
    }
    return pkg;
  }

}
