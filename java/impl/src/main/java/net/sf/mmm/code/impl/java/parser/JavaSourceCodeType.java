/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.parser;

import java.util.ArrayList;
import java.util.List;

import net.sf.mmm.code.api.comment.CodeComment;
import net.sf.mmm.code.api.type.CodeTypePlaceholder;
import net.sf.mmm.code.impl.java.JavaContext;
import net.sf.mmm.code.impl.java.JavaFile;
import net.sf.mmm.code.impl.java.annotation.JavaAnnotation;
import net.sf.mmm.code.impl.java.element.JavaElementWithTypeVariables;
import net.sf.mmm.code.impl.java.type.JavaComposedType;
import net.sf.mmm.code.impl.java.type.JavaGenericType;
import net.sf.mmm.code.impl.java.type.JavaParameterizedType;
import net.sf.mmm.code.impl.java.type.JavaTypeProxy;
import net.sf.mmm.code.impl.java.type.JavaType;
import net.sf.mmm.code.impl.java.type.JavaTypeParameters;
import net.sf.mmm.code.impl.java.type.JavaTypePlaceholder;
import net.sf.mmm.code.impl.java.type.JavaTypeVariable;
import net.sf.mmm.code.impl.java.type.JavaTypeWildcard;

/**
 * Container with the structured data from source-code referencing a {@link JavaGenericType}. Needed because
 * types may be used in source-code before they are declared. Therefore first an instance of this container is
 * created and {@link #toGenericType(JavaFile, JavaElementWithTypeVariables) converted} later.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaSourceCodeType {

  private final String name;

  private List<JavaSourceCodeType> composedTypes;

  private List<JavaSourceCodeType> typeParameters;

  private JavaSourceCodeType extendsBound;

  private JavaSourceCodeType superBound;

  private int arrayCount;

  private List<JavaAnnotation> annotations;

  private CodeComment comment;

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   */
  public JavaSourceCodeType(String name) {

    super();
    this.name = name;
  }

  /**
   * @return the name from the source code. May be {@link JavaGenericType#getSimpleName() simple name},
   *         {@link JavaGenericType#getQualifiedName() qualified name}, or
   *         {@link JavaTypePlaceholder#getName() variable name}.
   */
  public String getName() {

    return this.name;
  }

  /**
   * @return the {@link List} of {@link JavaSourceCodeType}s that act as
   */
  public List<JavaSourceCodeType> getTypeParameters() {

    return this.typeParameters;
  }

  /**
   * @param typeParameters the new value of {@link #getTypeParameters()}.
   */
  public void setTypeParameters(List<JavaSourceCodeType> typeParameters) {

    this.typeParameters = typeParameters;
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
   * @param type the type parameter to add.
   */
  public void addTypeParameter(JavaSourceCodeType type) {

    ensureTypeParameters();
    this.typeParameters.add(type);
  }

  /**
   * @param type the composed type to add.
   */
  public void addComposedType(JavaSourceCodeType type) {

    if (this.composedTypes == null) {
      this.composedTypes = new ArrayList<>();
    }
    this.composedTypes.add(type);
  }

  /**
   * @return the upper bound of a wildcard or {@code null}.
   */
  public JavaSourceCodeType getExtendsBound() {

    return this.extendsBound;
  }

  /**
   * @param extendsBound the new value of {@link #getExtendsBound()}.
   */
  public void setExtendsType(JavaSourceCodeType extendsBound) {

    this.extendsBound = extendsBound;
  }

  /**
   * @return the lower bound of a wildcard or {@code null}.
   */
  public JavaSourceCodeType getSuperBound() {

    return this.superBound;
  }

  /**
   * @param superBound the new value of {@link #getSuperBound()}.
   */
  public void setSuperType(JavaSourceCodeType superBound) {

    this.superBound = superBound;
  }

  /**
   * @return the {@link List} of {@link JavaAnnotation}s. May be {@code null}.
   */
  public List<JavaAnnotation> getAnnotations() {

    return this.annotations;
  }

  /**
   * @param annotations the new value of {@link #getAnnotations()}.
   */
  public void setAnnotations(List<JavaAnnotation> annotations) {

    this.annotations = annotations;
  }

  /**
   * @return the optional {@link CodeComment} for this type. May be {@code null}.
   */
  public CodeComment getComment() {

    return this.comment;
  }

  /**
   * @param comment the new value of {@link #getComment()}.
   */
  public void setComment(CodeComment comment) {

    this.comment = comment;
  }

  /**
   * @return {@code true} if diamond operator ({@literal <>}) was present in source-code for this type
   *         reference, {@code false} otherwise. E.g. needed for constructor references.
   */
  public boolean isDiamonOperator() {

    return ((this.typeParameters != null) && (this.typeParameters.isEmpty()));
  }

  /**
   * @param file the owning {@link JavaFile}.
   * @param element the declaring {@link JavaElementWithTypeVariables}.
   * @return the resolved {@link JavaGenericType}.
   */
  public JavaGenericType toGenericType(JavaFile file, JavaElementWithTypeVariables element) {

    return toGenericType(file, element, null);
  }

  private JavaGenericType toGenericType(JavaFile file, JavaElementWithTypeVariables element, JavaTypePlaceholder typePlaceholder) {

    JavaContext context = element.getContext();
    if (CodeTypePlaceholder.NAME_WILDCARD.equals(this.name)) {
      return toWildcardType(file, element, context);
    }
    JavaGenericType type = toGenericTypeByName(file, element, this.name);
    if (this.composedTypes != null) {
      JavaComposedType composedType = new JavaComposedType(typePlaceholder);
      composedType.add(type);
      for (JavaSourceCodeType interfaceType : this.composedTypes) {
        composedType.add(interfaceType.toGenericType(file, element, typePlaceholder));
      }
      composedType.setImmutable();
      type = composedType;
    }
    if ((this.typeParameters != null) && !this.typeParameters.isEmpty()) {
      JavaType javaType = (JavaType) type;
      JavaParameterizedType parameterizedType = javaType.createParameterizedType(element);
      JavaTypeParameters parameters = parameterizedType.getTypeParameters();
      for (JavaSourceCodeType typeParam : this.typeParameters) {
        parameters.add(typeParam.toGenericType(file, element, typePlaceholder));
      }
      parameterizedType.setImmutable();
      type = parameterizedType;
    }
    for (int i = 0; i < this.arrayCount; i++) {
      type = type.createArray();
    }
    return type;
  }

  private JavaGenericType toWildcardType(JavaFile file, JavaElementWithTypeVariables element, JavaContext context) {

    JavaTypeWildcard wildcard;
    JavaGenericType bound = null;
    if (this.extendsBound != null) {
      wildcard = new JavaTypeWildcard(element, null, false);
      bound = this.extendsBound.toGenericType(file, element, wildcard);
    } else if (this.superBound != null) {
      wildcard = new JavaTypeWildcard(element, null, true);
      bound = this.superBound.toGenericType(file, element, wildcard);
    } else {
      wildcard = context.getUnboundedWildcard();
    }
    if (bound != null) {
      wildcard.setBound(bound);
      wildcard.setImmutable();
    }
    return wildcard;
  }

  private JavaGenericType toGenericTypeByName(JavaFile file, JavaElementWithTypeVariables element, String typeName) {

    JavaTypeVariable typeVariable = element.getTypeParameters().get(typeName, true);
    if (typeVariable != null) {
      return typeVariable;
    }

    JavaContext context = element.getContext();

    String qualifiedName = typeName;
    boolean qualified = typeName.indexOf('.') > 0;
    if (!qualified) {
      qualifiedName = context.getQualifiedName(typeName, file, false);
    }
    JavaType type = context.getRequiredType(qualifiedName);
    if (qualified) {
      JavaTypeProxy qualifiedType = new JavaTypeProxy(element, type);
      applyCommentAndAnnotations(qualifiedType);
      qualifiedType.setImmutable();
      return qualifiedType;
    }
    return type;

  }

  private void applyCommentAndAnnotations(JavaGenericType qualifiedType) {

    if (this.annotations != null) {
      for (JavaAnnotation annotation : this.annotations) {
        qualifiedType.getAnnotations().add(annotation);
      }
      this.annotations = null;
    }
    if (this.comment != null) {
      qualifiedType.setComment(this.comment);
      this.comment = null;
    }
  }

}
