/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.arg;

import java.io.IOException;

import net.sf.mmm.code.api.CodeGenericType;
import net.sf.mmm.code.api.arg.CodeOperationArg;
import net.sf.mmm.code.api.member.CodeMember;
import net.sf.mmm.code.impl.java.JavaElement;
import net.sf.mmm.code.impl.java.JavaGenericType;
import net.sf.mmm.code.impl.java.JavaType;
import net.sf.mmm.code.impl.java.member.JavaMember;

/**
 * Implementation of {@link CodeOperationArg} for Java.
 *
 * @author hohwille
 * @since 1.0.0
 */
public class JavaOperationArg extends JavaElement implements CodeOperationArg {

  private final JavaMember declaringMember;

  private JavaGenericType type;

  /**
   * The constructor.
   *
   * @param declaringMember the {@link #getDeclaringMember() declaring member}.
   */
  public JavaOperationArg(JavaMember declaringMember) {

    super(declaringMember.getContext());
    this.declaringMember = declaringMember;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaOperationArg} to copy.
   */
  public JavaOperationArg(JavaOperationArg template) {

    super(template);
    this.declaringMember = template.declaringMember;
  }

  @Override
  public JavaType getDeclaringType() {

    return this.declaringMember.getDeclaringType();
  }

  @Override
  public CodeMember getDeclaringMember() {

    return this.declaringMember;
  }

  @Override
  public JavaGenericType getType() {

    return this.type;
  }

  @Override
  public void setType(CodeGenericType type) {

    verifyMutalbe();
    this.type = (JavaGenericType) type;
  }

  @Override
  protected void doWrite(Appendable sink, String defaultIndent, String currentIndent) throws IOException {

    doWriteAnnotations(sink, "", "");
    this.type.writeReference(sink, false);
  }

}
