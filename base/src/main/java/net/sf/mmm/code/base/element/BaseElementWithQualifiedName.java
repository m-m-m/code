/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.element;

import net.sf.mmm.code.api.CodePackage;
import net.sf.mmm.code.api.item.CodeMutableItemWithQualifiedName;
import net.sf.mmm.code.base.BasePackage;

/**
 * Base implementation of {@link CodeMutableItemWithQualifiedName} as {@link BaseElementImpl}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BaseElementWithQualifiedName extends BaseElementImpl implements CodeMutableItemWithQualifiedName {

  private String simpleName;

  private BasePackage parentPackage;

  /**
   * The constructor.
   *
   * @param parentPackage the {@link #getParentPackage() parent package}.
   * @param simpleName the {@link #getSimpleName() simple name}.
   */
  public BaseElementWithQualifiedName(BasePackage parentPackage, String simpleName) {

    super();
    this.parentPackage = parentPackage;
    this.simpleName = simpleName;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseElementWithQualifiedName} to copy.
   */
  public BaseElementWithQualifiedName(BaseElementWithQualifiedName template) {

    super(template);
    this.simpleName = template.simpleName;
    this.parentPackage = template.parentPackage;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseElementWithQualifiedName} to copy.
   * @param parentPackage the {@link #getParentPackage() parent package}.
   */
  public BaseElementWithQualifiedName(BaseElementWithQualifiedName template, BasePackage parentPackage) {

    super(template);
    this.simpleName = template.simpleName;
    this.parentPackage = parentPackage;
  }

  @Override
  public String getSimpleName() {

    return this.simpleName;
  }

  @Override
  public void setSimpleName(String simpleName) {

    verifyMutalbe();
    this.simpleName = simpleName;
  }

  @Override
  public BasePackage getParentPackage() {

    return this.parentPackage;
  }

  @Override
  public void setParentPackage(CodePackage parentPackage) {

    verifyMutalbe();
    if (this.parentPackage == parentPackage) {
      return;
    }
    doSetParentPackage((BasePackage) parentPackage);
  }

  /**
   * Internal variant of {@link #setParentPackage(CodePackage)}.
   *
   * @param parentPkg the new {@link #getParentPackage() parent package}.
   */
  protected void doSetParentPackage(BasePackage parentPkg) {

    this.parentPackage = parentPkg;
  }

  @Override
  public abstract BaseElementWithQualifiedName copy();

}
