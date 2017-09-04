/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.member;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.mmm.code.api.member.CodeProperties;
import net.sf.mmm.code.api.member.CodeProperty;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.impl.java.type.JavaGenericType;
import net.sf.mmm.code.impl.java.type.JavaType;
import net.sf.mmm.util.exception.api.DuplicateObjectException;

/**
 * Implementation of {@link CodeProperties} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaProperties extends JavaMembers<CodeProperty> implements CodeProperties {

  private List<JavaProperty> properties;

  private Map<String, JavaProperty> propertyMap;

  /**
   * The constructor.
   *
   * @param declaringType the {@link #getDeclaringType()}.
   */
  public JavaProperties(JavaType declaringType) {

    super(declaringType);
    this.properties = new ArrayList<>();
    this.propertyMap = new HashMap<>();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaProperties} to copy.
   * @param declaringType the {@link #getDeclaringType()}.
   */
  public JavaProperties(JavaProperties template, JavaType declaringType) {

    super(template, declaringType);
    this.properties = doCopy(template.properties, declaringType);
  }

  @Override
  protected void doSetImmutable() {

    super.doSetImmutable();
    this.properties = makeImmutable(this.properties);
    this.propertyMap = Collections.unmodifiableMap(this.propertyMap);
  }

  @Override
  public List<? extends JavaProperty> getDeclared() {

    return this.properties;
  }

  @Override
  public Iterable<? extends JavaProperty> getAll() {

    List<JavaProperty> list = new ArrayList<>(this.properties);
    collectProperties(list);
    return list;
  }

  private void collectProperties(List<JavaProperty> list) {

    for (JavaGenericType superType : getDeclaringType().getSuperTypes().getDeclared()) {
      JavaProperties javaProperties = superType.asType().getProperties();
      list.addAll(javaProperties.properties);
      javaProperties.collectProperties(list);
    }
  }

  @Override
  public JavaProperty get(String name) {

    JavaProperty property = this.propertyMap.get(name);
    if (property != null) {
      return property;
    }
    for (JavaGenericType superType : getDeclaringType().getSuperTypes().getDeclared()) {
      JavaProperties javaProperties = superType.asType().getProperties();
      property = javaProperties.get(name);
      if (property != null) {
        return property;
      }
    }
    return null;
  }

  @Override
  public JavaProperty add(String name) {

    verifyMutalbe();
    if (this.propertyMap.containsKey(name)) {
      throw new DuplicateObjectException(getDeclaringType().getSimpleName() + ".properties", name);
    }
    JavaProperty property = new JavaProperty(getDeclaringType());
    property.setName(name);
    this.properties.add(property);
    this.propertyMap.put(name, property);
    return property;
  }

  /**
   * @param name the {@link JavaProperty#getName() name} of the {@link JavaProperty} to remove.
   * @return {@code true} if successfully removed, {@code false} otherwise.
   */
  boolean remove(String name) {

    verifyMutalbe();
    JavaProperty property = this.propertyMap.remove(name);
    if (property != null) {
      return this.properties.remove(property);
    }
    return false;
  }

  @Override
  public CodeProperty getDeclared(String name) {

    return this.propertyMap.get(name);
  }

  void rename(JavaField field, String oldName) {

    JavaProperty property = this.propertyMap.get(oldName);
    if ((property != null) && property.getField() == field) {
      property.setField(null);
      if (property.isEmpty()) {
        remove(oldName);
      }
    }
    String name = field.getName();
    property = this.propertyMap.get(name);
    if (property == null) {
      property = add(name);
    }
    property.join(field);
  }

  void rename(JavaMethod method, String oldName) {

    String propertyName = JavaProperty.getPropertyName(oldName);
    if (propertyName != null) {
      JavaProperty property = this.propertyMap.get(propertyName);
      if (property != null) {
        if (oldName.startsWith("set")) {
          if (property.getSetter() == method) {
            property.setSetter(null);
          }
        } else {
          if (property.getGetter() == method) {
            property.setGetter(null);
          }
        }
        if (property.isEmpty()) {
          remove(propertyName);
        }
      }
    }
    propertyName = JavaProperty.getPropertyName(method, true);
    if (propertyName != null) {
      JavaProperty property = this.propertyMap.get(propertyName);
      if (property == null) {
        property = add(propertyName);
      }
      property.join(method);
    }
  }

  @Override
  public JavaProperties copy(CodeType newDeclaringType) {

    return new JavaProperties(this, (JavaType) newDeclaringType);
  }

}
