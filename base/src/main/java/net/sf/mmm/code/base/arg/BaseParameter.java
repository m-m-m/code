/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.arg;

import java.io.IOException;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.function.Consumer;

import net.sf.mmm.code.api.arg.CodeParameter;
import net.sf.mmm.code.api.arg.CodeParameters;
import net.sf.mmm.code.api.copy.CodeCopyMapper;
import net.sf.mmm.code.api.copy.CodeCopyType;
import net.sf.mmm.code.api.expression.CodeLiteral;
import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.api.merge.CodeMergeStrategy;
import net.sf.mmm.code.base.member.BaseOperation;
import net.sf.mmm.code.base.type.BaseType;

/**
 * Base implementation of {@link CodeParameter}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseParameter extends BaseOperationArg implements CodeParameter {

  private final BaseParameters parent;

  private final Parameter reflectiveObject;

  private String name;

  private boolean varArgs;

  private CodeParameter sourceCodeObject;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param name the {@link #getName() name}.
   */
  public BaseParameter(BaseParameters parent, String name) {

    this(parent, name, null, null);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May be {@code null}.
   */
  public BaseParameter(BaseParameters parent, Parameter reflectiveObject) {

    this(parent, reflectiveObject.getName(), reflectiveObject, null);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param name the {@link #getName() name}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May be {@code null}.
   */
  BaseParameter(BaseParameters parent, String name, Parameter reflectiveObject, CodeParameter sourceCodeObject) {

    super();
    this.parent = parent;
    this.reflectiveObject = reflectiveObject;
    this.name = getLanguage().verifyName(this, name);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseParameter} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseParameter(BaseParameter template, CodeCopyMapper mapper) {

    super(template, mapper);
    this.parent = mapper.map(template.parent, CodeCopyType.PARENT);
    this.name = template.name;
    this.reflectiveObject = null;
  }

  @Override
  public BaseParameters getParent() {

    return this.parent;
  }

  @Override
  public BaseOperation getDeclaringOperation() {

    return this.parent.getParent();
  }

  @Override
  public BaseType getDeclaringType() {

    return getParent().getDeclaringType();
  }

  @Override
  public String getName() {

    return this.name;
  }

  @Override
  public void setName(String name) {

    verifyMutalbe();
    if (this.name.equals(name)) {
      return;
    }
    Consumer<String> renamer = this::doSetName;
    getParent().rename(this, this.name, name, renamer);
  }

  private void doSetName(String newName) {

    this.name = getLanguage().verifyName(this, newName);
  }

  @Override
  public boolean isVarArgs() {

    return this.varArgs;
  }

  @Override
  public void setVarArgs(boolean varArgs) {

    verifyMutalbe();
    this.varArgs = varArgs;
  }

  @Override
  public Parameter getReflectiveObject() {

    return this.reflectiveObject;
  }

  @Override
  protected Type getReflectiveObjectType() {

    if (this.reflectiveObject != null) {
      return this.reflectiveObject.getParameterizedType();
    }
    return null;
  }

  @Override
  public CodeParameter getSourceCodeObject() {

    if ((this.sourceCodeObject == null) && !isInitialized()) {
      CodeParameters sourceParameters = this.parent.getSourceCodeObject();
      if (sourceParameters != null) {
        CodeParameter sourceParameter = sourceParameters.get(this.name);
        if ((sourceParameter != null) && getType().getQualifiedName().equals(sourceParameter.getType().getQualifiedName())) {
          this.sourceCodeObject = sourceParameter;
        }
      }
    }
    return this.sourceCodeObject;
  }

  @Override
  public CodeLiteral evaluate() {

    return null;
  }

  @Override
  public CodeParameter merge(CodeParameter other, CodeMergeStrategy strategy) {

    doMerge(other, strategy);
    if ((strategy == CodeMergeStrategy.OVERRIDE) || (strategy == CodeMergeStrategy.MERGE_OVERRIDE_BODY)) {
      setName(other.getName());
      this.varArgs = other.isVarArgs();
    }
    return this;
  }

  @Override
  public BaseParameter copy() {

    return copy(getDefaultCopyMapper());
  }

  @Override
  public BaseParameter copy(CodeCopyMapper mapper) {

    return new BaseParameter(this, mapper);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    super.doWrite(sink, newline, defaultIndent, currentIndent, language);
  }

  @Override
  public void writeReference(Appendable sink, boolean declaration, Boolean qualified) throws IOException {

    CodeParameter.super.writeReference(sink, declaration, qualified);
    if (isVarArgs()) {
      sink.append("...");
    }
  }

  @Override
  protected void doWriteDeclaration(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    language.writeDeclaration(this, sink);
  }

}
