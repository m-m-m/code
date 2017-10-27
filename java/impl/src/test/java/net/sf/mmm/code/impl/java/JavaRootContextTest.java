/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeTypeCategory;
import net.sf.mmm.code.base.BasePackage;
import net.sf.mmm.code.base.member.BaseConstructor;
import net.sf.mmm.code.base.member.BaseConstructors;
import net.sf.mmm.code.base.member.BaseField;
import net.sf.mmm.code.base.member.BaseFields;
import net.sf.mmm.code.base.member.BaseMethod;
import net.sf.mmm.code.base.member.BaseMethods;
import net.sf.mmm.code.base.type.BaseGenericType;
import net.sf.mmm.code.base.type.BaseGenericTypeParameters;
import net.sf.mmm.code.base.type.BaseParameterizedType;
import net.sf.mmm.code.base.type.BaseSuperTypes;
import net.sf.mmm.code.base.type.BaseType;
import net.sf.mmm.code.base.type.BaseTypeVariable;
import net.sf.mmm.code.base.type.BaseTypeVariables;

/**
 * Test of {@link JavaRootContext}.
 */
public class JavaRootContextTest extends Assertions {

  private JavaRootContext getContext() {

    return JavaRootContext.get();
  }

  /**
   * Test the very basic methods of {@link JavaContext}.
   */
  @Test
  public void testBasics() {

    // given
    JavaContext context = getContext();

    // then
    assertThat(context.getPackageSeparator()).isEqualTo('.');
    assertThat(context.getRootContext()).isSameAs(context);
    BasePackage rootPackage = context.getRootPackage();
    assertThat(rootPackage).isNotNull();
    assertThat(rootPackage.getSimpleName()).isEmpty();
    assertThat(rootPackage.getQualifiedName()).isEmpty();
    assertThat(rootPackage.getParentPackage()).isNull();
    assertThat(context.getPackage("")).isSameAs(rootPackage);
    assertThat(context.getQualifiedNameForStandardType("byte", true)).isEqualTo("byte");
    assertThat(context.getQualifiedNameForStandardType("String", true)).isEqualTo("String");
    assertThat(context.getQualifiedNameForStandardType("String", false)).isEqualTo("java.lang.String");
    assertThat(context.getQualifiedNameForStandardType("UndefinedBanana", false)).isNull();
  }

  /**
   * Test of {@link JavaContext#getRootType()}
   */
  @Test
  public void testObject() {

    // given
    JavaContext context = getContext();

    // when
    BaseType object = context.getRootType();

    // then
    verifyClass(object, Object.class, context);
  }

  /**
   * Test of {@link JavaContext#getRootExceptionType()}
   */
  @Test
  public void testThrowable() {

    // given
    JavaContext context = getContext();

    // when
    BaseType throwable = context.getRootExceptionType();

    // then
    verifyClass(throwable, Throwable.class, context);
  }

  /**
   * Test of {@link JavaContext#getRootEnumerationType()}.
   */
  @Test
  public void testEnum() {

    // given
    JavaContext context = getContext();

    // when
    BaseType enumeration = context.getRootEnumerationType();

    // then
    verifyClass(enumeration, Enum.class, context);
    BaseSuperTypes superTypes = enumeration.getSuperTypes();
    assertThat(superTypes.getSuperClass()).isSameAs(context.getRootType());
    // test "Enum implements Comparable<E>, Serializable"
    @SuppressWarnings("unchecked")
    List<BaseGenericType> superInterfaces = (List<BaseGenericType>) superTypes.getSuperInterfaces();
    BaseGenericType serializable = context.getType(Serializable.class);
    assertThat(superInterfaces).hasSize(2).contains(serializable);
    BaseGenericType comparable = superInterfaces.get(0);
    if (comparable == serializable) {
      comparable = superInterfaces.get(1);
    }
    assertThat(comparable).isNotNull().isInstanceOf(BaseParameterizedType.class);

    // test "Enum<E extends Enum<E>>" - great test with its recursive declaration
    BaseTypeVariables typeVariables = enumeration.getTypeParameters();
    List<? extends BaseTypeVariable> typeVariableList = typeVariables.getDeclared();
    assertThat(typeVariableList).hasSize(1);
    BaseTypeVariable typeVariable = typeVariableList.get(0);
    assertThat(typeVariable.getName()).isEqualTo("E");
    assertThat(typeVariable.asType()).isSameAs(enumeration);
    assertThat(typeVariables.get("E")).isSameAs(typeVariable);
    assertThat(typeVariable.isSuper()).isFalse();
    assertThat(typeVariable.isExtends()).isTrue();
    assertThat(typeVariable.isWildcard()).isFalse();
    BaseGenericType bound = typeVariable.getBound();
    assertThat(bound).isNotNull().isInstanceOf(BaseParameterizedType.class);
    BaseGenericTypeParameters<?> typeParameters = bound.getTypeParameters();
    List<? extends BaseGenericType> typeParameterList = typeParameters.getDeclared();
    assertThat(typeParameterList).hasSize(1);
    BaseGenericType boundTypeVariable = typeParameterList.get(0);
    assertThat(boundTypeVariable).isSameAs(typeVariable);
  }

  private void verifyClass(BaseType type, Class<?> clazz, JavaContext context) {

    assertThat(type).isNotNull();
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

  private void verifyFields(BaseFields fields, Field[] declaredFields, JavaContext context) {

    assertThat(fields.getDeclared()).hasSameSizeAs(declaredFields);
    for (Field field : declaredFields) {
      verifyField(fields, field, context);
    }
  }

  private void verifyField(BaseFields fields, Field field, JavaContext context) {

    BaseField javaField = fields.get(field.getName());
    assertThat(javaField).as(field.toGenericString()).isNotNull();
    assertThat(javaField.getName()).isEqualTo(field.getName());
    CodeGenericType type = javaField.getType();
    if (!type.isArray()) {
      type = type.asType();
    }
    assertThat(type).as("Type of " + javaField).isEqualTo(context.getType(field.getType()));
  }

  private void verifyConstructors(BaseConstructors constructors, Constructor<?>[] declaredConstructors, JavaContext context) {

    assertThat(constructors.getDeclared()).hasSameSizeAs(declaredConstructors);
    for (Constructor<?> constructor : declaredConstructors) {
      verifyConstructor(constructors, constructor, context);
    }
  }

  private void verifyConstructor(BaseConstructors constructors, Constructor<?> constructor, JavaContext context) {

    Class<?>[] parameters = constructor.getParameterTypes();
    CodeGenericType[] parameterTypes = new CodeGenericType[parameters.length];
    for (int i = 0; i < parameters.length; i++) {
      parameterTypes[i] = context.getType(parameters[i]);
    }
    BaseConstructor javaConstructor = constructors.get(parameterTypes);
    assertThat(javaConstructor).as(constructor.toGenericString()).isNotNull();
    assertThat(constructor.getName()).isEqualTo(constructor.getName());
  }

  private void verifyMethods(BaseMethods methods, Method[] declaredMethods, JavaContext context) {

    assertThat(methods.getDeclared()).hasSameSizeAs(declaredMethods);
    for (Method method : declaredMethods) {
      verifyMethod(methods, method, context);
    }
  }

  private void verifyMethod(BaseMethods methods, Method method, JavaContext context) {

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
