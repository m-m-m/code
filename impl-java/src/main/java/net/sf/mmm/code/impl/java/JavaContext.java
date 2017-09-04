/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.mmm.code.api.CodePackage;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.base.AbstractCodeContext;
import net.sf.mmm.code.impl.java.type.JavaType;
import net.sf.mmm.code.impl.java.type.JavaTypeVariables;

/**
 * Implementation of {@link net.sf.mmm.code.api.CodeContext} for Java.
 *
 * @author hohwille
 * @since 1.0.0
 */
public class JavaContext extends AbstractCodeContext<JavaType, JavaPackage> {

  private static final Set<String> JAVA_PRIMITIVE_TYPES = new HashSet<>(Arrays.asList("void", "boolean", "int", "long", "char", "float", "double"));

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

  private final JavaTypeVariables emptyTypeVariables;

  /**
   * The constructor.
   */
  public JavaContext() {

    super();
    JavaPackage rootPackage = new JavaPackage(this);
    setRootPackage(rootPackage);
    this.emptyTypeVariables = new JavaTypeVariables(this);
  }

  @Override
  public JavaType getType(CodePackage parentPackage, String simpleName) {

    JavaPackage pkg = (JavaPackage) parentPackage;
    return pkg.getType(simpleName);
  }

  @Override
  public JavaType createType(CodePackage parentPackage, String simpleName) {

    JavaPackage pkg = (JavaPackage) parentPackage;
    JavaFile file = new JavaFile(pkg, simpleName);
    JavaType type = new JavaType(file);
    file.getTypes().add(type);
    return type;
  }

  @Override
  public List<JavaPackage> getChildPackages(CodePackage pkg) {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<JavaType> getChildTypes(CodePackage pkg) {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public JavaPackage getPackage(CodePackage parentPackage, String simpleName) {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public JavaPackage createPackage(CodePackage parentPackage, String simpleName) {

    JavaPackage parent = (JavaPackage) parentPackage;
    JavaPackage pkg = new JavaPackage(parent, simpleName);
    return pkg;
  }

  @Override
  public JavaImport createImport(CodeType type) {

    return new JavaImport(this, type.getQualifiedName(), false);
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
    if (JAVA_PRIMITIVE_TYPES.contains(simpleName)) {
      return simpleName;
    }
    return null;
  }

  /**
   * @return an instance of {@link JavaTypeVariables} that is always empty and
   *         {@link JavaTypeVariables#isImmutable() immutable}.
   */
  public JavaTypeVariables getEmptyTypeVariables() {

    return this.emptyTypeVariables;
  }

  /**
   * @param javaType the {@link JavaType} that might be {@link JavaType#isPrimitive() primitive}.
   * @return the corresponding {@link JavaType#getNonPrimitiveType() non-primitive type}.
   */
  public JavaType getNonPrimitiveType(JavaType javaType) {

    if (!javaType.isPrimitive()) {
      return javaType;
    }
    // TODO Auto-generated method stub
    return null;
  }
}
