/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.member;

import java.lang.reflect.Constructor;

import net.sf.mmm.code.api.member.CodeConstructors;
import net.sf.mmm.code.api.member.CodeMethods;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link CodeMethods} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaConstructors extends JavaOperations<JavaConstructor>
    implements CodeConstructors<JavaConstructor>, CodeNodeItemWithGenericParent<JavaType, JavaConstructors> {

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public JavaConstructors(JavaType parent) {

    super(parent);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaConstructors} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaConstructors(JavaConstructors template, JavaType parent) {

    super(template, parent);
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
    Class<?> reflectiveObject = getParent().getReflectiveObject();
    if (reflectiveObject != null) {
      for (Constructor<?> constructor : reflectiveObject.getDeclaredConstructors()) {
        JavaConstructor javaConstructor = new JavaConstructor(this, constructor);
        addInternal(javaConstructor);
      }
    }
  }

  @Override
  public JavaConstructor get(JavaConstructor constructor) {

    for (JavaConstructor myConstructor : getDeclared()) {
      if (myConstructor.getParameters().isInvokable(constructor.getParameters())) {
        return constructor;
      }
    }
    return null;
  }

  @Override
  public JavaConstructor get(CodeGenericType... parameterTypes) {

    for (JavaConstructor constructor : getDeclared()) {
      if (constructor.getParameters().isInvokable(parameterTypes)) {
        return constructor;
      }
    }
    return null;
  }

  @Override
  public JavaConstructor add() {

    JavaConstructor constructor = new JavaConstructor(this, null);
    add(constructor);
    return constructor;
  }

  @Override
  public JavaConstructors copy() {

    return copy(getParent());
  }

  @Override
  public JavaConstructors copy(JavaType newParent) {

    return new JavaConstructors(this, newParent);
  }

}
