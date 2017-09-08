/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api;

import net.sf.mmm.code.api.element.CodeElement;
import net.sf.mmm.code.api.node.CodeContainer;
import net.sf.mmm.code.api.source.CodeSource;
import net.sf.mmm.code.api.type.CodeType;

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
   * <b>Attention:</b><br>
   * Do not get confused by this method. For simple usage you most probably want to use
   * {@link #getParentPackage()} and can ignore this method. However, it is exposed for experts that want to
   * do very advanced things. In order to support the layering of {@link CodeSource}s we have multiple
   * representations of the same logical package for split packages. These are chained together via this super
   * layer package property.
   *
   * @return the optional {@link CodePackage} this package inherits from. May be {@code null}.
   * @see CodePathElements#get(String, boolean)
   */
  CodePackage getSuperLayerPackage();

  /**
   * @return the {@link CodePathElements} containing the child {@link CodePackage}s and {@link CodeFile}s of
   *         this package.
   */
  CodePathElements getChildren();

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

  /**
   * @deprecated a {@link CodePackage} contains {@link CodeType}s and not vice versa. Therefore this method
   *             will always return {@code null} here.
   */
  @Deprecated
  @Override
  default CodeType getDeclaringType() {

    return null;
  }

  @Override
  CodePackage copy();

}
