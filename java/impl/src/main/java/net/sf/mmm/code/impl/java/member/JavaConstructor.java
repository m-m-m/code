/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.member;

import java.lang.reflect.Constructor;
import java.util.regex.Pattern;

import net.sf.mmm.code.api.member.CodeConstructor;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.impl.java.item.JavaReflectiveObject;
import net.sf.mmm.code.impl.java.parser.JavaTypeVariablesFromSource;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * Implementation of {@link CodeConstructor} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaConstructor extends JavaOperation
    implements CodeConstructor, CodeNodeItemWithGenericParent<JavaConstructors, JavaConstructor>, JavaReflectiveObject<Constructor<?>> {

  private final JavaConstructors parent;

  private final Constructor<?> reflectiveObject;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public JavaConstructor(JavaConstructors parent) {

    this(parent, null);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May be {@code null}.
   */
  public JavaConstructor(JavaConstructors parent, Constructor<?> reflectiveObject) {

    super("");
    this.parent = parent;
    this.reflectiveObject = reflectiveObject;
  }

  /**
   * The constructor.
   *
   * @param typeVariables the {@link #getTypeParameters() type variables}.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaConstructor(JavaTypeVariablesFromSource typeVariables, JavaConstructors parent) {

    super("", typeVariables);
    this.parent = parent;
    this.reflectiveObject = null;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaConstructor} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaConstructor(JavaConstructor template, JavaConstructors parent) {

    super(template);
    this.parent = parent;
    this.reflectiveObject = null;
  }

  @Override
  protected Pattern getNamePattern() {

    return null;
  }

  @Override
  public JavaConstructors getParent() {

    return this.parent;
  }

  @Override
  public Constructor<?> getReflectiveObject() {

    return this.reflectiveObject;
  }

  @Override
  public String getName() {

    return getDeclaringType().getSimpleName();
  }

  @Override
  public void setName(String name) {

    throw new ReadOnlyException(getClass().getSimpleName(), "name");
  }

  @Override
  public JavaConstructor copy() {

    return copy(this.parent);
  }

  @Override
  public JavaConstructor copy(JavaConstructors newParent) {

    return new JavaConstructor(this, newParent);
  }

}
