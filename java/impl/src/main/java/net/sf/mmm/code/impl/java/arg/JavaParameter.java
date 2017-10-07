/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.arg;

import java.io.IOException;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

import net.sf.mmm.code.api.arg.CodeParameter;
import net.sf.mmm.code.api.expression.CodeLiteral;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.impl.java.item.JavaReflectiveObject;
import net.sf.mmm.code.impl.java.member.JavaOperation;
import net.sf.mmm.code.impl.java.type.JavaGenericType;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link CodeParameter} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaParameter extends JavaOperationArg
    implements CodeParameter, CodeNodeItemWithGenericParent<JavaParameters, JavaParameter>, JavaReflectiveObject<Parameter> {

  private final JavaParameters parent;

  private final Parameter reflectiveObject;

  private String name;

  private boolean varArgs;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param name the {@link #getName() name}.
   */
  public JavaParameter(JavaParameters parent, String name) {

    this(parent, name, null);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May be {@code null}.
   */
  public JavaParameter(JavaParameters parent, Parameter reflectiveObject) {

    this(parent, reflectiveObject.getName(), reflectiveObject);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param name the {@link #getName() name}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May be {@code null}.
   */
  private JavaParameter(JavaParameters parent, String name, Parameter reflectiveObject) {

    super();
    this.parent = parent;
    this.reflectiveObject = reflectiveObject;
    this.name = name;
    verifyName(name, NAME_PATTERN);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaParameter} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaParameter(JavaParameter template, JavaParameters parent) {

    super(template);
    this.parent = parent;
    this.name = template.name;
    this.reflectiveObject = null;
  }

  @Override
  public JavaParameters getParent() {

    return this.parent;
  }

  @Override
  public JavaOperation getDeclaringOperation() {

    return this.parent.getParent();
  }

  @Override
  public JavaType getDeclaringType() {

    return getParent().getDeclaringType();
  }

  @Override
  public String getName() {

    return this.name;
  }

  @Override
  public void setName(String name) {

    verifyMutalbe();
    this.name = name;
  }

  @Override
  public boolean isVarArgs() {

    return this.varArgs;
  }

  @Override
  public void setVarArgs(boolean varArgs) {

    verifyMutalbe();
    this.varArgs = varArgs;
  }

  @Override
  public Parameter getReflectiveObject() {

    return this.reflectiveObject;
  }

  @Override
  protected Type getReflectiveObjectType() {

    if (this.reflectiveObject != null) {
      return this.reflectiveObject.getParameterizedType();
    }
    return null;
  }

  @Override
  public CodeLiteral evaluate() {

    return null;
  }

  @Override
  public JavaParameter copy() {

    return copy(this.parent);
  }

  @Override
  public JavaParameter copy(JavaParameters newParent) {

    return new JavaParameter(this, newParent);
  }

  @Override
  protected void doWriteType(Appendable sink) throws IOException {

    if (this.varArgs) {
      JavaGenericType type = getType();
      JavaGenericType componentType = type.getComponentType();
      if (componentType != null) {
        componentType.writeReference(sink, false);
        sink.append("...");
      }
    }
    super.doWriteType(sink);
  }

}
