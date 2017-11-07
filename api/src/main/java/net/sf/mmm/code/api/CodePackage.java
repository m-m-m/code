/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api;

import net.sf.mmm.code.api.element.CodeElement;
import net.sf.mmm.code.api.node.CodeContainer;

/**
 * {@link CodeElement} representing a {@link Package} (or similar namespace concept in case of other
 * language).
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodePackage extends CodePathElement, CodeContainer {

  /**
   * The {@link #getParentPackage() parent package} or the {@link #getSource() source} if this is a root
   * package.
   */
  @Override
  CodeContainer getParent();

  /**
   * @return the {@link CodePathElements} containing the child {@link CodePackage}s and {@link CodeFile}s of
   *         this package.
   */
  CodePathElements<?> getChildren();

  /**
   * @return {@code true} if this a regular package that requires an {@link CodeFile#getImports() import},
   *         {@code false} otherwise (in case of a standard package that is always visible such as
   *         "{{@code java.lang}" for Java).
   */
  boolean isRequireImport();

  /**
   * @return {@code true} if this is the root package (called "default package" in Java), {@code false}
   *         otherwise.
   */
  boolean isRoot();

  @Override
  Package getReflectiveObject();

  @Override
  CodePackage copy();

}
