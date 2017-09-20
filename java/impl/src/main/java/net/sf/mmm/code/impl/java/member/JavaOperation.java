/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.member;

import java.io.IOException;
import java.lang.reflect.Executable;

import net.sf.mmm.code.api.member.CodeOperation;
import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.api.statement.CodeBody;
import net.sf.mmm.code.impl.java.arg.JavaExceptions;
import net.sf.mmm.code.impl.java.arg.JavaParameters;
import net.sf.mmm.code.impl.java.element.JavaElementWithTypeVariables;
import net.sf.mmm.code.impl.java.type.JavaTypeVariables;

/**
 * Implementation of {@link CodeOperation} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class JavaOperation extends JavaMember implements CodeOperation, JavaElementWithTypeVariables {

  private final JavaTypeVariables typeVariables;

  private JavaParameters parameters;

  private JavaExceptions exceptions;

  private CodeBody body;

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   */
  public JavaOperation(String name) {

    super(CodeModifiers.MODIFIERS_PUBLIC, name);
    this.typeVariables = new JavaTypeVariables(this);
    this.parameters = new JavaParameters(this);
    this.exceptions = new JavaExceptions(this);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaOperation} to copy.
   */
  public JavaOperation(JavaOperation template) {

    super(template);
    this.typeVariables = template.typeVariables.copy(getDeclaringType());
    this.parameters = template.parameters.copy(this);
    this.exceptions = template.exceptions.copy(this);
  }

  @Override
  public abstract JavaOperations<?> getParent();

  @Override
  protected void doSetImmutable() {

    super.doSetImmutable();
    this.typeVariables.setImmutable();
    this.parameters.setImmutable();
    this.exceptions.setImmutable();
  }

  @Override
  public JavaTypeVariables getTypeParameters() {

    return this.typeVariables;
  }

  @Override
  public JavaParameters getParameters() {

    return this.parameters;
  }

  @Override
  public JavaExceptions getExceptions() {

    return this.exceptions;
  }

  @Override
  public CodeBody getBody() {

    return this.body;
  }

  @Override
  public void setBody(CodeBody body) {

    verifyMutalbe();
    this.body = body;
  }

  @Override
  public abstract Executable getReflectiveObject();

  @Override
  public abstract JavaOperation copy();

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent) throws IOException {

    super.doWrite(sink, newline, defaultIndent, currentIndent);
    this.typeVariables.write(sink, newline, null, null);
    doWriteSignature(sink, newline);
    if (this.body == null) {
      sink.append(';');
    } else {
      sink.append(" {");
      sink.append(newline);
      this.body.write(sink, defaultIndent, currentIndent + defaultIndent);
      sink.append(currentIndent);
      sink.append("}");
      sink.append(newline);
    }
  }

  /**
   * Writes the operation signature with {@link #getName() name}, {@link #getParameters() args} and
   * {@link #getExceptions() exceptions}.
   *
   * @param sink the {@link Appendable}.
   * @param newline the newline {@link String}.
   * @throws IOException if thrown by {@link Appendable}.
   */
  protected void doWriteSignature(Appendable sink, String newline) throws IOException {

    sink.append(getName());
    sink.append('(');
    this.parameters.write(sink, newline, null, null);
    sink.append(')');
    this.exceptions.write(sink, newline, null, null);
  }

}
