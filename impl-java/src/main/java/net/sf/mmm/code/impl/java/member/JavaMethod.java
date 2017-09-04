/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.member;

import net.sf.mmm.code.api.arg.CodeReturn;
import net.sf.mmm.code.api.member.CodeMethod;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link CodeMethod} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaMethod extends JavaOperation implements CodeMethod {

  private CodeReturn returns;

  /**
   * The constructor.
   *
   * @param declaringType the {@link #getDeclaringType()}.
   */
  public JavaMethod(JavaType declaringType) {

    super(declaringType);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaMethod} to copy.
   * @param declaringType the {@link #getDeclaringType()}.
   */
  public JavaMethod(JavaMethod template, JavaType declaringType) {

    super(template, declaringType);
  }

  @Override
  protected void doSetImmutable() {

    super.doSetImmutable();
    this.returns.setImmutable();
  }

  @Override
  public CodeReturn getReturns() {

    return this.returns;
  }

  @Override
  public void setReturns(CodeReturn returns) {

    verifyMutalbe();
    this.returns = returns;
  }

  @Override
  public void setName(String name) {

    String oldName = getName();
    super.setName(name);
    if (oldName.equals(name)) {
      return;
    }
    getDeclaringType().getProperties().rename(this, oldName);
  }

  @Override
  public JavaMethod copy(CodeType newDeclaringType) {

    return new JavaMethod(this, (JavaType) newDeclaringType);
  }

}
