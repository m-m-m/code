/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.arg;

import net.sf.mmm.code.api.arg.CodeReturn;
import net.sf.mmm.code.api.member.CodeMember;
import net.sf.mmm.code.impl.java.member.JavaMember;

/**
 * Implementation of {@link CodeReturn} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaReturn extends JavaOperationArg implements CodeReturn {

  /**
   * The constructor.
   *
   * @param declaringMember the {@link #getDeclaringMember() declaring member}.
   */
  public JavaReturn(JavaMember declaringMember) {

    super(declaringMember);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaReturn} to copy.
   * @param declaringMember the {@link #getDeclaringMember() declaring member}.
   */
  public JavaReturn(JavaReturn template, JavaMember declaringMember) {

    super(template, declaringMember);
  }

  @Override
  public JavaReturn copy(CodeMember newDeclaringMember) {

    return new JavaReturn(this, (JavaMember) newDeclaringMember);
  }

}
