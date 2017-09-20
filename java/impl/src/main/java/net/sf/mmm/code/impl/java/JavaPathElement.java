/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import net.sf.mmm.code.api.CodePathElement;
import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.impl.java.element.JavaElementWithQualifiedName;
import net.sf.mmm.util.exception.api.DuplicateObjectException;

/**
 * Implementation of {@link CodeItem} for Java reflection.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class JavaPathElement extends JavaElementWithQualifiedName implements CodePathElement {

  /**
   * The constructor.
   *
   * @param parentPackage the {@link #getParentPackage() parent package}.
   * @param simpleName the {@link #getSimpleName() simple name}.
   */
  public JavaPathElement(JavaPackage parentPackage, String simpleName) {

    super(parentPackage, simpleName);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaPathElement} to copy.
   * @param parentPackage the {@link #getParentPackage() parent package}.
   */
  public JavaPathElement(JavaPathElement template, JavaPackage parentPackage) {

    super(template, parentPackage);
  }

  @Override
  protected void doSetParentPackage(JavaPackage newParentPkg) {

    String simpleName = getSimpleName();
    if (newParentPkg.getChildren().get(simpleName, true) != null) {
      throw new DuplicateObjectException(getClass().getSimpleName(), simpleName);
    }
    JavaPackage oldParentPkg = getParentPackage();
    super.doSetParentPackage(newParentPkg);
    oldParentPkg.getChildren().remove(this);
    newParentPkg.getChildren().add(this);
  }

  @Override
  public void removeFromParent() {

    // TODO Auto-generated method stub
    super.removeFromParent();
  }

  /**
   * @return {@code true} if this is a {@link JavaFile}, {@code false} otherwise (if a {@link JavaPackage}).
   */
  public abstract boolean isFile();
}
