/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.arg;

import java.io.IOException;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

import net.sf.mmm.code.api.arg.CodeParameter;
import net.sf.mmm.code.api.expression.CodeLiteral;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.base.member.BaseOperation;
import net.sf.mmm.code.base.type.BaseGenericType;
import net.sf.mmm.code.base.type.BaseType;

/**
 * Base implementation of {@link CodeParameter}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseParameter extends BaseOperationArg implements CodeParameter, CodeNodeItemWithGenericParent<BaseParameters, BaseParameter> {

  private final BaseParameters parent;

  private final Parameter reflectiveObject;

  private String name;

  private boolean varArgs;

  private BaseParameter sourceCodeObject;

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
  BaseParameter(BaseParameters parent, String name, Parameter reflectiveObject, BaseParameter sourceCodeObject) {

    super();
    this.parent = parent;
    this.reflectiveObject = reflectiveObject;
    this.name = name;
    verifyName(name, NAME_PATTERN);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseParameter} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public BaseParameter(BaseParameter template, BaseParameters parent) {

    super(template);
    this.parent = parent;
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
    this.name = name;
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
  public BaseParameter getSourceCodeObject() {

    if ((this.sourceCodeObject == null) && !isInitialized()) {
      BaseParameters sourceParameters = this.parent.getSourceCodeObject();
      if (sourceParameters != null) {
        BaseParameter sourceParameter = sourceParameters.get(this.name);
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
  public BaseParameter copy() {

    return copy(this.parent);
  }

  @Override
  public BaseParameter copy(BaseParameters newParent) {

    return new BaseParameter(this, newParent);
  }

  @Override
  protected void doWriteType(Appendable sink) throws IOException {

    if (this.varArgs) {
      BaseGenericType type = getType();
      BaseGenericType componentType = type.getComponentType();
      if (componentType != null) {
        componentType.writeReference(sink, false);
        sink.append("...");
      }
    }
    super.doWriteType(sink);
  }

}
