/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.type;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.element.CodeElement;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeParameterizedType;
import net.sf.mmm.code.base.arg.BaseOperationArg;
import net.sf.mmm.code.base.element.BaseElementImpl;
import net.sf.mmm.code.base.member.BaseOperation;

/**
 * Base implementation of {@link CodeParameterizedType}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseParameterizedType extends BaseGenericType
    implements CodeParameterizedType, CodeNodeItemWithGenericParent<BaseElementImpl, BaseParameterizedType> {

  private static final Logger LOG = LoggerFactory.getLogger(BaseParameterizedType.class);

  private final BaseElementImpl parent;

  private final BaseTypeParameters typeVariables;

  private final ParameterizedType reflectiveObject;

  private BaseParameterizedType sourceCodeObject;

  private BaseType type;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param type the {@link #getType() type} that gets parameterized. Should have the same number of
   *        {@link BaseType#getTypeParameters() type variables} as the {@link #getTypeParameters() type
   *        parameters of this type} when initialized.
   */
  public BaseParameterizedType(BaseElementImpl parent, BaseType type) {

    this(parent, null, type);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May be {@code null}.
   */
  public BaseParameterizedType(BaseElementImpl parent, ParameterizedType reflectiveObject) {

    this(parent, reflectiveObject, null);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May be {@code null}.
   * @param type the {@link #getType() type} that gets parameterized. Should have the same number of
   *        {@link BaseType#getTypeParameters() type variables} as the {@link #getTypeParameters() type
   *        parameters of this type} when initialized.
   */
  public BaseParameterizedType(BaseElementImpl parent, ParameterizedType reflectiveObject, BaseType type) {

    super();
    this.parent = parent;
    this.typeVariables = new BaseTypeParameters(this);
    this.reflectiveObject = reflectiveObject;
    this.type = type;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseParameterizedType} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public BaseParameterizedType(BaseParameterizedType template, BaseElementImpl parent) {

    super(template);
    this.parent = parent;
    this.typeVariables = template.typeVariables.copy(this);
    this.reflectiveObject = null;
    this.type = template.type;
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
    getType();
  }

  @Override
  public BaseElementImpl getParent() {

    return this.parent;
  }

  @Override
  public BaseTypeParameters getTypeParameters() {

    return this.typeVariables;
  }

  @Override
  public ParameterizedType getReflectiveObject() {

    return this.reflectiveObject;
  }

  @Override
  public BaseParameterizedType getSourceCodeObject() {

    if ((this.sourceCodeObject == null) && !isInitialized()) {
      BaseElementImpl sourceElement = this.parent.getSourceCodeObject();
      if (sourceElement != null) {
        this.sourceCodeObject = null; // TODO
      }
    }
    return this.sourceCodeObject;
  }

  @Override
  public BaseType getDeclaringType() {

    return getType();
  }

  @Override
  public BaseOperation getDeclaringOperation() {

    return getDeclaringOperation(this.parent);
  }

  private BaseOperation getDeclaringOperation(CodeElement parentNode) {

    if (parentNode instanceof BaseOperationArg) {
      return ((BaseOperationArg) parentNode).getDeclaringOperation();
    } else if (parentNode instanceof BaseParameterizedType) {
      return ((BaseParameterizedType) parentNode).getDeclaringOperation();
    } else if (parentNode instanceof BaseArrayType) {
      return getDeclaringOperation(((BaseArrayType) parentNode).getParent());
    }
    return null;
  }

  @Override
  public BaseType asType() {

    return getType().asType();
  }

  @Override
  public BaseType getType() {

    if ((this.type == null) && (this.reflectiveObject != null)) {
      Class<?> rawClass = (Class<?>) this.reflectiveObject.getRawType();
      this.type = (BaseType) getContext().getType(rawClass);
    }
    return this.type;
  }

  @Override
  public void setType(CodeGenericType type) {

    verifyMutalbe();
    this.type = (BaseType) type;
  }

  @Override
  public BaseTypeVariable asTypeVariable() {

    return null;
  }

  @Override
  public BaseGenericType resolve(CodeGenericType context) {

    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public String getSimpleName() {

    return getType().getSimpleName();
  }

  @Override
  public String getQualifiedName() {

    return getType().getQualifiedName();
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    super.doWrite(sink, newline, null, "", language);
    writeReference(sink, true);
  }

  @Override
  public BaseParameterizedType copy() {

    return copy(this.parent);
  }

  @Override
  public BaseParameterizedType copy(BaseElementImpl newParent) {

    return new BaseParameterizedType(this, newParent);
  }

  @Override
  public void writeReference(Appendable sink, boolean declaration, Boolean qualified) throws IOException {

    BaseType myType = getType();
    if (myType == null) {
      LOG.warn("Parameterized type with undefined type declared in {}", getDeclaringType().getSimpleName());
      sink.append("Undefined");
    } else {
      myType.writeReference(sink, false, qualified);
      getTypeParameters().write(sink, DEFAULT_NEWLINE, null, "");
    }
  }

}
