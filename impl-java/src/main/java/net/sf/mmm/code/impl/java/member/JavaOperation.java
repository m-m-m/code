/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.member;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.mmm.code.api.arg.CodeException;
import net.sf.mmm.code.api.arg.CodeParameter;
import net.sf.mmm.code.api.member.CodeOperation;
import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.api.statement.CodeBody;
import net.sf.mmm.code.impl.java.JavaType;

/**
 * Implementation of {@link CodeOperation} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class JavaOperation extends JavaMember implements CodeOperation {

  private CodeBody body;

  private List<CodeParameter> parameters;

  private List<CodeException> exceptions;

  /**
   * The constructor.
   *
   * @param declaringType the {@link #getDeclaringType()}.
   */
  public JavaOperation(JavaType declaringType) {

    super(declaringType, CodeModifiers.MODIFIERS_PUBLIC);
    this.parameters = new ArrayList<>();
    this.exceptions = new ArrayList<>();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaOperation} to copy.
   */
  public JavaOperation(JavaOperation template) {

    super(template);
    this.parameters = copy(template.parameters);
    this.exceptions = copy(template.exceptions);
  }

  @Override
  public List<? extends CodeParameter> getParameters() {

    return this.parameters;
  }

  @Override
  public List<? extends CodeException> getExceptions() {

    return this.exceptions;
  }

  @Override
  public CodeBody getBody() {

    return this.body;
  }

  @Override
  protected void doWrite(Appendable sink, String defaultIndent, String currentIndent) throws IOException {

    super.doWrite(sink, defaultIndent, currentIndent);
    doWriteSignature(sink);
    if (this.body == null) {
      sink.append(';');
    } else {
      sink.append(" {");
      writeNewline(sink);
      this.body.write(sink, defaultIndent, currentIndent + defaultIndent);
      sink.append(currentIndent);
      sink.append("}");
      writeNewline(sink);
    }
  }

  /**
   * Writes the operation signature with {@link #getName() name}, {@link #getParameters() args} and
   * {@link #getExceptions() exceptions}.
   * 
   * @param sink the {@link Appendable}.
   * @throws IOException if thrown by {@link Appendable}.
   */
  protected void doWriteSignature(Appendable sink) throws IOException {

    sink.append(getName());
    String separator = "(";
    for (CodeParameter arg : this.parameters) {
      sink.append(separator);
      arg.write(sink);
      separator = ", ";
    }
    sink.append(')');
    separator = " throws ";
    for (CodeException ex : this.exceptions) {
      sink.append(separator);
      ex.write(sink);
      separator = ", ";
    }
  }

}
