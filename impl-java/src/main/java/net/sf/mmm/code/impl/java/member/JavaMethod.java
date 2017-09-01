/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.member;

import java.io.IOException;
import java.util.List;

import net.sf.mmm.code.api.CodeGenericType;
import net.sf.mmm.code.api.arg.CodeReturn;
import net.sf.mmm.code.api.member.CodeMethod;
import net.sf.mmm.code.impl.java.JavaGenericType;
import net.sf.mmm.code.impl.java.JavaType;

/**
 * Implementation of {@link CodeMethod} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaMethod extends JavaOperation implements CodeMethod {

  private List<JavaGenericType> typeParameters;

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
   */
  public JavaMethod(JavaMethod template) {

    super(template);
  }

  @Override
  public List<? extends JavaGenericType> getTypeParameters() {

    return this.typeParameters;
  }

  @Override
  public void addTypeParameter(CodeGenericType typeParameter) {

    verifyMutalbe();
    this.typeParameters.add((JavaGenericType) typeParameter);
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
  protected void doWriteSignature(Appendable sink) throws IOException {

    String separator = "<";
    for (JavaGenericType typeParam : this.typeParameters) {
      sink.append(separator);
      typeParam.writeReference(sink, true);
      separator = ", ";
    }
    if (separator.length() != 1) {
      sink.append("> ");
    }
    super.doWriteSignature(sink);
  }

}
