/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.sf.mmm.code.api.comment.CodeComment;
import net.sf.mmm.code.api.type.CodeTypePlaceholder;
import net.sf.mmm.code.impl.java.JavaContext;
import net.sf.mmm.code.impl.java.JavaFile;
import net.sf.mmm.code.impl.java.annotation.JavaAnnotation;
import net.sf.mmm.code.impl.java.annotation.JavaAnnotations;
import net.sf.mmm.code.impl.java.element.JavaElementWithTypeVariables;
import net.sf.mmm.code.impl.java.type.JavaComposedType;
import net.sf.mmm.code.impl.java.type.JavaGenericType;
import net.sf.mmm.code.impl.java.type.JavaGenericTypeProxy;
import net.sf.mmm.code.impl.java.type.JavaParameterizedType;
import net.sf.mmm.code.impl.java.type.JavaType;
import net.sf.mmm.code.impl.java.type.JavaTypeParameters;
import net.sf.mmm.code.impl.java.type.JavaTypePlaceholder;
import net.sf.mmm.code.impl.java.type.JavaTypeProxy;
import net.sf.mmm.code.impl.java.type.JavaTypeVariable;
import net.sf.mmm.code.impl.java.type.JavaTypeWildcard;

/**
 * {@link JavaGenericTypeProxy} used to create types from source code with lazy evaluation. In Java types can
 * be referenced before they are actually declared (e.g. a nested class can be used in its parent class before
 * it is defined in the source code). Therefore instances of this class can be created whilst parsing the
 * source code and will be resolved via lazy initialization after the parsing has completed.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaGenericTypeFromSource extends JavaGenericTypeProxy {

  private final JavaElementWithTypeVariables parent;

  private final String name;

  private final JavaFile file;

  private JavaGenericType type;

  private List<JavaGenericType> composedTypes;

  private List<JavaGenericType> typeParameters;

  private JavaGenericType extendsBound;

  private JavaGenericType superBound;

  private int arrayCount;

  private String arrayLengthExpression;

  private boolean commentAndAnnotationsApplied;

  /**
   * The constructor.
   *
   * @param parent the (potential) {@link #getParent() parent}.
   * @param name the name of the type from the source code.
   * @param file the declaring {@link JavaFile}.
   */
  public JavaGenericTypeFromSource(JavaElementWithTypeVariables parent, String name, JavaFile file) {

    super();
    Objects.requireNonNull(parent, "parent");
    Objects.requireNonNull(name, "name");
    Objects.requireNonNull(file, "file");
    this.parent = parent;
    this.name = name;
    this.file = file;
  }

  /**
   * @return the {@link JavaGenericType} this proxy delegates to. May be initialized lazy by this method.
   */
  @Override
  public JavaGenericType getDelegate() {

    if (this.type == null) {
      this.type = toGenericType(null);
    }
    return this.type;
  }

  /**
   * @return the raw name from source code.
   */
  public String getName() {

    return this.name;
  }

  private JavaGenericType toGenericType(JavaTypePlaceholder placeholder) {

    if (CodeTypePlaceholder.NAME_WILDCARD.equals(this.name)) {
      return toWildcardType();
    }
    JavaGenericType genericType = toGenericTypeByName(this.name);
    if (this.composedTypes != null) {
      JavaComposedType composedType = new JavaComposedType(placeholder);
      composedType.add(genericType);
      for (JavaGenericType interfaceType : this.composedTypes) {
        composedType.add(resolve(interfaceType, placeholder));
      }
      applyCommentAndAnnotations(composedType);
      composedType.setImmutable();
      genericType = composedType;
    }
    if ((this.typeParameters != null) && !this.typeParameters.isEmpty()) {
      JavaType javaType = (JavaType) genericType;
      JavaParameterizedType parameterizedType = javaType.createParameterizedType(this.parent);
      JavaTypeParameters parameters = parameterizedType.getTypeParameters();
      for (JavaGenericType typeParam : this.typeParameters) {
        parameters.add(resolve(typeParam, placeholder));
      }
      applyCommentAndAnnotations(parameterizedType);
      parameterizedType.setImmutable();
      genericType = parameterizedType;
    }
    if (this.arrayCount > 0) {
      for (int i = 0; i < this.arrayCount; i++) {
        genericType = genericType.createArray();
      }
      genericType = applyCommentAndAnnotations(genericType);
    }
    return genericType;
  }

  private JavaGenericType toGenericTypeByName(String typeName) {

    JavaTypeVariable typeVariable = this.parent.getTypeParameters().get(typeName, true);
    if (typeVariable != null) {
      return typeVariable;
    }
    JavaContext context = this.parent.getContext();
    String qualifiedName = typeName;
    boolean qualified = typeName.indexOf('.') > 0;
    if (!qualified) {
      qualifiedName = context.getQualifiedName(typeName, this.file, false);
    }
    JavaType javaType = context.getRequiredType(qualifiedName);
    if (qualified) {
      JavaTypeProxy qualifiedType = new JavaTypeProxy(this.parent, javaType);
      qualifiedType.setQualified(true);
      applyCommentAndAnnotations(qualifiedType);
      qualifiedType.setImmutable();
      return qualifiedType;
    }
    return javaType;
  }

  private JavaGenericType applyCommentAndAnnotations(JavaGenericType genericType) {

    if (this.commentAndAnnotationsApplied) {
      return genericType;
    }
    JavaGenericType result = genericType;
    JavaAnnotations annotations = getAnnotations();
    if (annotations != null) {
      if (result instanceof JavaType) {
        result = new JavaTypeProxy(this.parent, (JavaType) genericType);
      }
      assert (result.getAnnotations().getDeclared().isEmpty());
      for (JavaAnnotation annotation : annotations) {
        result.getAnnotations().add(annotation);
      }
    }
    CodeComment comment = getComment();
    if (comment != null) {
      if (result instanceof JavaType) {
        result = new JavaTypeProxy(this.parent, (JavaType) genericType);
      }
      assert (result.getComment() == null);
      result.setComment(comment);
    }
    this.commentAndAnnotationsApplied = true;
    return result;
  }

  private static JavaGenericType resolve(JavaGenericType type, JavaTypePlaceholder placeholder) {

    if (type instanceof JavaGenericTypeFromSource) {
      return ((JavaGenericTypeFromSource) type).toGenericType(placeholder);
    }
    return type;
  }

  private JavaGenericType toWildcardType() {

    JavaTypeWildcard wildcard;
    JavaGenericType bound = null;
    if (this.extendsBound != null) {
      wildcard = new JavaTypeWildcard(this.parent, null, false);
      bound = resolve(this.extendsBound, wildcard);
    } else if (this.superBound != null) {
      wildcard = new JavaTypeWildcard(this.parent, null, true);
      bound = resolve(this.superBound, wildcard);
    } else {
      wildcard = this.parent.getContext().getUnboundedWildcard();
    }
    if (bound != null) {
      wildcard.setBound(bound);
      applyCommentAndAnnotations(wildcard);
      wildcard.setImmutable();
    }
    return wildcard;
  }

  /**
   * @return {@code true} if diamond operator ({@literal <>}) was present in source-code for this type
   *         reference, {@code false} otherwise. E.g. needed for constructor references.
   */
  public boolean isDiamonOperator() {

    return ((this.typeParameters != null) && (this.typeParameters.isEmpty()));
  }

  /**
   * Ensures that {@link #getTypeParameters() type parameters} are initialized.
   *
   * @see #isDiamonOperator()
   */
  public void ensureTypeParameters() {

    if (this.typeParameters == null) {
      this.typeParameters = new ArrayList<>();
    }
  }

  /**
   * @param typeParameter the type parameter to add.
   */
  public void addTypeParameter(JavaGenericType typeParameter) {

    ensureTypeParameters();
    this.typeParameters.add(typeParameter);
  }

  /**
   * @param composedType the composed type to add.
   */
  public void addComposedType(JavaGenericType composedType) {

    if (this.composedTypes == null) {
      this.composedTypes = new ArrayList<>();
    }
    this.composedTypes.add(composedType);
  }

  /**
   * @return the upper bound of a wildcard or {@code null}.
   */
  public JavaGenericType getExtendsBound() {

    return this.extendsBound;
  }

  /**
   * @param extendsBound the new value of {@link #getExtendsBound()}.
   */
  public void setExtendsBound(JavaGenericType extendsBound) {

    this.extendsBound = extendsBound;
  }

  /**
   * @return the lower bound of a wildcard or {@code null}.
   */
  public JavaGenericType getSuperBound() {

    return this.superBound;
  }

  /**
   * @param superBound the new value of {@link #getSuperBound()}.
   */
  public void setSuperBound(JavaGenericType superBound) {

    this.superBound = superBound;
  }

  /**
   * @return the number of {@link JavaGenericType#isArray() array} dimensions. Will be zero (0) for no array.
   */
  public int getArrayCount() {

    return this.arrayCount;
  }

  /**
   * @param arrayCount the new value of {@link #getArrayCount()}.
   */
  public void setArrayCount(int arrayCount) {

    this.arrayCount = arrayCount;
  }

  /**
   * Increments the {@link #getArrayCount() array count}.
   */
  public void incArrayCount() {

    this.arrayCount++;
  }

  /**
   * @return the length of the array (in case of an array initializer such as 1024 in {@code new byte[1024]}
   *         but may also be a variable, field reference, calculation expression, etc.) or {@code null} if not
   *         an array initializer.
   */
  public String getArrayLengthExpression() {

    return this.arrayLengthExpression;
  }

  /**
   * @param arrayLengthExpression the new value of {@link #getArrayLengthExpression()}.
   */
  public void setArrayLengthExpression(String arrayLengthExpression) {

    this.arrayLengthExpression = arrayLengthExpression;
  }

  @Override
  public JavaGenericType copy() {

    JavaGenericType delegate = getDelegate();
    if (delegate.isImmutable()) {
      delegate = delegate.copy();
    }
    return delegate;
  }

}
