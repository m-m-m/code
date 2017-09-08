/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.element;

import java.util.Objects;

import net.sf.mmm.code.api.CodePackage;
import net.sf.mmm.code.api.element.CodeElementWithQualifiedName;
import net.sf.mmm.code.impl.java.JavaPackage;

/**
 * Implementation of {@link CodeElementWithQualifiedName} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class JavaElementWithQualifiedName extends JavaElement implements CodeElementWithQualifiedName {

  private String simpleName;

  private JavaPackage parentPackage;

  private Runnable lazyInit;

  /**
   * The constructor.
   *
   * @param parentPackage the {@link #getParentPackage() parent package}.
   * @param simpleName the {@link #getSimpleName() simple name}.
   */
  public JavaElementWithQualifiedName(JavaPackage parentPackage, String simpleName) {

    super();
    this.parentPackage = parentPackage;
    this.simpleName = simpleName;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaElementWithQualifiedName} to copy.
   */
  public JavaElementWithQualifiedName(JavaElementWithQualifiedName template) {

    super(template);
    this.simpleName = template.simpleName;
    this.parentPackage = template.parentPackage;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaElementWithQualifiedName} to copy.
   * @param parentPackage the {@link #getParentPackage() parent package}.
   */
  public JavaElementWithQualifiedName(JavaElementWithQualifiedName template, JavaPackage parentPackage) {

    super(template);
    template.lazyInit();
    this.simpleName = template.simpleName;
    this.parentPackage = parentPackage;
  }

  /**
   * @param lazyInit the lazy initializer. Should only be set once directly after construction.
   */
  void setLazyInit(Runnable lazyInit) {

    this.lazyInit = lazyInit;
  }

  /**
   * Runs a potential lazy initializer.
   */
  protected void lazyInit() {

    if (this.lazyInit != null) {
      this.lazyInit.run();
      this.lazyInit = null;
    }
  }

  @Override
  public void setImmutable() {

    lazyInit();
    super.setImmutable();
  }

  @Override
  protected void verifyMutalbe() {

    lazyInit();
    super.verifyMutalbe();
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
  public JavaPackage getParentPackage() {

    return this.parentPackage;
  }

  @Override
  public void setParentPackage(CodePackage parentPackage) {

    verifyMutalbe();
    if (this.parentPackage == parentPackage) {
      return;
    }
    doSetParentPackage((JavaPackage) parentPackage);
  }

  /**
   * Internal variant of {@link #setParentPackage(CodePackage)}.
   *
   * @param parentPkg the new {@link #getParentPackage() parent package}.
   */
  protected void doSetParentPackage(JavaPackage parentPkg) {

    this.parentPackage = parentPkg;
  }

  @Override
  public boolean equals(Object obj) {

    if (obj == this) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    JavaElementWithQualifiedName other = (JavaElementWithQualifiedName) obj;
    if (!Objects.equals(getQualifiedName(), other.getQualifiedName())) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {

    return Objects.hashCode(getQualifiedName());
  }
}
