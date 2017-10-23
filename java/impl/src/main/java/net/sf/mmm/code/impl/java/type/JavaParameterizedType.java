/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.type;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.syntax.CodeSyntax;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeParameterizedType;
import net.sf.mmm.code.impl.java.arg.JavaOperationArg;
import net.sf.mmm.code.impl.java.element.JavaElement;
import net.sf.mmm.code.impl.java.item.JavaReflectiveObject;
import net.sf.mmm.code.impl.java.member.JavaOperation;
import net.sf.mmm.code.impl.java.node.JavaNode;

/**
 * Implementation of {@link JavaGenericType} for a {@link java.lang.reflect.ParameterizedType}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaParameterizedType extends JavaGenericType
    implements CodeParameterizedType, CodeNodeItemWithGenericParent<JavaElement, JavaParameterizedType>, JavaReflectiveObject<ParameterizedType> {

  private static final Logger LOG = LoggerFactory.getLogger(JavaParameterizedType.class);

  private final JavaElement parent;

  private final JavaTypeParameters typeVariables;

  private final ParameterizedType reflectiveObject;

  private JavaType type;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param type the {@link #getType() type} that gets parameterized. Should have the same number of
   *        {@link JavaType#getTypeParameters() type variables} as the {@link #getTypeParameters() type
   *        parameters of this type} when initialized.
   */
  public JavaParameterizedType(JavaElement parent, JavaType type) {

    this(parent, null, type);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May be {@code null}.
   */
  public JavaParameterizedType(JavaElement parent, ParameterizedType reflectiveObject) {

    this(parent, reflectiveObject, null);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May be {@code null}.
   * @param type the {@link #getType() type} that gets parameterized. Should have the same number of
   *        {@link JavaType#getTypeParameters() type variables} as the {@link #getTypeParameters() type
   *        parameters of this type} when initialized.
   */
  public JavaParameterizedType(JavaElement parent, ParameterizedType reflectiveObject, JavaType type) {

    super();
    this.parent = parent;
    this.typeVariables = new JavaTypeParameters(this);
    this.reflectiveObject = reflectiveObject;
    this.type = type;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaParameterizedType} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaParameterizedType(JavaParameterizedType template, JavaElement parent) {

    super(template);
    this.parent = parent;
    this.typeVariables = template.typeVariables.copy(this);
    this.reflectiveObject = null;
    this.type = template.type;
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
    getType();
    // Type ownerType = this.reflectiveObject.getOwnerType(); // TODO not used...
  }

  @Override
  public JavaElement getParent() {

    return this.parent;
  }

  @Override
  public JavaTypeParameters getTypeParameters() {

    return this.typeVariables;
  }

  @Override
  public ParameterizedType getReflectiveObject() {

    return this.reflectiveObject;
  }

  @Override
  public JavaType getDeclaringType() {

    return getType();
  }

  @Override
  public JavaOperation getDeclaringOperation() {

    return getDeclaringOperation(this.parent);
  }

  private JavaOperation getDeclaringOperation(JavaNode parentNode) {

    if (parentNode instanceof JavaOperationArg) {
      return ((JavaOperationArg) parentNode).getDeclaringOperation();
    } else if (parentNode instanceof JavaParameterizedType) {
      return ((JavaParameterizedType) parentNode).getDeclaringOperation();
    } else if (parentNode instanceof JavaArrayType) {
      return getDeclaringOperation(parentNode.getParent());
    }
    return null;
  }

  @Override
  public JavaType asType() {

    return getType().asType();
  }

  @Override
  public JavaType getType() {

    if ((this.type == null) && (this.reflectiveObject != null)) {
      Class<?> rawClass = (Class<?>) this.reflectiveObject.getRawType();
      this.type = (JavaType) getContext().getType(rawClass);
    }
    return this.type;
  }

  @Override
  public void setType(CodeGenericType type) {

    verifyMutalbe();
    this.type = (JavaType) type;
  }

  @Override
  public JavaTypeVariable asTypeVariable() {

    return null;
  }

  @Override
  public JavaGenericType resolve(CodeGenericType context) {

    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public String getSimpleName() {

    return getType().getSimpleName();
  }

  @Override
  public String getQualifiedName() {

    return getType().getQualifiedName();
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeSyntax syntax) throws IOException {

    super.doWrite(sink, newline, null, "", syntax);
    writeReference(sink, true);
  }

  @Override
  public JavaParameterizedType copy() {

    return copy(this.parent);
  }

  @Override
  public JavaParameterizedType copy(JavaElement newParent) {

    return new JavaParameterizedType(this, newParent);
  }

  @Override
  public void writeReference(Appendable sink, boolean declaration, Boolean qualified) throws IOException {

    JavaType myType = getType();
    if (myType == null) {
      LOG.warn("Parameterized type with undefined type declared in {}", getDeclaringType().getSimpleName());
      sink.append("Undefined");
    } else {
      myType.writeReference(sink, false, qualified);
      getTypeParameters().write(sink, DEFAULT_NEWLINE, null, "");
    }
  }

}
