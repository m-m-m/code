/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.arg;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;

import net.sf.mmm.code.api.arg.CodeException;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.impl.java.item.JavaReflectiveObject;
import net.sf.mmm.code.impl.java.member.JavaOperation;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link CodeException} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaException extends JavaOperationArg
    implements CodeException, CodeNodeItemWithGenericParent<JavaExceptions, JavaException>, JavaReflectiveObject<AnnotatedType> {

  private final JavaExceptions parent;

  private final AnnotatedType reflectiveObject;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May be {@code null}.
   */
  public JavaException(JavaExceptions parent, AnnotatedType reflectiveObject) {

    super();
    this.parent = parent;
    this.reflectiveObject = reflectiveObject;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaException} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaException(JavaException template, JavaExceptions parent) {

    super(template);
    this.parent = parent;
    this.reflectiveObject = null;
  }

  @Override
  public JavaExceptions getParent() {

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
  public AnnotatedType getReflectiveObject() {

    return this.reflectiveObject;
  }

  @Override
  protected Type getReflectiveObjectType() {

    if (this.reflectiveObject != null) {
      return this.reflectiveObject.getType();
    }
    return null;
  }

  @Override
  public JavaException copy() {

    return copy(this.parent);
  }

  @Override
  public JavaException copy(JavaExceptions newParent) {

    return new JavaException(this, newParent);
  }

}
