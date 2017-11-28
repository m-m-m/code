/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.element;

import net.sf.mmm.code.api.CodePackage;
import net.sf.mmm.code.api.copy.CodeCopyMapper;
import net.sf.mmm.code.api.copy.CodeCopyType;
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

  private String qualifiedName;

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
    if (parentPackage == null) {
      if (!"".equals(simpleName)) {
        throw new IllegalArgumentException("Root package name has to be empty!");
      }
      this.simpleName = simpleName;
    } else {
      this.simpleName = parentPackage.getLanguage().verifySimpleName(this, simpleName);
    }
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseElementWithQualifiedName} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseElementWithQualifiedName(BaseElementWithQualifiedName template, CodeCopyMapper mapper) {

    super(template, mapper);
    this.simpleName = template.simpleName;
    this.parentPackage = mapper.map(template.parentPackage, CodeCopyType.PARENT);
  }

  @Override
  public String getSimpleName() {

    return this.simpleName;
  }

  @Override
  public void setSimpleName(String simpleName) {

    verifyMutalbe();
    this.simpleName = getLanguage().verifySimpleName(this, simpleName);
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
  public String getQualifiedName() {

    if (this.qualifiedName != null) {
      return this.qualifiedName;
    }
    String result = CodeMutableItemWithQualifiedName.super.getQualifiedName();
    if (isImmutable()) {
      this.qualifiedName = result;
    }
    return result;
  }

}
