/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.arg;

import net.sf.mmm.code.api.arg.CodeException;
import net.sf.mmm.code.impl.java.member.JavaMember;

/**
 * Implementation of {@link CodeException} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaException extends JavaOperationArg implements CodeException {

  /**
   * The constructor.
   *
   * @param declaringMember the {@link #getDeclaringMember() declaring member}.
   */
  public JavaException(JavaMember declaringMember) {

    super(declaringMember);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaException} to copy.
   */
  public JavaException(JavaException template) {

    super(template);
  }

}
