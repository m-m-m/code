/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base;

import io.github.mmm.base.exception.DuplicateObjectException;
import io.github.mmm.code.api.CodePathElement;
import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.base.element.BaseElementWithQualifiedName;

/**
 * Base implementation of {@link CodePathElement}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BasePathElement extends BaseElementWithQualifiedName implements CodePathElement {

  /**
   * The constructor.
   *
   * @param parentPackage the {@link #getParentPackage() parent package}.
   * @param simpleName the {@link #getSimpleName() simple name}.
   */
  public BasePathElement(BasePackage parentPackage, String simpleName) {

    super(parentPackage, simpleName);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BasePathElement} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BasePathElement(BasePathElement template, CodeCopyMapper mapper) {

    super(template, mapper);
  }

  @Override
  protected void doSetParentPackage(BasePackage newParentPkg) {

    String simpleName = getSimpleName();
    if (newParentPkg.getChildren().get(simpleName, true) != null) {
      throw new DuplicateObjectException(getClass().getSimpleName(), simpleName);
    }
    BasePackage oldParentPkg = getParentPackage();
    super.doSetParentPackage(newParentPkg);
    oldParentPkg.getChildren().remove(this);
    newParentPkg.getChildren().add(this);
  }

  @Override
  public void removeFromParent() {

    // TODO Auto-generated method stub
    super.removeFromParent();
  }

}
