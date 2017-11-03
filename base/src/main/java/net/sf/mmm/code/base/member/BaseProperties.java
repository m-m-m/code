/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.member;

import java.util.HashMap;
import java.util.Map;

import net.sf.mmm.code.api.member.CodeField;
import net.sf.mmm.code.api.member.CodeMember;
import net.sf.mmm.code.api.member.CodeProperties;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.base.type.BaseGenericType;
import net.sf.mmm.code.base.type.BaseType;

/**
 * Base implementation of {@link CodeProperties}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseProperties extends BaseMembers<BaseProperty> implements CodeProperties<BaseProperty>, CodeNodeItemWithGenericParent<BaseType, BaseProperties> {

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public BaseProperties(BaseType parent) {

    super(parent);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseProperties} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public BaseProperties(BaseProperties template, BaseType parent) {

    super(template, parent);
  }

  @Override
  public Iterable<? extends BaseProperty> getAll() {

    Map<String, BaseProperty> map = new HashMap<>(getMap());
    collectProperties(map);
    return map.values();
  }

  private void collectProperties(Map<String, BaseProperty> map) {

    for (BaseGenericType superType : getDeclaringType().getSuperTypes()) {
      BaseProperties properties = superType.asType().getProperties();
      for (BaseProperty property : properties) {
        map.putIfAbsent(property.getName(), property.inherit(getParent()));
      }
      properties.collectProperties(map);
    }
  }

  @Override
  public BaseProperty get(String name) {

    BaseProperty property = getByName(name);
    if (property != null) {
      return property;
    }
    for (BaseGenericType superType : getDeclaringType().getSuperTypes().getDeclared()) {
      BaseProperties javaProperties = superType.asType().getProperties();
      property = javaProperties.get(name);
      if (property != null) {
        return property.inherit(getParent());
      }
    }
    return null;
  }

  @Override
  public BaseProperty add(String name) {

    verifyMutalbe();
    BaseProperty property = new BaseProperty(this, name);
    add(property);
    return property;
  }

  @Override
  public BaseProperty getDeclared(String name) {

    return getByName(name);
  }

  @Override
  public BaseProperty getDeclaredOrCreate(String name) {

    BaseProperty property = getDeclared(name);
    if (property == null) {
      property = add(name);
    }
    return property;
  }

  void renameMember(CodeMember member, String oldName, String newName) {

    if (member instanceof CodeField) {
      rename((CodeField) member, oldName, newName);
    } else if (member instanceof BaseMethod) {
      rename((BaseMethod) member, oldName, newName);
    }
  }

  private void rename(CodeField field, String oldName, String newName) {

    Map<String, BaseProperty> map = getMap();
    BaseProperty property = map.get(oldName);
    if ((property != null) && property.getField() == field) {
      property.setField(null);
      if (property.isEmpty()) {
        remove(property);
      }
    }
    property = getDeclaredOrCreate(newName);
    property.join(field);
  }

  private void rename(BaseMethod method, String oldName, String newName) {

    Map<String, BaseProperty> map = getMap();
    String propertyName = BaseProperty.getPropertyName(oldName);
    if (propertyName != null) {
      BaseProperty property = map.get(propertyName);
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
    propertyName = BaseProperty.getPropertyName(method, true);
    if (propertyName != null) {
      BaseProperty property = getDeclaredOrCreate(propertyName);
      property.join(method);
    }
  }

  /**
   * @deprecated a {@link BaseProperties} is a virtual object that can never have a source-code object.
   */
  @Deprecated
  @Override
  public BaseProperties getSourceCodeObject() {

    return null;
  }

  @Override
  public BaseProperties copy() {

    return copy(getParent());
  }

  @Override
  public BaseProperties copy(BaseType newParent) {

    return new BaseProperties(this, newParent);
  }

}
