/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.type;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.copy.CodeCopyType;
import io.github.mmm.code.api.member.CodeOperation;
import io.github.mmm.code.api.type.CodeTypeVariable;
import io.github.mmm.code.api.type.CodeTypeVariables;

/**
 * Base implementation of {@link CodeTypeVariable}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseTypeVariable extends BaseTypePlaceholder implements CodeTypeVariable {

  private final BaseTypeVariables parent;

  private final TypeVariable<?> reflectiveObject;

  private CodeTypeVariable sourceCodeObject;

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
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseTypeVariable(BaseTypeVariable template, CodeCopyMapper mapper) {

    super(template, mapper);
    this.parent = mapper.map(template.parent, CodeCopyType.PARENT);
    this.reflectiveObject = null;
    this.name = template.name;
  }

  @Override
  public CodeTypeVariables getParent() {

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
  public CodeTypeVariable getSourceCodeObject() {

    if ((this.sourceCodeObject == null) && !isInitialized()) {
      CodeTypeVariables sourceTypeVariables = this.parent.getSourceCodeObject();
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

    return copy(getDefaultCopyMapper());
  }

  @Override
  public BaseTypeVariable copy(CodeCopyMapper mapper) {

    return new BaseTypeVariable(this, mapper);
  }

}
