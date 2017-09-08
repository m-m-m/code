/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.CodeName;
import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.api.type.CodeTypeCategory;
import net.sf.mmm.code.impl.java.annotation.JavaAnnotation;
import net.sf.mmm.code.impl.java.arg.JavaParameter;
import net.sf.mmm.code.impl.java.arg.JavaParameters;
import net.sf.mmm.code.impl.java.element.JavaElement;
import net.sf.mmm.code.impl.java.element.JavaElementWithTypeVariables;
import net.sf.mmm.code.impl.java.expression.JavaLiteral;
import net.sf.mmm.code.impl.java.member.JavaConstructor;
import net.sf.mmm.code.impl.java.member.JavaConstructors;
import net.sf.mmm.code.impl.java.member.JavaField;
import net.sf.mmm.code.impl.java.member.JavaFields;
import net.sf.mmm.code.impl.java.member.JavaMethod;
import net.sf.mmm.code.impl.java.member.JavaMethods;
import net.sf.mmm.code.impl.java.member.JavaOperation;
import net.sf.mmm.code.impl.java.source.JavaSource;
import net.sf.mmm.code.impl.java.type.JavaGenericType;
import net.sf.mmm.code.impl.java.type.JavaType;
import net.sf.mmm.code.impl.java.type.JavaTypeVariable;
import net.sf.mmm.code.impl.java.type.JavaTypeVariables;

/**
 * Default implementation of {@link JavaClassLoader}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaClassLoaderImpl implements JavaLoader {

  private static final Logger LOG = LoggerFactory.getLogger(JavaClassLoaderImpl.class);

  private final ClassLoader classLoader;

  /**
   * The constructor.
   */
  public JavaClassLoaderImpl() {

    this(Thread.currentThread().getContextClassLoader());
  }

  /**
   * The constructor.
   *
   * @param classLoader the underlying {@link ClassLoader}.
   */
  public JavaClassLoaderImpl(ClassLoader classLoader) {

    super();
    this.classLoader = classLoader;
  }

  @Override
  public JavaType getType(String qualifiedName) {

    try {
      Class<?> clazz = this.classLoader.loadClass(qualifiedName);
      CodeName name = new CodeName(qualifiedName, '.');
      JavaPackage pkg = getPackage(name.getParent().getFullName());
      JavaType type = createType(clazz, pkg);
      return type;
    } catch (ClassNotFoundException e) {
      LOG.debug("Failed to load type {}", qualifiedName, e);
      return null;
    }
  }

  @Override
  public JavaPackage getPackage(String qualifiedName) {

    return getPackage(new CodeName(qualifiedName, '.'));
  }

  private JavaPackage getPackage(CodeName name) {

    // TODO all nuts, redesign and rewrite
    Package pkg = Package.getPackage(name.getFullName());
    if (pkg == null) {
      return null;
    }
    JavaPackage parentPkg;
    CodeName parent = name.getParent();
    if (parent == null) {
      parentPkg = new JavaPackage((JavaSource) null, null, null);
    } else {
      parentPkg = getPackage(parent);
    }
    return new JavaPackage(parentPkg, name.getSimpleName(), pkg);
  }

  private JavaType createType(Class<?> clazz, JavaPackage parentPackage) {

    Class<?> declaringClass = clazz.getDeclaringClass();
    JavaType javaType;
    if (declaringClass != null) {
      JavaType declaringType = createType(declaringClass, parentPackage);
      javaType = declaringType.getNestedTypes().add(clazz.getSimpleName());
    } else {
      JavaFile file = new JavaFile(parentPackage, clazz.getSimpleName(), true);
      javaType = new JavaType(file);
      file.getTypes().add(javaType);
    }
    javaType.setModifiers(CodeModifiers.of(clazz.getModifiers()));
    javaType.setCategory(getCategory(clazz));
    createFields(javaType, clazz);
    createConstructors(javaType, clazz);
    createMethods(javaType, clazz);
    return javaType;
  }

  /**
   * @param javaType the {@link JavaType}.
   * @param clazz the {@link Class} to {@link Class#getDeclaredFields() analyze}.
   */
  private void createFields(JavaType javaType, Class<?> clazz) {

    Field[] declaredFields = clazz.getDeclaredFields();
    if (declaredFields.length == 0) {
      return;
    }
    JavaFields javaFields = javaType.getFields();
    for (Field field : declaredFields) {
      JavaField javaField = javaFields.add(field.getName());
      CodeModifiers modifiers = CodeModifiers.of(field.getModifiers());
      javaField.setModifiers(modifiers);
      JavaGenericType genericType = createType(field.getGenericType());
      javaField.setType(genericType);
      Object value = null;
      if (modifiers.isStatic() && modifiers.isFinal()) {
        try {
          field.setAccessible(true);
          value = field.get(null);
        } catch (Exception e) {
          LOG.warn("Failed to read static final field {}", field, e);
        }
      }
      if (value != null) {
        javaField.setInitializer(createLiteral(value, field.getType()));
      }
      createAnnoations(javaField, field);
    }
  }

  /**
   * @param javaType the {@link JavaType}.
   * @param clazz the {@link Class} to {@link Class#getDeclaredConstructors() analyze}.
   */
  private void createConstructors(JavaType javaType, Class<?> clazz) {

    Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
    if (declaredConstructors.length == 0) {
      return;
    }
    JavaConstructors javaConstructors = javaType.getConstructors();
    for (Constructor<?> constructor : declaredConstructors) {
      JavaConstructor javaConstructor = javaConstructors.add();
      createExecutable(javaConstructor, constructor);
    }
  }

  /**
   * @param JavaType the {@link JavaType}.
   * @param clazz the {@link Class} to {@link Class#getDeclaredMethods() analyze}.
   */
  private void createMethods(JavaType javaType, Class<?> clazz) {

    Method[] declaredMethods = clazz.getDeclaredMethods();
    if (declaredMethods.length == 0) {
      return;
    }
    JavaMethods javaMethods = javaType.getMethods();
    for (Method method : declaredMethods) {
      JavaMethod javaMethod = javaMethods.add(method.getName());
      createExecutable(javaMethod, method);
    }
  }

  private void createExecutable(JavaOperation javaOperation, Executable executable) {

    javaOperation.setModifiers(CodeModifiers.of(executable.getModifiers()));
    createAnnoations(javaOperation, executable);
    createParameters(javaOperation, executable);
    createTypeVariables(javaOperation, executable);
    executable.getTypeParameters();
    createBody(javaOperation, executable);
  }

  private void createTypeVariables(JavaElementWithTypeVariables javaElement, GenericDeclaration genericDeclaration) {

    TypeVariable<?>[] typeParameters = genericDeclaration.getTypeParameters();
    if (typeParameters.length == 0) {
      return;
    }
    JavaTypeVariables javaTypeVariables = javaElement.getTypeVariables();
    for (TypeVariable<?> typeParameter : typeParameters) {
      JavaTypeVariable javaTypeVariable = javaTypeVariables.add(typeParameter.getName());
      AnnotatedType[] annotatedBounds = typeParameter.getAnnotatedBounds();
    }
  }

  private void createBody(JavaOperation javaOperation, Executable executable) {
    // TODO create body? Read bytecode?
    // operation.setBody(body);
  }

  private void createParameters(JavaOperation javaOperation, Executable executable) {

    Parameter[] parameters = executable.getParameters();
    if (parameters.length == 0) {
      return;
    }
    JavaParameters javaParameters = javaOperation.getParameters();
    for (Parameter parameter : parameters) {
      JavaParameter javaParameter = javaParameters.add(parameter.getName());
      javaParameter.setType(createType(parameter.getParameterizedType()));
      createAnnoations(javaParameter, parameter);
    }
  }

  private JavaLiteral<?> createLiteral(Object value, Class<?> type) {

    return JavaLiteral.of(value, type.isPrimitive());
  }

  private void createAnnoations(JavaElement element, AnnotatedElement annotatedElement) {

    for (Annotation annotation : annotatedElement.getAnnotations()) {
      Class<? extends Annotation> annotationType = annotation.annotationType();
      JavaAnnotation javaAnnotation = element.getAnnotations().add(createType(annotationType));
      for (Method method : annotationType.getDeclaredMethods()) {
        String key = method.getName();
        try {
          Object value = method.invoke(annotation, (Object[]) null);
          javaAnnotation.getParameters().put(key, createLiteral(value, method.getReturnType()));
        } catch (Exception e) {
          LOG.warn("Failed to read attribute {} of annotation {}.", key, annotation, e);
        }
      }
    }
  }

  private JavaGenericType createType(Type type) {

    return null;
  }

  private JavaType createType(Class<?> type) {

    return null;
  }

  private static CodeTypeCategory getCategory(Class<?> clazz) {

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
    return category;
  }

}
