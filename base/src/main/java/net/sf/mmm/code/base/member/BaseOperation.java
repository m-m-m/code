/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.member;

import java.io.IOException;
import java.lang.reflect.Executable;

import net.sf.mmm.code.api.block.CodeBlockBody;
import net.sf.mmm.code.api.expression.CodeVariable;
import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.api.member.CodeField;
import net.sf.mmm.code.api.member.CodeOperation;
import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.base.arg.BaseExceptions;
import net.sf.mmm.code.base.arg.BaseParameter;
import net.sf.mmm.code.base.arg.BaseParameters;
import net.sf.mmm.code.base.block.BaseBlockBody;
import net.sf.mmm.code.base.element.BaseElementWithTypeVariables;
import net.sf.mmm.code.base.node.BaseFunction;
import net.sf.mmm.code.base.type.BaseTypeVariables;

/**
 * Base implementation of {@link CodeOperation}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BaseOperation extends BaseMember implements CodeOperation, BaseElementWithTypeVariables, BaseFunction {

  private final BaseTypeVariables typeVariables;

  private BaseParameters parameters;

  private BaseExceptions exceptions;

  private CodeBlockBody body;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param name the {@link #getName() name}.
   */
  public BaseOperation(BaseOperations<?> parent, String name) {

    super(parent, CodeModifiers.MODIFIERS_PUBLIC, name);
    this.typeVariables = new BaseTypeVariables(this);
    this.parameters = new BaseParameters(this);
    this.exceptions = new BaseExceptions(this);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param name the {@link #getName() name}.
   * @param typeVariables the {@link #getTypeParameters() type variables}.
   */
  protected BaseOperation(BaseOperations<?> parent, String name, BaseTypeVariables typeVariables) {

    super(parent, CodeModifiers.MODIFIERS_PUBLIC, name);
    typeVariables.setParent(this);
    typeVariables.setImmutable();
    this.typeVariables = typeVariables;
    this.parameters = new BaseParameters(this);
    this.exceptions = new BaseExceptions(this);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseOperation} to copy.
   */
  public BaseOperation(BaseOperation template) {

    super(template);
    this.typeVariables = template.typeVariables.copy(getDeclaringType());
    this.parameters = template.parameters.copy(this);
    this.exceptions = template.exceptions.copy(this);
  }

  @Override
  public abstract BaseOperations<?> getParent();

  @Override
  public CodeVariable getVariable(String name) {

    BaseParameter parameter = this.parameters.get(name);
    if (parameter != null) {
      return parameter;
    }
    CodeField javaField = getParent().getParent().getFields().get(name);
    return javaField;
  }

  @Override
  protected void doSetImmutable() {

    super.doSetImmutable();
    this.typeVariables.setImmutableIfNotSystemImmutable();
    this.parameters.setImmutableIfNotSystemImmutable();
    this.exceptions.setImmutableIfNotSystemImmutable();
  }

  @Override
  public BaseTypeVariables getTypeParameters() {

    return this.typeVariables;
  }

  @Override
  public BaseParameters getParameters() {

    return this.parameters;
  }

  @Override
  public BaseExceptions getExceptions() {

    return this.exceptions;
  }

  @Override
  public CodeBlockBody getBody() {

    if (this.body == null) {
      this.body = new BaseBlockBody(this);
    }
    return this.body;
  }

  @Override
  public void setBody(CodeBlockBody body) {

    verifyMutalbe();
    if (body.getParent() != this) {
      throw new IllegalArgumentException();
    }
    this.body = body;
  }

  @Override
  public abstract Executable getReflectiveObject();

  @Override
  public abstract BaseOperation getSourceCodeObject();

  @Override
  public abstract BaseOperation copy();

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    super.doWrite(sink, newline, defaultIndent, currentIndent, language);
    this.typeVariables.write(sink, newline, defaultIndent, currentIndent, language);
    doWriteSignature(sink, newline, defaultIndent, currentIndent, language);
    if ((this.body == null) || (defaultIndent == null)) {
      sink.append(language.getStatementTerminator());
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
   * @param defaultIndent the {@link String} used for indentation (e.g. a number of spaces to insert per
   *        indent level).
   * @param currentIndent the current indent (number of spaces). Initially the empty string ({@code ""}).
   *        Before a recursion the {@code indent} will be appended.
   * @param language the {@link CodeLanguage} to use.
   * @throws IOException if thrown by {@link Appendable}.
   */
  protected void doWriteSignature(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    sink.append(getName());
    doWriteParameters(sink, newline, defaultIndent, currentIndent, language);
    this.exceptions.write(sink, newline, null, null);
  }

  /**
   * Writes the {@link #getParameters() args}.
   *
   * @param sink the {@link Appendable}.
   * @param newline the newline {@link String}.
   * @param defaultIndent the {@link String} used for indentation (e.g. a number of spaces to insert per
   *        indent level).
   * @param currentIndent the current indent (number of spaces). Initially the empty string ({@code ""}).
   *        Before a recursion the {@code indent} will be appended.
   * @param language the {@link CodeLanguage} to use.
   * @throws IOException if thrown by {@link Appendable}.
   */
  protected void doWriteParameters(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    sink.append('(');
    this.parameters.write(sink, newline, null, null);
    sink.append(')');
  }

}
