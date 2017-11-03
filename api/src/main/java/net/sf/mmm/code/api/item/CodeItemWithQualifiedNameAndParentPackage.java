/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.item;

import net.sf.mmm.code.api.CodePackage;

/**
 * {@link CodeItemWithQualifiedName} that has a {@link #getParentPackage() parent package}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeItemWithQualifiedNameAndParentPackage extends CodeItemWithQualifiedName {

  @Override
  default String getQualifiedName() {

    CodePackage pkg = getParentPackage();
    String prefix = "";
    if (pkg != null) {
      prefix = pkg.getQualifiedName();
    }
    if (prefix.isEmpty()) {
      return getSimpleName();
    } else {
      return prefix + getSyntax().getPackageSeparator() + getSimpleName();
    }
  }

  /**
   * @return the {@link CodePackage} containing this element. May only be {@code null} for the root
   *         {@link CodePackage} (default package).
   */
  CodePackage getParentPackage();

}
