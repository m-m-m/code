/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.type;

import java.io.IOException;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.Objects;

import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.impl.java.element.JavaElement;
import net.sf.mmm.code.impl.java.item.JavaReflectiveObject;
import net.sf.mmm.util.exception.api.IllegalCaseException;

/**
 * Implementation of {@link JavaGenericType} for {@link #isArray() array} in Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaArrayType extends JavaGenericType implements CodeNodeItemWithGenericParent<JavaElement, JavaArrayType>, JavaReflectiveObject<Type> {

  private final JavaElement parent;

  private final Type reflectiveObject;

  private JavaGenericType componentType;

  /**
   * The constructor.
   *
   * @param componentType the {@link #getComponentType() component type} to also use as {@link #getParent()
   *        parent}.
   */
  public JavaArrayType(JavaGenericType componentType) {

    this(componentType, null, componentType);
    Objects.requireNonNull(componentType, "componentType");
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May be {@code null}.
   */
  public JavaArrayType(JavaElement parent, Type reflectiveObject) {

    this(parent, reflectiveObject, null);
    Objects.requireNonNull(reflectiveObject, "reflectiveObject");
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param componentType the {@link #getComponentType() component type}.
   */
  public JavaArrayType(JavaElement parent, JavaGenericType componentType) {

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
  private JavaArrayType(JavaElement parent, Type reflectiveObject, JavaGenericType componentType) {

    super();
    this.parent = parent;
    this.reflectiveObject = reflectiveObject;
    this.componentType = componentType;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaArrayType} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaArrayType(JavaArrayType template, JavaElement parent) {

    super(template);
    this.parent = parent;
    this.componentType = template.getComponentType();
    this.reflectiveObject = null;
  }

  @Override
  public JavaElement getParent() {

    return this.parent;
  }

  @Override
  public final JavaGenericType getComponentType() {

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
  public final boolean isArray() {

    return true;
  }

  @Override
  public JavaType asType() {

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
  public JavaGenericType resolve(CodeGenericType context) {

    JavaGenericType resolvedType = this.componentType.resolve(context);
    if (resolvedType == this.componentType) {
      return this;
    }
    return new JavaArrayType(resolvedType, resolvedType);
  }

  @Override
  public JavaType getDeclaringType() {

    return null;
  }

  @Override
  public void writeReference(Appendable sink, boolean declaration) throws IOException {

    this.componentType.writeReference(sink, false);
    sink.append("[]");
  }

  @Override
  public JavaArrayType copy() {

    return copy(this.parent);
  }

  @Override
  public JavaArrayType copy(JavaElement newParent) {

    return new JavaArrayType(this, newParent);
  }

}
