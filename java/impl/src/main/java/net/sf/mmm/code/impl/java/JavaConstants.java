/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Constants for Java and JDK.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
class JavaConstants {

  private static final Logger LOG = LoggerFactory.getLogger(JavaConstants.class);

  static final String PRIMITVE_TYPE_DOUBLE = "double";

  static final String PRIMITVE_TYPE_FLOAT = "float";

  static final String PRIMITVE_TYPE_CHAR = "char";

  static final String PRIMITVE_TYPE_LONG = "long";

  static final String PRIMITVE_TYPE_INT = "int";

  static final String PRIMITVE_TYPE_SHORT = "short";

  static final String PRIMITVE_TYPE_BYTE = "byte";

  static final String PRIMITVE_TYPE_BOOLEAN = "boolean";

  static final String PRIMITVE_TYPE_VOID = "void";

  static final Set<String> JAVA_LANG_TYPES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("AbstractMethodError", "AbstractStringBuilder",
      "Appendable", "ArithmeticException", "ArrayIndexOutOfBoundsException", "ArrayStoreException", "AssertionError", "AutoCloseable", "Boolean",
      "BootstrapMethodError", "Byte", "Character", "CharSequence", "Class", "ClassCastException", "ClassCircularityError", "ClassFormatError", "ClassLoader",
      "ClassNotFoundException", "ClassValue", "Cloneable", "CloneNotSupportedException", "Comparable", "Compiler", "Deprecated", "Double", "Enum",
      "EnumConstantNotPresentException", "Error", "Exception", "ExceptionInInitializerError", "Float", "FunctionalInterface", "IllegalAccessError",
      "IllegalAccessException", "IllegalArgumentException", "IllegalMonitorStateException", "IllegalStateException", "IllegalThreadStateException",
      "IncompatibleClassChangeError", "IndexOutOfBoundsException", "InheritableThreadLocal", "InstantiationError", "InstantiationException", "Integer",
      "InternalError", "InterruptedException", "Iterable", "LinkageError", "Long", "Math", "NegativeArraySizeException", "NoClassDefFoundError",
      "NoSuchFieldError", "NoSuchFieldException", "NoSuchMethodError", "NoSuchMethodException", "NullPointerException", "Number", "NumberFormatException",
      "Object", "OutOfMemoryError", "Override", "Package", "Process", "ProcessBuilder", "Readable", "ReflectiveOperationException", "Runnable", "Runtime",
      "RuntimeException", "RuntimePermission", "SafeVarargs", "SecurityException", "SecurityManager", "Short", "StackOverflowError", "StackTraceElement",
      "StrictMath", "String", "StringBuffer", "StringBuilder", "StringIndexOutOfBoundsException", "SuppressWarnings", "System", "Thread", "ThreadDeath",
      "ThreadGroup", "ThreadLocal", "Throwable", "TypeNotPresentException", "UnknownError", "UnsatisfiedLinkError", "UnsupportedClassVersionError",
      "UnsupportedOperationException", "VerifyError", "VirtualMachineError", "Void")));

  static final Map<String, String> JAVA_PRIMITIVE_TYPES_MAP = Collections.unmodifiableMap(createPrimitiveTypesMap());

  static final List<Class<?>> PRIMITIVE_TYPES = Collections
      .unmodifiableList(Arrays.asList(void.class, boolean.class, char.class, int.class, long.class, short.class, byte.class, double.class, float.class));

  static final WildcardType UNBOUNDED_WILDCARD;

  public Class<?> type;

  static {

    WildcardType wildcard = null;
    try {
      ParameterizedType pType = (ParameterizedType) JavaConstants.class.getField("type").getGenericType();
      wildcard = (WildcardType) pType.getActualTypeArguments()[0];
    } catch (Exception e) {
      LOG.error("Failed to retrieve unbounded wildcard type via reflection - JVM broken?", e);
    }
    UNBOUNDED_WILDCARD = wildcard;
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

}
