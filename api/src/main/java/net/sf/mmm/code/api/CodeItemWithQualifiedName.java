/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api;

import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * {@link CodeItem} that has a {@link #getSimpleName() simple name} and a {@link #getQualifiedName() qualified
 * name}, that is qualified via the {@link #getParentPackage() parent package}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeItemWithQualifiedName extends CodeItemWithComment {

  /**
   * @return the simple name of this element.
   * @see Class#getSimpleName()
   */
  String getSimpleName();

  /**
   * @param simpleName the new {@link #getSimpleName() simple name}.
   * @throws ReadOnlyException if {@link #isImmutable() immutable}.
   */
  void setSimpleName(String simpleName);

  /**
   * @return the qualified name of this element.
   */
  default String getQualifiedName() {

    CodePackage pkg = getParentPackage();
    String prefix = "";
    if (pkg != null) {
      prefix = pkg.getQualifiedName();
    }
    if (prefix.isEmpty()) {
      return getSimpleName();
    } else {
      return prefix + "." + getSimpleName();
    }
  }

  /**
   * @return the {@link CodePackage} containing this element. May only be {@code null} for the root
   *         {@link CodePackage} (default package).
   */
  CodePackage getParentPackage();

  /**
   * @param parentPackage the new {@link #getParentPackage() parent package}.
   * @throws ReadOnlyException if {@link #isImmutable() immutable}.
   */
  void setParentPackage(CodePackage parentPackage);

}
