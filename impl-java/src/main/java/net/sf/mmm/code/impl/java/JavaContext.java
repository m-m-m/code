/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.util.List;

import net.sf.mmm.code.api.CodePackage;
import net.sf.mmm.code.api.CodeType;
import net.sf.mmm.code.base.AbstractCodeContext;

/**
 * TODO: this class ...
 *
 * @author hohwille
 * @since 1.0.0
 */
public class JavaContext extends AbstractCodeContext<JavaType, JavaPackage> {

  /**
   * The constructor.
   */
  public JavaContext() {

    super();
    setRootPackage(new JavaPackage(this));
  }

  @Override
  public JavaType getType(CodePackage parentPackage, String simpleName) {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public JavaType createType(CodePackage parentPackage, String simpleName) {

    JavaPackage pkg = (JavaPackage) parentPackage;
    JavaFile file = new JavaFile(pkg, simpleName);
    JavaType type = new JavaType(file);
    file.getTypes().add(type);
    return type;
  }

  @Override
  public List<JavaPackage> getChildPackages(CodePackage pkg) {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<JavaType> getChildTypes(CodePackage pkg) {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public JavaType createType(CodeType declaringType, String simpleName) {

    JavaType declaring = (JavaType) declaringType;
    JavaType type = new JavaType(declaring.getFile(), simpleName);
    type.setDeclaringType(declaringType);
    return type;
  }

  @Override
  public JavaPackage getPackage(CodePackage parentPackage, String simpleName) {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public JavaPackage createPackage(CodePackage parentPackage, String simpleName) {

    JavaPackage parent = (JavaPackage) parentPackage;
    JavaPackage pkg = new JavaPackage(parent, simpleName);
    return pkg;
  }

  @Override
  public JavaImport createImport(CodeType type) {

    return new JavaImport(this, type.getQualifiedName(), false);
  }

}
