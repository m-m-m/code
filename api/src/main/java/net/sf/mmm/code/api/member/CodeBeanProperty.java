/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.member;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.CodeAnnotation;
import net.sf.mmm.code.api.CodeGenericType;
import net.sf.mmm.code.api.CodeType;
import net.sf.mmm.code.api.arg.CodeParameter;
import net.sf.mmm.code.api.doc.CodeDoc;
import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.api.statement.CodeComment;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * Implementation of {@link CodeProperty} for a virtual property composed out of a pair of a getter and a
 * setter.
 *
 * @see #of(CodeMethod)
 * @see #join(CodeBeanProperty)
 *
 * @author hohwille
 * @since 1.0.0
 */
public class CodeBeanProperty implements CodeProperty {

  private static final Logger LOG = LoggerFactory.getLogger(CodeBeanProperty.class);

  private final CodeMethod getter;

  private final CodeMethod setter;

  private final String name;

  private final CodeGenericType type;

  private final CodeType declaringType;

  /**
   * The constructor.
   *
   * @param getter the {@link #getGetter() getter}.
   * @param setter the {@link #getSetter() setter}.
   */
  private CodeBeanProperty(CodeType declaringType, CodeMethod getter, CodeMethod setter, String name, CodeGenericType type) {

    super();
    Objects.requireNonNull(declaringType, "declaringType");
    this.declaringType = declaringType;
    if (getter == null) {
      Objects.requireNonNull(setter, "getter||setter");
    }
    this.getter = getter;
    this.setter = setter;
    this.name = name;
    this.type = type;
  }

  /**
   * @return the getter. May be {@code null}.
   */
  public CodeMethod getGetter() {

    return this.getter;
  }

  /**
   * @return the setter. May be {@code null}.
   */
  public CodeMethod getSetter() {

    return this.setter;
  }

  @Override
  public CodeGenericType getType() {

    return this.type;
  }

  @Override
  public void setType(CodeGenericType type) {

    throw new ReadOnlyException(getClass().getSimpleName(), "type");
  }

  @Override
  public CodeComment getComment() {

    return null;
  }

  @Override
  public void setComment(CodeComment comment) {

    throw new ReadOnlyException(getClass().getSimpleName(), "comment");
  }

  @Override
  public CodeType getDeclaringType() {

    return this.declaringType;
  }

  @Override
  public CodeModifiers getModifiers() {

    if (this.getter != null) {
      return this.getter.getModifiers();
    }
    return this.setter.getModifiers();
  }

  @Override
  public void setModifiers(CodeModifiers modifiers) {

    throw new ReadOnlyException(getClass().getSimpleName(), "modifiers");
  }

  @Override
  public String getName() {

    return this.name;
  }

  @Override
  public void setName(String name) {

    throw new ReadOnlyException(getClass().getSimpleName(), "name");
  }

  @Override
  public CodeDoc getDoc() {

    CodeDoc doc;
    if (this.getter != null) {
      doc = this.getter.getReturns().getDoc();
      if (doc.isEmpty()) {
        doc = this.getter.getDoc();
      }
    } else {
      doc = this.setter.getParameters().get(0).getDoc();
      if (doc.isEmpty()) {
        doc = this.setter.getDoc();
      }
    }
    return doc;
  }

  @Override
  public void setDoc(CodeDoc doc) {

    throw new ReadOnlyException(getClass().getSimpleName(), "doc");
  }

  @Override
  public boolean isImmutable() {

    return true;
  }

  @Override
  public List<CodeAnnotation> getAnnotations() {

    List<CodeAnnotation> annotations = null;
    if (this.getter != null) {
      annotations = this.getter.getAnnotations();
    }
    if (this.setter != null) {
      if ((annotations == null) || annotations.isEmpty()) {
        annotations = this.setter.getAnnotations();
      } else {
        List<CodeAnnotation> setterAnnotations = this.setter.getAnnotations();
        if (!setterAnnotations.isEmpty()) {
          annotations = new ArrayList<>(annotations);
          annotations.addAll(setterAnnotations);
        }
      }
    }
    return annotations;
  }

  @Override
  public void write(Appendable sink, String indent, String currentIndent) {

    // virtual - nothing to do...
  }

  @Override
  public boolean isReadable() {

    return (this.getter != null);
  }

  @Override
  public boolean isWritable() {

    return (this.setter != null);
  }

  /**
   * @param property the {@link CodeBeanProperty} to join.
   * @return a new {@link CodeBeanProperty} created from joining this {@link CodeBeanProperty} with the given
   *         one or {@code null} if the join failed ({@link #getName() name} or {@link #getType() type} does
   *         not match, or both getters or both setters).
   */
  public CodeBeanProperty join(CodeBeanProperty property) {

    if (!this.name.equals(property.name)) {
      LOG.warn("Can not join property {} with property {}.", this.name, property.name);
      return null;
    }
    if (!this.type.equals(property.type)) {
      LOG.warn("Can not join properties {} of types {} and {}.", this.name, this.type, property.type);
      return null;
    }
    CodeType declaring;
    if (this.declaringType.isAssignableFrom(property.declaringType)) {
      declaring = property.declaringType;
    } else if (property.declaringType.isAssignableFrom(this.declaringType)) {
      declaring = this.declaringType;
    } else {
      LOG.warn("Can not join property {} with declaring types {} and {}.", this.name, this.declaringType, property.declaringType);
      return null;
    }
    if ((this.getter == null) && (property.getter != null)) {
      return new CodeBeanProperty(declaring, property.getter, this.setter, this.name, this.type);
    } else if ((this.setter == null) && (property.setter != null)) {
      return new CodeBeanProperty(declaring, this.getter, property.setter, this.name, this.type);
    } else {
      LOG.warn("Can not join property {} - candidates not disjunct.", this.name, property.name);
      return null;
    }
  }

  /**
   * @param declaring the new potential {@link #getDeclaringType()}.
   * @return a new {@link CodeBeanProperty} with its {@link #getType() type} being
   *         {@link CodeGenericType#resolve(CodeGenericType) resolved} or this property itself if the type
   *         could not be resolved any further.
   */
  public CodeBeanProperty inherit(CodeType declaring) {

    CodeGenericType resolvedType = this.type.resolve(declaring);
    if (resolvedType == declaring) {
      return this;
    }
    return new CodeBeanProperty(declaring, this.getter, this.setter, this.name, resolvedType);
  }

  @Override
  public String toString() {

    return this.name;
  }

  /**
   * @param method the {@link CodeMethod} that may be a {@link #getGetter() getter} or {@link #getSetter()
   *        setter}.
   * @return a new {@link CodeBeanProperty} for the given {@link CodeMethod} or {@code null} if neither
   *         {@link #getGetter() getter} nor {@link #getSetter() setter}.
   * @see #join(CodeBeanProperty)
   */
  public static CodeBeanProperty of(CodeMethod method) {

    List<? extends CodeParameter> parameters = method.getParameters();
    int size = parameters.size();
    CodeMethod getter = null;
    CodeMethod setter = null;
    CodeGenericType type;
    String name = null;
    if (size == 0) { // getter?
      type = method.getReturns().getType();
      if (type.getRawType().isVoid()) {
        return null;
      }
      name = getPropertyName(method, true);
      if (name == null) {
        return null;
      }
      getter = method;
    } else if (size == 1) { // setter?
      type = parameters.get(0).getType();
      name = getPropertyName(method, false);
      if (name == null) {
        return null;
      }
      setter = method;
    } else {
      return null;
    }
    return new CodeBeanProperty(method.getDeclaringType(), getter, setter, name, type);
  }

  private static String getPropertyName(CodeMethod method, boolean getterNotSetter) {

    String propertyName = method.getName();
    if (getterNotSetter) {
      if (propertyName.startsWith("get") || propertyName.startsWith("has") || propertyName.startsWith("can")) {
        propertyName = propertyName.substring(3);
      } else if (propertyName.startsWith("is")) {
        propertyName = propertyName.substring(2);
      } else {
        return null;
      }
    } else {
      if (propertyName.startsWith("set")) {
        propertyName = propertyName.substring(3);
      } else {
        return null;
      }
    }
    if (propertyName.isEmpty()) {
      return propertyName;
    }
    char first = propertyName.charAt(0);
    char uncapitalized = Character.toLowerCase(first);
    if (first == uncapitalized) {
      LOG.debug("Invalid getter/setter without capitalized property will be ignored: {}", method);
      return null;
    }
    propertyName = uncapitalized + propertyName.substring(1);
    return propertyName;
  }

}
