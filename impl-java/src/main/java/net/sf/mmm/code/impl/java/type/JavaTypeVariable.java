/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.type;

import java.io.IOException;

import net.sf.mmm.code.api.CodeGenericType;
import net.sf.mmm.code.api.member.CodeOperation;
import net.sf.mmm.code.api.type.CodeTypeVariable;
import net.sf.mmm.code.api.type.CodeTypeVariables;
import net.sf.mmm.code.impl.java.member.JavaOperation;

/**
 * Implementation of {@link CodeTypeVariable} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaTypeVariable extends JavaGenericType implements CodeTypeVariable {

  private JavaType declaringType;

  private JavaOperation declaringOperation;

  private JavaType type;

  private String name;

  /**
   * The constructor.
   *
   * @param declaringType the {@link #getDeclaringType() declaring type}.
   */
  public JavaTypeVariable(JavaType declaringType) {

    super(declaringType.getContext());
    this.declaringOperation = null;
    this.declaringType = declaringType;
    this.type = getContext().getRootType();
  }

  /**
   * The constructor.
   *
   * @param declaringOperation the {@link #getDeclaringOperation() declaring operation}.
   */
  public JavaTypeVariable(JavaOperation declaringOperation) {

    super(declaringOperation.getContext());
    this.declaringOperation = declaringOperation;
    this.declaringType = this.declaringType.getDeclaringType();
    this.type = getContext().getRootType();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaTypeVariable} to copy.
   */
  public JavaTypeVariable(JavaTypeVariable template) {

    super(template);
    this.declaringType = template.declaringType;
    this.declaringOperation = template.declaringOperation;
    this.type = template.type;
  }

  @Override
  public JavaTypeVariable asTypeVariable() {

    return this;
  }

  @Override
  public boolean isQualified() {

    return false;
  }

  @Override
  public boolean isArray() {

    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public CodeGenericType resolve(CodeGenericType context) {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void writeReference(Appendable sink, boolean declaration) throws IOException {

    // TODO Auto-generated method stub

  }

  @Override
  public JavaType getDeclaringType() {

    return this.declaringType;
  }

  @Override
  public CodeOperation getDeclaringOperation() {

    return this.declaringOperation;
  }

  @Override
  public String getName() {

    return this.name;
  }

  @Override
  public void setName(String name) {

    verifyMutalbe();
    this.name = name;
  }

  @Override
  public boolean isExtends() {

    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isSuper() {

    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isWildcard() {

    return "?".equals(this.name);
  }

  @Override
  public JavaType asType() {

    return this.type;
  }

  @Override
  public CodeTypeVariables getTypeVariables() {

    // TODO Auto-generated method stub
    return null;
  }

}
