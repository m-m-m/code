/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.type;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import net.sf.mmm.code.api.member.CodeOperation;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.type.CodeTypeVariable;

/**
 * Base implementation of {@link CodeTypeVariable}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseTypeVariable extends BaseTypePlaceholder implements CodeTypeVariable, CodeNodeItemWithGenericParent<BaseTypeVariables, BaseTypeVariable> {

  private final BaseTypeVariables parent;

  private final TypeVariable<?> reflectiveObject;

  private BaseTypeVariable sourceCodeObject;

  private String name;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param name the {@link #getName() name}.
   */
  public BaseTypeVariable(BaseTypeVariables parent, String name) {

    this(parent, name, null, null);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param name the {@link #getName() name}.
   * @param bound the {@link #getBound() bound}.
   */
  public BaseTypeVariable(BaseTypeVariables parent, String name, BaseGenericType bound) {

    this(parent, name, null, bound);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}.
   */
  public BaseTypeVariable(BaseTypeVariables parent, TypeVariable<?> reflectiveObject) {

    this(parent, reflectiveObject.getName(), reflectiveObject, null);
  }

  private BaseTypeVariable(BaseTypeVariables parent, String name, TypeVariable<?> reflectiveObject, BaseGenericType bound) {

    super(bound);
    this.parent = parent;
    this.reflectiveObject = reflectiveObject;
    this.name = name;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseTypeVariable} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public BaseTypeVariable(BaseTypeVariable template, BaseTypeVariables parent) {

    super(template);
    this.parent = parent;
    this.reflectiveObject = null;
    this.name = template.name;
  }

  @Override
  public BaseTypeVariables getParent() {

    return this.parent;
  }

  @Override
  public BaseTypeVariable asTypeVariable() {

    return this;
  }

  @Override
  public TypeVariable<?> getReflectiveObject() {

    return this.reflectiveObject;
  }

  @Override
  public BaseTypeVariable getSourceCodeObject() {

    if ((this.sourceCodeObject == null) && !isInitialized()) {
      BaseTypeVariables sourceTypeVariables = this.parent.getSourceCodeObject();
      if (sourceTypeVariables != null) {
        this.sourceCodeObject = sourceTypeVariables.get(this.name);
      }
    }
    return this.sourceCodeObject;
  }

  @Override
  protected Type[] getReflectiveBounds() {

    if (this.reflectiveObject == null) {
      return null;
    }
    return this.reflectiveObject.getBounds();
  }

  @Override
  public String getName() {

    return this.name;
  }

  @Override
  public void setName(String name) {

    verifyMutalbe();
    this.parent.rename(this, this.name, name, this::doSetName);
  }

  private void doSetName(String newName) {

    this.name = newName;
  }

  @Override
  public BaseType getDeclaringType() {

    return this.parent.getDeclaringType();
  }

  @Override
  public CodeOperation getDeclaringOperation() {

    return this.parent.getDeclaringOperation();
  }

  @Override
  public final boolean isExtends() {

    return true;
  }

  @Override
  public final boolean isSuper() {

    return false;
  }

  @Override
  public final boolean isWildcard() {

    return false;
  }

  @Override
  public String getSimpleName() {

    return this.name;
  }

  @Override
  public String getQualifiedName() {

    return this.name;
  }

  @Override
  public BaseTypeVariable copy() {

    return copy(this.parent);
  }

  @Override
  public BaseTypeVariable copy(BaseTypeVariables newParent) {

    return new BaseTypeVariable(this, newParent);
  }

}
