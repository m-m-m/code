/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.mmm.code.api.annotation.CodeAnnotation;
import io.github.mmm.code.api.annotation.CodeAnnotations;
import io.github.mmm.code.api.comment.CodeComment;
import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.element.CodeElementWithTypeVariables;
import io.github.mmm.code.api.type.CodeGenericType;
import io.github.mmm.code.api.type.CodeTypePlaceholder;
import io.github.mmm.code.api.type.CodeTypeWildcard;
import io.github.mmm.code.base.BaseContext;
import io.github.mmm.code.base.BaseFile;
import io.github.mmm.code.base.type.BaseComposedType;
import io.github.mmm.code.base.type.BaseGenericType;
import io.github.mmm.code.base.type.BaseGenericTypeProxy;
import io.github.mmm.code.base.type.BaseParameterizedType;
import io.github.mmm.code.base.type.BaseType;
import io.github.mmm.code.base.type.BaseTypeParameters;
import io.github.mmm.code.base.type.BaseTypeProxy;
import io.github.mmm.code.base.type.BaseTypeVariable;
import io.github.mmm.code.base.type.BaseTypeWildcard;

/**
 * {@link BaseGenericTypeProxy} used to create types from source code with lazy evaluation. In Java types can be
 * referenced before they are actually declared (e.g. a nested class can be used in its parent class before it is
 * defined in the source code). Therefore instances of this class can be created whilst parsing the source code and will
 * be resolved via lazy initialization after the parsing has completed.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaGenericTypeFromSource extends BaseGenericTypeProxy {

  private final CodeElementWithTypeVariables parent;

  private final String name;

  private final BaseFile file;

  private BaseGenericType type;

  private List<BaseGenericType> composedTypes;

  private List<BaseGenericType> typeParameters;

  private BaseGenericType extendsBound;

  private BaseGenericType superBound;

  private int arrayCount;

  private String arrayLengthExpression;

  private boolean commentAndAnnotationsApplied;

  /**
   * The constructor.
   *
   * @param parent the (potential) {@link #getParent() parent}.
   * @param name the name of the type from the source code.
   * @param file the declaring {@link BaseFile}.
   */
  public JavaGenericTypeFromSource(CodeElementWithTypeVariables parent, String name, BaseFile file) {

    super();
    Objects.requireNonNull(parent, "parent");
    Objects.requireNonNull(name, "name");
    Objects.requireNonNull(file, "file");
    this.parent = parent;
    this.name = name;
    this.file = file;
  }

  @Override
  public BaseGenericType getDelegate() {

    if (this.type == null) {
      this.type = (BaseGenericType) toGenericType(null);
    }
    return this.type;
  }

  /**
   * @return the raw name from source code.
   */
  public String getName() {

    return this.name;
  }

  private CodeGenericType toGenericType(CodeTypePlaceholder placeholder) {

    if (CodeTypePlaceholder.NAME_WILDCARD.equals(this.name)) {
      return toWildcardType();
    }
    CodeGenericType genericType = toGenericTypeByName(this.name);
    if (this.composedTypes != null) {
      BaseComposedType composedType = new BaseComposedType(placeholder);
      composedType.add(genericType);
      for (BaseGenericType interfaceType : this.composedTypes) {
        composedType.add(resolve(interfaceType, placeholder));
      }
      applyCommentAndAnnotations(composedType);
      composedType.setImmutable();
      genericType = composedType;
    }
    if ((this.typeParameters != null) && !this.typeParameters.isEmpty()) {
      BaseType javaType = (BaseType) genericType;
      BaseParameterizedType parameterizedType = javaType.createParameterizedType(this.parent);
      BaseTypeParameters parameters = parameterizedType.getTypeParameters();
      for (BaseGenericType typeParam : this.typeParameters) {
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

  private CodeGenericType toGenericTypeByName(String typeName) {

    BaseTypeVariable typeVariable = (BaseTypeVariable) this.parent.getTypeParameters().get(typeName, true);
    if (typeVariable != null) {
      return typeVariable;
    }
    BaseContext context = (BaseContext) this.parent.getContext();
    String qualifiedName = typeName;
    boolean qualified = typeName.indexOf('.') > 0;
    if (!qualified) {
      qualifiedName = context.getQualifiedName(typeName, this.file, false);
    }
    BaseType baseType = context.getOrCreateType(qualifiedName, false);
    if (qualified) {
      BaseTypeProxy qualifiedType = new BaseTypeProxy(this.parent, baseType);
      qualifiedType.setQualified(true);
      applyCommentAndAnnotations(qualifiedType);
      qualifiedType.setImmutable();
      return qualifiedType;
    }
    return baseType;
  }

  private CodeGenericType applyCommentAndAnnotations(CodeGenericType genericType) {

    if (this.commentAndAnnotationsApplied) {
      return genericType;
    }
    CodeGenericType result = genericType;
    CodeAnnotations annotations = getAnnotations(false);
    if (annotations != null) {
      if (result instanceof BaseType) {
        result = new BaseTypeProxy(this.parent, (BaseType) genericType);
      }
      assert (result.getAnnotations().getDeclared().isEmpty());
      for (CodeAnnotation annotation : annotations) {
        result.getAnnotations().add(annotation);
      }
    }
    CodeComment comment = getComment(false);
    if (comment != null) {
      if (result instanceof BaseType) {
        result = new BaseTypeProxy(this.parent, (BaseType) genericType);
      }
      assert (result.getComment() == null);
      result.setComment(comment);
    }
    this.commentAndAnnotationsApplied = true;
    return result;
  }

  private static CodeGenericType resolve(CodeGenericType type, CodeTypePlaceholder placeholder) {

    if (type instanceof JavaGenericTypeFromSource) {
      return ((JavaGenericTypeFromSource) type).toGenericType(placeholder);
    }
    return type;
  }

  private CodeGenericType toWildcardType() {

    CodeTypeWildcard wildcard;
    CodeGenericType bound = null;
    if (this.extendsBound != null) {
      wildcard = new BaseTypeWildcard(this.parent, null, false);
      bound = resolve(this.extendsBound, wildcard);
    } else if (this.superBound != null) {
      wildcard = new BaseTypeWildcard(this.parent, null, true);
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
   * @return {@code true} if diamond operator ({@literal <>}) was present in source-code for this type reference,
   *         {@code false} otherwise. E.g. needed for constructor references.
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
  public void addTypeParameter(BaseGenericType typeParameter) {

    ensureTypeParameters();
    this.typeParameters.add(typeParameter);
  }

  /**
   * @param composedType the composed type to add.
   */
  public void addComposedType(BaseGenericType composedType) {

    if (this.composedTypes == null) {
      this.composedTypes = new ArrayList<>();
    }
    this.composedTypes.add(composedType);
  }

  /**
   * @return the upper bound of a wildcard or {@code null}.
   */
  public BaseGenericType getExtendsBound() {

    return this.extendsBound;
  }

  /**
   * @param extendsBound the new value of {@link #getExtendsBound()}.
   */
  public void setExtendsBound(BaseGenericType extendsBound) {

    this.extendsBound = extendsBound;
  }

  /**
   * @return the lower bound of a wildcard or {@code null}.
   */
  public BaseGenericType getSuperBound() {

    return this.superBound;
  }

  /**
   * @param superBound the new value of {@link #getSuperBound()}.
   */
  public void setSuperBound(BaseGenericType superBound) {

    this.superBound = superBound;
  }

  /**
   * @return the number of {@link BaseGenericType#isArray() array} dimensions. Will be zero (0) for no array.
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
   * @return the length of the array (in case of an array initializer such as 1024 in {@code new byte[1024]} but may
   *         also be a variable, field reference, calculation expression, etc.) or {@code null} if not an array
   *         initializer.
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
  public BaseGenericType copy() {

    BaseGenericType delegate = getDelegate();
    if (delegate.isImmutable()) {
      delegate = delegate.copy();
    }
    return delegate;
  }

  @Override
  public BaseGenericType copy(CodeCopyMapper mapper) {

    BaseGenericType delegate = getDelegate();
    if (delegate.isImmutable()) {
      delegate = delegate.copy(mapper);
    }
    return delegate;
  }

}
