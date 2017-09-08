/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link JavaContext} for the {@link #getParent() root} context.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaRootContext extends JavaContext {

  private static final String PRIMITVE_TYPE_DOUBLE = "double";

  private static final String PRIMITVE_TYPE_FLOAT = "float";

  private static final String PRIMITVE_TYPE_CHAR = "char";

  private static final String PRIMITVE_TYPE_LONG = "long";

  private static final String PRIMITVE_TYPE_INT = "int";

  private static final String PRIMITVE_TYPE_SHORT = "short";

  private static final String PRIMITVE_TYPE_BYTE = "byte";

  private static final String PRIMITVE_TYPE_BOOLEAN = "boolean";

  private static final String PRIMITVE_TYPE_VOID = "void";

  private static final Map<String, String> JAVA_PRIMITIVE_TYPES_MAP = createPrimitiveTypesMap();

  private static final Set<String> JAVA_LANG_TYPES = new HashSet<>(Arrays.asList("AbstractMethodError", "AbstractStringBuilder", "Appendable",
      "ArithmeticException", "ArrayIndexOutOfBoundsException", "ArrayStoreException", "AssertionError", "AutoCloseable", "Boolean", "BootstrapMethodError",
      "Byte", "Character", "CharSequence", "Class", "ClassCastException", "ClassCircularityError", "ClassFormatError", "ClassLoader", "ClassNotFoundException",
      "ClassValue", "Cloneable", "CloneNotSupportedException", "Comparable", "Compiler", "Deprecated", "Double", "Enum", "EnumConstantNotPresentException",
      "Error", "Exception", "ExceptionInInitializerError", "Float", "FunctionalInterface", "IllegalAccessError", "IllegalAccessException",
      "IllegalArgumentException", "IllegalMonitorStateException", "IllegalStateException", "IllegalThreadStateException", "IncompatibleClassChangeError",
      "IndexOutOfBoundsException", "InheritableThreadLocal", "InstantiationError", "InstantiationException", "Integer", "InternalError", "InterruptedException",
      "Iterable", "LinkageError", "Long", "Math", "NegativeArraySizeException", "NoClassDefFoundError", "NoSuchFieldError", "NoSuchFieldException",
      "NoSuchMethodError", "NoSuchMethodException", "NullPointerException", "Number", "NumberFormatException", "Object", "OutOfMemoryError", "Override",
      "Package", "Process", "ProcessBuilder", "Readable", "ReflectiveOperationException", "Runnable", "Runtime", "RuntimeException", "RuntimePermission",
      "SafeVarargs", "SecurityException", "SecurityManager", "Short", "StackOverflowError", "StackTraceElement", "StrictMath", "String", "StringBuffer",
      "StringBuilder", "StringIndexOutOfBoundsException", "SuppressWarnings", "System", "Thread", "ThreadDeath", "ThreadGroup", "ThreadLocal", "Throwable",
      "TypeNotPresentException", "UnknownError", "UnsatisfiedLinkError", "UnsupportedClassVersionError", "UnsupportedOperationException", "VerifyError",
      "VirtualMachineError", "Void"));

  private final JavaType voidType;

  private JavaType rootExceptionType;

  private JavaType rootType;

  /**
   * The constructor.
   */
  public JavaRootContext() {

    super(null, null); // TODO
    this.voidType = createPrimitiveType(PRIMITVE_TYPE_VOID, void.class);
    createPrimitiveType(PRIMITVE_TYPE_BOOLEAN, boolean.class);
    createPrimitiveType(PRIMITVE_TYPE_LONG, long.class);
    createPrimitiveType(PRIMITVE_TYPE_INT, int.class);
    createPrimitiveType(PRIMITVE_TYPE_SHORT, short.class);
    createPrimitiveType(PRIMITVE_TYPE_BYTE, byte.class);
    createPrimitiveType(PRIMITVE_TYPE_DOUBLE, double.class);
    createPrimitiveType(PRIMITVE_TYPE_FLOAT, float.class);
    createPrimitiveType(PRIMITVE_TYPE_CHAR, char.class);
  }

  private JavaType createPrimitiveType(String name, Class<?> clazz) {

    assert (clazz.isPrimitive());
    JavaPackage rootPackage = getRootPackage();
    JavaPathElements children = rootPackage.getChildren();
    JavaFile file = new JavaFile(rootPackage, name, false);
    JavaType type = new JavaType(file, clazz);
    file.getTypes().add(type);
    file.setImmutable();
    children.addInternal(file);
    return type;
  }

  private static Map<String, String> createPrimitiveTypesMap() {

    Map<String, String> map = new HashMap<>();
    map.put(PRIMITVE_TYPE_VOID, "java.lang.Void");
    map.put(PRIMITVE_TYPE_BOOLEAN, "java.lang.Boolean");
    map.put(PRIMITVE_TYPE_LONG, "java.lang.Long");
    map.put(PRIMITVE_TYPE_INT, "java.lang.Integer");
    map.put(PRIMITVE_TYPE_SHORT, "java.lang.Short");
    map.put(PRIMITVE_TYPE_BYTE, "java.lang.Byte");
    map.put(PRIMITVE_TYPE_DOUBLE, "java.lang.Double");
    map.put(PRIMITVE_TYPE_FLOAT, "java.lang.Float");
    map.put(PRIMITVE_TYPE_CHAR, "java.lang.Character");
    return map;
  }

  @Override
  public JavaContext getParent() {

    return null;
  }

  @Override
  public JavaType getVoidType() {

    return this.voidType;
  }

  @Override
  public JavaType getRootType() {

    if (this.rootType == null) {
      this.rootType = getRootPackage().getChildren().createPackage("java").getChildren().createPackage("lang").getChildren().createType("Object");
    }
    return this.rootType;
  }

  @Override
  public JavaType getRootExceptionType() {

    if (this.rootExceptionType == null) {
      this.rootExceptionType = getType("java.lang.Throwable");
    }
    return this.rootExceptionType;
  }

  /**
   * @param javaType the {@link JavaType} that might be {@link JavaType#isPrimitive() primitive}.
   * @return the corresponding {@link JavaType#getNonPrimitiveType() non-primitive type}.
   */
  @Override
  public JavaType getNonPrimitiveType(JavaType javaType) {

    if (javaType.isPrimitive()) {
      String qualifiedName = JAVA_PRIMITIVE_TYPES_MAP.get(javaType.getSimpleName());
      if (qualifiedName == null) {
        throw new IllegalArgumentException(javaType.toString());
      }
      return getType(qualifiedName);
    }
    return javaType;
  }

  @Override
  public String getQualifiedNameForStandardType(String simpleName, boolean omitStandardPackages) {

    if (JAVA_LANG_TYPES.contains(simpleName)) {
      if (omitStandardPackages) {
        return simpleName;
      } else {
        return "java.lang." + simpleName;
      }
    }
    if (JAVA_PRIMITIVE_TYPES_MAP.containsKey(simpleName)) {
      return simpleName;
    }
    return null;
  }

}
