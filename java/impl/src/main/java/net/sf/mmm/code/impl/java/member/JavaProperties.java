/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.mmm.code.api.member.CodeProperties;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.impl.java.type.JavaGenericType;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link CodeProperties} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaProperties extends JavaMembers<JavaProperty> implements CodeProperties<JavaProperty>, CodeNodeItemWithGenericParent<JavaType, JavaProperties> {

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public JavaProperties(JavaType parent) {

    super(parent);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaProperties} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaProperties(JavaProperties template, JavaType parent) {

    super(template, parent);
  }

  @Override
  public List<? extends JavaProperty> getDeclared() {

    return getList();
  }

  @Override
  public Iterable<? extends JavaProperty> getAll() {

    Map<String, JavaProperty> map = new HashMap<>(getMap());
    collectProperties(map);
    return map.values();
  }

  private void collectProperties(Map<String, JavaProperty> map) {

    for (JavaGenericType superType : getDeclaringType().getSuperTypes().getDeclared()) {
      JavaProperties javaProperties = superType.asType().getProperties();
      for (JavaProperty property : javaProperties.getDeclared()) {
        map.putIfAbsent(property.getName(), property.inherit(getParent()));
      }
      javaProperties.collectProperties(map);
    }
  }

  @Override
  public JavaProperty get(String name) {

    JavaProperty property = getByName(name);
    if (property != null) {
      return property;
    }
    for (JavaGenericType superType : getDeclaringType().getSuperTypes().getDeclared()) {
      JavaProperties javaProperties = superType.asType().getProperties();
      property = javaProperties.get(name);
      if (property != null) {
        return property.inherit(getParent());
      }
    }
    return null;
  }

  @Override
  public JavaProperty getRequired(String name) {

    return super.getRequired(name);
  }

  @Override
  public JavaProperty add(String name) {

    verifyMutalbe();
    JavaProperty property = new JavaProperty(this, name);
    add(property);
    return property;
  }

  @Override
  public JavaProperty getDeclared(String name) {

    return getByName(name);
  }

  @Override
  public JavaProperty getDeclaredOrCreate(String name) {

    JavaProperty property = getDeclared(name);
    if (property == null) {
      property = add(name);
    }
    return property;
  }

  void renameMember(JavaMember member, String oldName, String newName) {

    if (member instanceof JavaField) {
      rename((JavaField) member, oldName, newName);
    } else if (member instanceof JavaMethod) {
      rename((JavaMethod) member, oldName, newName);
    }
  }

  private void rename(JavaField field, String oldName, String newName) {

    Map<String, JavaProperty> map = getMap();
    JavaProperty property = map.get(oldName);
    if ((property != null) && property.getField() == field) {
      property.setField(null);
      if (property.isEmpty()) {
        remove(property);
      }
    }
    property = getDeclaredOrCreate(newName);
    property.join(field);
  }

  private void rename(JavaMethod method, String oldName, String newName) {

    Map<String, JavaProperty> map = getMap();
    String propertyName = JavaProperty.getPropertyName(oldName);
    if (propertyName != null) {
      JavaProperty property = map.get(propertyName);
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
          remove(property);
        }
      }
    }
    propertyName = JavaProperty.getPropertyName(method, true);
    if (propertyName != null) {
      JavaProperty property = getDeclaredOrCreate(propertyName);
      property.join(method);
    }
  }

  @Override
  public JavaProperties copy() {

    return copy(getParent());
  }

  @Override
  public JavaProperties copy(JavaType newParent) {

    return new JavaProperties(this, newParent);
  }

}
