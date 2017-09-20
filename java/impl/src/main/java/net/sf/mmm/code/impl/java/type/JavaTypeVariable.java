/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.type;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import net.sf.mmm.code.api.member.CodeOperation;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.type.CodeTypeVariable;
import net.sf.mmm.code.impl.java.item.JavaReflectiveObject;

/**
 * Implementation of {@link CodeTypeVariable} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaTypeVariable extends JavaTypePlaceholder
    implements CodeTypeVariable, CodeNodeItemWithGenericParent<JavaTypeVariables, JavaTypeVariable>, JavaReflectiveObject<TypeVariable<?>> {

  private final JavaTypeVariables parent;

  private final TypeVariable<?> reflectiveObject;

  private String name;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param name the {@link #getName() name}.
   */
  public JavaTypeVariable(JavaTypeVariables parent, String name) {

    this(parent, name, null, null);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param name the {@link #getName() name}.
   * @param bound the {@link #getBound() bound}.
   */
  public JavaTypeVariable(JavaTypeVariables parent, String name, JavaGenericType bound) {

    this(parent, name, null, bound);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}.
   */
  public JavaTypeVariable(JavaTypeVariables parent, TypeVariable<?> reflectiveObject) {

    this(parent, reflectiveObject.getName(), reflectiveObject, null);
  }

  private JavaTypeVariable(JavaTypeVariables parent, String name, TypeVariable<?> reflectiveObject, JavaGenericType bound) {

    super(bound);
    this.parent = parent;
    this.reflectiveObject = reflectiveObject;
    this.name = name;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaTypeVariable} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaTypeVariable(JavaTypeVariable template, JavaTypeVariables parent) {

    super(template);
    this.parent = parent;
    this.reflectiveObject = null;
    this.name = template.name;
  }

  @Override
  public JavaTypeVariables getParent() {

    return this.parent;
  }

  @Override
  public JavaTypeVariable asTypeVariable() {

    return this;
  }

  @Override
  public TypeVariable<?> getReflectiveObject() {

    return this.reflectiveObject;
  }

  @Override
  protected Type[] getReflectiveBounds() {

    if (this.reflectiveObject == null) {
      return null;
    }
    return this.reflectiveObject.getBounds();
  }

  @Override
  public String getName() {

    return this.name;
  }

  @Override
  public void setName(String name) {

    verifyMutalbe();
    this.parent.rename(this, this.name, name, this::doSetName);
  }

  private void doSetName(String newName) {

    this.name = newName;
  }

  @Override
  public JavaType getDeclaringType() {

    return this.parent.getDeclaringType();
  }

  @Override
  public CodeOperation getDeclaringOperation() {

    return this.parent.getDeclaringOperation();
  }

  @Override
  public final boolean isExtends() {

    return true;
  }

  @Override
  public final boolean isSuper() {

    return false;
  }

  @Override
  public final boolean isWildcard() {

    return false;
  }

  @Override
  public JavaTypeVariable copy() {

    return copy(this.parent);
  }

  @Override
  public JavaTypeVariable copy(JavaTypeVariables newParent) {

    return new JavaTypeVariable(this, newParent);
  }

}
