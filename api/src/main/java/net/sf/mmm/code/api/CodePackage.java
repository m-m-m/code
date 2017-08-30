/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api;

/**
 * {@link CodeElement} representing a {@link Package} (or similar namespace concept in case of other
 * language).
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodePackage extends CodeElementWithQualifiedName {

  /**
   * @deprecated a {@link CodePackage} contains {@link CodeType}s and not vice versa. Therefore this method
   *             will always return {@code null} here.
   */
  @Deprecated
  @Override
  default CodeType getDeclaringType() {

    return null;
  }

}
