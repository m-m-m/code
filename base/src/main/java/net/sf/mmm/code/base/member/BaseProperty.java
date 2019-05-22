/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.member;

import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.util.List;

import net.sf.mmm.code.api.arg.CodeParameter;
import net.sf.mmm.code.api.copy.CodeCopyMapper;
import net.sf.mmm.code.api.copy.CodeCopyType;
import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.api.member.CodeField;
import net.sf.mmm.code.api.member.CodeMethod;
import net.sf.mmm.code.api.member.CodeProperty;
import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.api.modifier.CodeVisibility;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base implementation of {@link CodeProperty}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseProperty extends BaseMember implements CodeProperty {

  private static final Logger LOG = LoggerFactory.getLogger(BaseProperty.class);

  private final BaseProperties parent;

  private CodeGenericType type;

  private CodeMethod getter;

  private CodeMethod setter;

  private CodeField field;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param name the {@link #getName() name}.
   */
  public BaseProperty(BaseProperties parent, String name) {

    super(parent, null, name);
    this.parent = parent;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseProperty} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseProperty(BaseProperty template, CodeCopyMapper mapper) {

    super(template, mapper);
    this.parent = mapper.map(template.parent, CodeCopyType.PARENT);
  }

  @Override
  public BaseProperties getParent() {

    return this.parent;
  }

  @Override
  public CodeModifiers getModifiers() {

    if (this.getter != null) {
      return this.getter.getModifiers();
    }
    if (this.setter != null) {
      return this.setter.getModifiers();
    }
    return this.field.getModifiers();
  }

  /**
   * @deprecated see {@link CodeProperty#setModifiers(CodeModifiers)}.
   */
  @Deprecated
  @Override
  public void setModifiers(CodeModifiers modifiers) {

    CodeProperty.super.setModifiers(modifiers);
  }

  @Override
  public CodeField getField() {

    return this.field;
  }

  void setField(BaseField field) {

    verifyMutalbe();
    this.field = field;
  }

  @Override
  public CodeMethod getGetter() {

    return this.getter;
  }

  void setGetter(CodeMethod getter) {

    verifyMutalbe();
    this.getter = getter;
  }

  @Override
  public CodeMethod getSetter() {

    return this.setter;
  }

  void setSetter(CodeMethod setter) {

    verifyMutalbe();
    this.setter = setter;
  }

  /**
   * @return {@code true} if this property is empty, {@code false} otherwise.
   */
  boolean isEmpty() {

    if (this.field != null) {
      return false;
    }
    if (this.getter != null) {
      return false;
    }
    if (this.setter != null) {
      return false;
    }
    return true;
  }

  /**
   * @deprecated a {@link BaseProperty} is a virtual object that can never have a reflective object. Use that method on
   *             {@link #getField()}, {@link #getGetter()}, or {@link #getSetter()} instead.
   */
  @Deprecated
  @Override
  public AccessibleObject getReflectiveObject() {

    return null;
  }

  /**
   * @deprecated a {@link BaseProperty} is a virtual object that can never have a source-code object. Use that method on
   *             {@link #getField()}, {@link #getGetter()}, or {@link #getSetter()} instead.
   */
  @Deprecated
  @Override
  public BaseProperty getSourceCodeObject() {

    return null;
  }

  @Override
  public BaseProperty inherit(CodeType declaring) {

    CodeGenericType resolvedType = this.type.resolve(declaring);
    if (resolvedType == this.type) {
      return this;
    }
    BaseProperty copy = copy();
    copy.type = resolvedType;
    return copy;
  }

  void join(CodeMethod method) {

    verifyMutalbe();
    assert (getName().equals(getPropertyName(method, true))) : method;
    assert (getDeclaringType().equals(method.getDeclaringType())) : method;
    boolean isSetter = (method.getName().startsWith("set"));
    if (isSetter) {
      if (this.setter != null) {
        LOG.debug("Replacing setter {} with {}.", this.setter, method);
      }
      this.setter = method;
    } else {
      if (this.getter != null) {
        LOG.debug("Replacing getter {} with {}.", this.getter, method);
      }
      this.getter = method;
    }
    if (this.type == null) {
      if (isSetter) {
        List<? extends CodeParameter> parameters = method.getParameters().getDeclared();
        if (parameters.size() == 1) {
          this.type = parameters.get(0).getType();
        } else {
          LOG.debug("Inconsistent setter {}.", method);
        }
      } else {
        this.type = method.getReturns().getType();
      }
    }
  }

  void join(CodeField newField) {

    verifyMutalbe();
    assert (getName().equals(newField.getName())) : newField;
    assert (getDeclaringType().equals(newField.getDeclaringType())) : newField;
    if (this.field != null) {
      LOG.debug("Replacing field {} with {}.", this.field, newField);
    }
    this.field = newField;
    if (this.type == null) {
      this.type = newField.getType();
    } else {
      assert (this.type == newField.getType());
    }
  }

  @Override
  public boolean isReadable(CodeVisibility visibility) {

    if ((this.getter != null) && this.getter.getModifiers().getVisibility().isWeakerOrEqualTo(visibility)) {
      return true;
    }
    if ((this.field != null) && this.field.getModifiers().getVisibility().isWeakerOrEqualTo(visibility)) {
      return true;
    }
    return false;
  }

  @Override
  public boolean isWritable(CodeVisibility visibility) {

    if ((this.setter != null) && this.setter.getModifiers().getVisibility().isWeakerOrEqualTo(visibility)) {
      return true;
    }
    if ((this.field != null) && !this.field.getModifiers().isFinal()
        && this.field.getModifiers().getVisibility().isWeakerOrEqualTo(visibility)) {
      return true;
    }
    return false;
  }

  @Override
  public CodeGenericType getType() {

    return this.type;
  }

  @Override
  public void setType(CodeGenericType type) {

    verifyMutalbe();
    this.type = type;
  }

  @Override
  public BaseProperty copy() {

    return copy(getDefaultCopyMapper());
  }

  @Override
  public BaseProperty copy(CodeCopyMapper mapper) {

    return new BaseProperty(this, mapper);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent,
      CodeLanguage language) throws IOException {

    // properties are virtual and will never be written...
  }

  static String getPropertyName(CodeMethod method, boolean checkSignature) {

    String methodName = method.getName();
    String propertyName = getPropertyName(methodName);
    if (checkSignature && (propertyName != null)) {
      if (methodName.startsWith("set")) {
        if (method.getParameters().getDeclared().size() != 1) {
          return null;
        }
      } else {
        if (method.getParameters().getDeclared().size() != 0) {
          return null;
        }
        if (method.getReturns().getType().asType().isVoid()) {
          return null;
        }
      }
    }
    return propertyName;
  }

  static String getPropertyName(String methodName) {

    String propertyName = methodName;
    if (propertyName.startsWith("set") || propertyName.startsWith("get") || propertyName.startsWith("has")
        || propertyName.startsWith("can")) {
      propertyName = propertyName.substring(3);
    } else if (propertyName.startsWith("is")) {
      propertyName = propertyName.substring(2);
    } else {
      return null;
    }
    if (!propertyName.isEmpty()) {
      char first = propertyName.charAt(0);
      char uncapitalized = Character.toLowerCase(first);
      if (first == uncapitalized) {
        LOG.debug("Invalid getter/setter without capitalized property will be ignored: {}", methodName);
        return null;
      }
      propertyName = uncapitalized + propertyName.substring(1);
    }
    return propertyName;
  }

}
