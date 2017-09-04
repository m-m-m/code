/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.arg;

import java.io.IOException;

import net.sf.mmm.code.api.arg.CodeOperationArg;
import net.sf.mmm.code.api.member.CodeMember;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.impl.java.element.JavaElement;
import net.sf.mmm.code.impl.java.member.JavaMember;
import net.sf.mmm.code.impl.java.type.JavaGenericType;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link CodeOperationArg} for Java.
 *
 * @author hohwille
 * @since 1.0.0
 */
public abstract class JavaOperationArg extends JavaElement implements CodeOperationArg {

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
   * @param declaringMember the {@link #getDeclaringMember() declaring member}.
   */
  public JavaOperationArg(JavaOperationArg template, JavaMember declaringMember) {

    super(template);
    this.declaringMember = declaringMember;
    this.type = template.type;
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
