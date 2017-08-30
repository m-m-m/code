/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.doc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import net.sf.mmm.code.base.doc.AbstractCodeDoc;
import net.sf.mmm.code.impl.java.JavaContext;
import net.sf.mmm.code.impl.java.JavaElement;

/**
 * Implementation of {@link net.sf.mmm.code.api.doc.CodeDoc} for Java.
 *
 * @author hohwille
 * @since 1.0.0
 */
public class JavaDoc extends AbstractCodeDoc<JavaContext> {

  private static final Pattern PATTERN_JAVADOC_TAG = Pattern.compile("\\{@([a-zA-Z]+) ([^}]*)\\}");

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

  private static final Set<String> JAVA_PRIMITIVE_TYPES = new HashSet<>(Arrays.asList("void", "boolean", "int", "long", "char", "float", "double"));

  /**
   * The constructor.
   *
   * @param element the owning {@link #getElement() element}.
   */
  public JavaDoc(JavaElement element) {

    super(element.getContext(), element);
  }

  @Override
  protected Pattern getTagPattern() {

    return PATTERN_JAVADOC_TAG;
  }

  @Override
  protected String qualifyStandardType(String simpleName) {

    if (JAVA_LANG_TYPES.contains(simpleName)) {
      return "java.lang." + simpleName;
    }
    if (JAVA_PRIMITIVE_TYPES.contains(simpleName)) {
      return simpleName;
    }
    return null;
  }

}
