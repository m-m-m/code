/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.item;

import net.sf.mmm.code.api.CodePackage;
import net.sf.mmm.code.api.item.CodeItemWithQualifiedName;
import net.sf.mmm.code.impl.java.JavaPackage;

/**
 * Implementation of {@link CodeItemWithQualifiedName} for Java reflection.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class JavaItemWithQualifiedName extends JavaItemWithComment implements CodeItemWithQualifiedName {

  private String simpleName;

  private JavaPackage parentPackage;

  /**
   * The constructor.
   *
   * @param parentPackage the {@link #getParentPackage() parent package}.
   * @param simpleName the {@link #getSimpleName() simple name}.
   */
  public JavaItemWithQualifiedName(JavaPackage parentPackage, String simpleName) {

    super(parentPackage.getContext());
    this.parentPackage = parentPackage;
    this.simpleName = simpleName;
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
    this.parentPackage = (JavaPackage) parentPackage;
  }

}
