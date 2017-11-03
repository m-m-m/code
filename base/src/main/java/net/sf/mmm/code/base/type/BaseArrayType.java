/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.type;

import java.io.IOException;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.Objects;

import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.base.element.BaseElementImpl;
import net.sf.mmm.util.exception.api.IllegalCaseException;

/**
 * Base implementation of {@link BaseGenericType} for {@link #isArray() array}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseArrayType extends BaseGenericType implements CodeNodeItemWithGenericParent<BaseElementImpl, BaseArrayType> {

  private final BaseElementImpl parent;

  private final Type reflectiveObject;

  private BaseGenericType componentType;

  /**
   * The constructor.
   *
   * @param componentType the {@link #getComponentType() component type} to also use as {@link #getParent()
   *        parent}.
   */
  public BaseArrayType(BaseGenericType componentType) {

    this(componentType, null, componentType);
    Objects.requireNonNull(componentType, "componentType");
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May be {@code null}.
   */
  public BaseArrayType(BaseElementImpl parent, Type reflectiveObject) {

    this(parent, reflectiveObject, null);
    Objects.requireNonNull(reflectiveObject, "reflectiveObject");
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param componentType the {@link #getComponentType() component type}.
   */
  public BaseArrayType(BaseElementImpl parent, BaseGenericType componentType) {

    this(parent, null, componentType);
    Objects.requireNonNull(componentType, "componentType");
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May be {@code null}.
   * @param componentType the {@link #getComponentType() component type}.
   */
  private BaseArrayType(BaseElementImpl parent, Type reflectiveObject, BaseGenericType componentType) {

    super();
    this.parent = parent;
    this.reflectiveObject = reflectiveObject;
    this.componentType = componentType;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseArrayType} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public BaseArrayType(BaseArrayType template, BaseElementImpl parent) {

    super(template);
    this.parent = parent;
    this.componentType = template.getComponentType();
    this.reflectiveObject = null;
  }

  @Override
  public BaseElementImpl getParent() {

    return this.parent;
  }

  @Override
  public final BaseGenericType getComponentType() {

    if (this.componentType == null) {
      if (this.reflectiveObject instanceof GenericArrayType) {
        Type type = ((GenericArrayType) this.reflectiveObject).getGenericComponentType();
        this.componentType = getContext().getType(type, this.parent);
      } else if (this.reflectiveObject instanceof Class) {
        this.componentType = getContext().getType(((Class<?>) this.reflectiveObject).getComponentType());
      } else {
        throw new IllegalCaseException(this.reflectiveObject.getClass().getSimpleName());
      }
    }
    return this.componentType;
  }

  @Override
  public String getQualifiedName() {

    return getComponentType().getQualifiedName() + "[]";
  }

  @Override
  public String getSimpleName() {

    return getComponentType().getSimpleName() + "[]";
  }

  @Override
  public final boolean isArray() {

    return true;
  }

  @Override
  public BaseType asType() {

    return getContext().getRootType();
  }

  /**
   * @return the {@link Class#isArray() array} {@link Class} or {@link GenericArrayType}.
   */
  @Override
  public Type getReflectiveObject() {

    return this.reflectiveObject;
  }

  @Override
  public BaseGenericType resolve(CodeGenericType context) {

    BaseGenericType resolvedType = this.componentType.resolve(context);
    if (resolvedType == this.componentType) {
      return this;
    }
    return new BaseArrayType(resolvedType, resolvedType);
  }

  @Override
  public BaseType getDeclaringType() {

    return null;
  }

  @Override
  public void writeReference(Appendable sink, boolean declaration, Boolean qualified) throws IOException {

    boolean withDeclaration = declaration;
    if (this.componentType instanceof BaseType) {
      withDeclaration = false;
    }
    this.componentType.writeReference(sink, withDeclaration, qualified);
    sink.append("[]");
  }

  @Override
  public BaseArrayType copy() {

    return copy(this.parent);
  }

  @Override
  public BaseArrayType copy(BaseElementImpl newParent) {

    return new BaseArrayType(this, newParent);
  }

}
