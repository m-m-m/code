/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.io.File;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.CodeSource;
import java.time.Clock;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.Deque;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.mmm.code.api.CodeName;
import net.sf.mmm.code.api.source.CodeSourceDescriptor;
import net.sf.mmm.code.base.source.CodeSourceDescriptorType;
import net.sf.mmm.code.impl.java.source.JavaSource;
import net.sf.mmm.code.impl.java.type.JavaGenericType;
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

  private static JavaRootContext instance;

  private final Map<String, JavaTypeContainer> javaSystemTypeCache;

  private JavaType voidType;

  private JavaType rootExceptionType;

  private JavaType rootType;

  /**
   * The constructor.
   */
  private JavaRootContext() {

    super(new JavaClassLoader(ClassLoader.getSystemClassLoader()), createSource()); // TODO
    this.javaSystemTypeCache = new HashMap<>();
    add2SystemTypeCache(Object.class, String.class, Enum.class, Class.class, Package.class, Number.class, //
        Void.class, Boolean.class, Short.class, Byte.class, Double.class, Float.class, Long.class, Integer.class, Character.class, //
        void.class, boolean.class, short.class, byte.class, double.class, float.class, long.class, int.class, char.class, //
        Throwable.class, Exception.class, RuntimeException.class, //
        CharSequence.class, StringBuilder.class, StringBuffer.class, Appendable.class, //
        Iterable.class, AutoCloseable.class, Comparable.class, Cloneable.class, //
        Override.class, FunctionalInterface.class, Deprecated.class, //
        Thread.class, Runnable.class, System.class, Runtime.class, //
        Math.class, StrictMath.class, //
        Collection.class, List.class, Set.class, Map.class, Iterator.class, Comparator.class, Enumeration.class, //
        Queue.class, Deque.class, SortedSet.class, NavigableSet.class, SortedMap.class, NavigableMap.class, //
        ArrayList.class, HashSet.class, HashMap.class, Properties.class, //
        Date.class, Currency.class, UUID.class, StringTokenizer.class, Optional.class, Objects.class, Arrays.class, //
        BigInteger.class, BigDecimal.class, //
        Pattern.class, Matcher.class, //
        Predicate.class, Supplier.class, Function.class, //
        Month.class, Year.class, YearMonth.class, MonthDay.class, DayOfWeek.class, Period.class, Duration.class, Clock.class, //
        Instant.class, LocalDate.class, LocalDateTime.class, LocalTime.class, OffsetDateTime.class, OffsetTime.class, ZonedDateTime.class, //
        ZoneId.class, ZoneOffset.class, //
        Array.class, Modifier.class, Type.class, Field.class, Method.class, Constructor.class, Parameter.class, AnnotatedElement.class, AnnotatedType.class);
  }

  private static JavaSource createSource() {

    String id = System.getProperty("java.home");
    File byteCodeLocation = new File(id);
    String version = System.getProperty("java.version");
    String majorVersion = getJavaMajorVersion(version);
    String docUrl = "http://docs.oracle.com/javase/" + majorVersion + "/docs/api/";
    String groupId = "java";
    String artifactId = "jre";
    File jdkHome = byteCodeLocation.getParentFile();
    File srcZip = new File(jdkHome, "src.zip");
    File sourceCodeLocation = null;
    if (srcZip.isFile()) {
      artifactId = "jdk";
      sourceCodeLocation = srcZip;
    }
    CodeSourceDescriptor descriptor = new CodeSourceDescriptorType(groupId, artifactId, version, docUrl);
    return new JavaSource(byteCodeLocation, sourceCodeLocation, id, descriptor);
  }

  private static String getJavaMajorVersion(String version) {

    String majorVersion;
    if (version.startsWith("1.")) {
      majorVersion = version.substring(2);
    } else {
      majorVersion = version;
    }
    int dotIndex = majorVersion.indexOf('.');
    if (dotIndex > 0) {
      majorVersion = majorVersion.substring(0, dotIndex);
    }
    int underscoreIndex = majorVersion.indexOf('_');
    if (underscoreIndex > 0) {
      majorVersion = majorVersion.substring(0, underscoreIndex);
    }
    return majorVersion;
  }

  private void add2SystemTypeCache(Class<?>... javaClasses) {

    for (Class<?> javaClass : javaClasses) {
      add2SystemTypeCache(javaClass);
    }
  }

  private void add2SystemTypeCache(Class<?> javaClass) {

    assert (javaClass.isPrimitive() || javaClass.getPackage().getName().startsWith("java."));
    assert (javaClass.getProtectionDomain().getCodeSource() == null);
    JavaTypeContainer container = new JavaTypeContainer(javaClass);
    if (javaClass.isPrimitive()) {
      container.javaType = createPrimitiveType(javaClass);
    }
    this.javaSystemTypeCache.put(javaClass.getName(), container);
  }

  private JavaType createPrimitiveType(Class<?> clazz) {

    assert (clazz.isPrimitive());
    JavaPackage rootPackage = getRootPackage();
    JavaPathElements children = rootPackage.getChildren();
    JavaFile file = new JavaFile(rootPackage, clazz);
    file.setImmutable();
    children.addInternal(file);
    return file.getType();
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
  public JavaRootContext getRootContext() {

    return this;
  }

  @Override
  public JavaType getVoidType() {

    if (this.voidType == null) {
      this.voidType = this.javaSystemTypeCache.get(PRIMITVE_TYPE_VOID).javaType;
    }
    return this.voidType;
  }

  private JavaType getType(JavaTypeContainer typeContainer) {

    if (typeContainer.javaType == null) {
      typeContainer.javaType = (JavaType) super.getType(typeContainer.javaClass);
    }
    return typeContainer.javaType;
  }

  @Override
  public JavaGenericType getType(Class<?> clazz) {

    JavaType type = getTypeFromCache(clazz.getName());
    if (type != null) {
      return type;
    }
    return super.getType(clazz);
  }

  @Override
  public JavaType getType(String qualifiedName) {

    JavaType type = getTypeFromCache(qualifiedName);
    if (type != null) {
      return type;
    }
    return getType(parseName(qualifiedName), false);
  }

  private JavaType getTypeFromCache(String qualifiedName) {

    JavaTypeContainer typeContainer = this.javaSystemTypeCache.get(qualifiedName);
    if (typeContainer != null) {
      return getType(typeContainer);
    }
    return null;
  }

  @Override
  public JavaType getType(CodeName qualifiedName) {

    return getType(qualifiedName, true);
  }

  private JavaType getType(CodeName qualifiedName, boolean lookup) {

    if (lookup) {
      JavaType type = getTypeFromCache(qualifiedName.getFullName());
      if (type != null) {
        return type;
      }
    }
    return super.getType(qualifiedName);
  }

  @Override
  protected JavaSource getOrCreateSource(CodeSource codeSource) {

    if (codeSource == null) {
      return getSource();
    }
    throw new IllegalStateException("Can not create source for external code in root context: " + codeSource.getLocation());
  }

  @Override
  public JavaType getRootType() {

    if (this.rootType == null) {
      this.rootType = getTypeFromCache("java.lang.Object");
    }
    return this.rootType;
  }

  @Override
  public JavaType getRootExceptionType() {

    if (this.rootExceptionType == null) {
      this.rootExceptionType = getTypeFromCache("java.lang.Throwable");
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

  private static class JavaTypeContainer {

    private final Class<?> javaClass;

    private JavaType javaType;

    private JavaTypeContainer(Class<?> javaClass) {

      super();
      this.javaClass = javaClass;
    }
  }

  /**
   * @return
   */
  static JavaRootContext get() {

    if (instance == null) {
      instance = new JavaRootContext();
    }
    return instance;
  }

}
