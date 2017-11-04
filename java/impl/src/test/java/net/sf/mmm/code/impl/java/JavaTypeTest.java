/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.assertj.core.api.Assertions;

import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeTypeCategory;
import net.sf.mmm.code.base.member.BaseConstructor;
import net.sf.mmm.code.base.member.BaseConstructors;
import net.sf.mmm.code.base.member.BaseField;
import net.sf.mmm.code.base.member.BaseFields;
import net.sf.mmm.code.base.member.BaseMethod;
import net.sf.mmm.code.base.member.BaseMethods;
import net.sf.mmm.code.base.type.BaseType;

/**
 * Test of {@link JavaRootContext}.
 */
@SuppressWarnings("javadoc")
public abstract class JavaTypeTest extends Assertions {

  public static void verifyClass(BaseType type, Class<?> clazz, JavaContext context) {

    assertThat(type).isNotNull();
    assertThat(type.getContext()).isSameAs(context);
    assertThat(context.getType(clazz)).isSameAs(type);
    assertThat(context.getType(clazz.getName())).isSameAs(type);
    assertThat(type.getSimpleName()).isEqualTo(clazz.getSimpleName());
    assertThat(type.getQualifiedName()).isEqualTo(clazz.getName());
    assertThat(type.getReflectiveObject()).isSameAs(clazz);
    CodeTypeCategory category;
    if (clazz.isInterface()) {
      category = CodeTypeCategory.INTERFACE;
    } else if (clazz.isEnum()) {
      category = CodeTypeCategory.ENUMERAION;
    } else if (clazz.isAnnotation()) {
      category = CodeTypeCategory.ANNOTATION;
    } else {
      category = CodeTypeCategory.CLASS;
    }
    assertThat(type.getCategory()).isSameAs(category);
    assertThat(type.getModifiers()).isEqualTo(CodeModifiers.of(clazz.getModifiers()));
    verifyFields(type.getFields(), clazz.getDeclaredFields(), context);
    verifyConstructors(type.getConstructors(), clazz.getDeclaredConstructors(), context);
    verifyMethods(type.getMethods(), clazz.getDeclaredMethods(), context);
  }

  public static void verifyFields(BaseFields fields, Field[] declaredFields, JavaContext context) {

    assertThat(fields.getDeclared()).hasSameSizeAs(declaredFields);
    for (Field field : declaredFields) {
      verifyField(fields, field, context);
    }
  }

  public static void verifyField(BaseFields fields, Field field, JavaContext context) {

    BaseField javaField = fields.get(field.getName());
    assertThat(javaField).as(field.toGenericString()).isNotNull();
    assertThat(javaField.getName()).isEqualTo(field.getName());
    CodeGenericType type = javaField.getType();
    if (!type.isArray()) {
      type = type.asType();
    }
    assertThat(type).as("Type of " + javaField).isEqualTo(context.getType(field.getType()));
  }

  public static void verifyConstructors(BaseConstructors constructors, Constructor<?>[] declaredConstructors, JavaContext context) {

    assertThat(constructors.getDeclared()).hasSameSizeAs(declaredConstructors);
    for (Constructor<?> constructor : declaredConstructors) {
      verifyConstructor(constructors, constructor, context);
    }
  }

  public static void verifyConstructor(BaseConstructors constructors, Constructor<?> constructor, JavaContext context) {

    Class<?>[] parameters = constructor.getParameterTypes();
    CodeGenericType[] parameterTypes = new CodeGenericType[parameters.length];
    for (int i = 0; i < parameters.length; i++) {
      parameterTypes[i] = context.getType(parameters[i]);
    }
    BaseConstructor javaConstructor = constructors.get(parameterTypes);
    assertThat(javaConstructor).as(constructor.toGenericString()).isNotNull();
    assertThat(constructor.getName()).isEqualTo(constructor.getName());
  }

  public static void verifyMethods(BaseMethods methods, Method[] declaredMethods, JavaContext context) {

    assertThat(methods.getDeclared()).hasSameSizeAs(declaredMethods);
    for (Method method : declaredMethods) {
      verifyMethod(methods, method, context);
    }
  }

  public static void verifyMethod(BaseMethods methods, Method method, JavaContext context) {

    Class<?>[] parameters = method.getParameterTypes();
    CodeGenericType[] parameterTypes = new CodeGenericType[parameters.length];
    for (int i = 0; i < parameters.length; i++) {
      parameterTypes[i] = context.getType(parameters[i]);
    }
    BaseMethod javaMethod = methods.getDeclared(method.getName(), parameterTypes);
    assertThat(javaMethod).as(method.toGenericString()).isNotNull();
    assertThat(javaMethod.getName()).isEqualTo(method.getName());
  }

}
