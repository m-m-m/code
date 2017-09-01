/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.arg;

import net.sf.mmm.code.api.arg.CodeParameter;
import net.sf.mmm.code.impl.java.member.JavaMember;

/**
 * Implementation of {@link CodeParameter} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaParameter extends JavaOperationArg implements CodeParameter {

  private String name;

  /**
   * The constructor.
   *
   * @param declaringMember the {@link #getDeclaringMember() declaring member}.
   */
  public JavaParameter(JavaMember declaringMember) {

    super(declaringMember);
    this.name = "arg";
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaParameter} to copy.
   */
  public JavaParameter(JavaParameter template) {

    super(template);
    this.name = template.name;
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

}
