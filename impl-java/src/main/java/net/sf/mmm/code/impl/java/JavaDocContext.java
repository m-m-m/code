/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaSource;

import net.sf.mmm.code.base.doc.CodeDocContext;
import net.sf.mmm.code.base.doc.CodeDocDescriptor;

/**
 * Implementation of {@link CodeDocContext} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaDocContext extends CodeDocContext {

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

  private final JavaClass javaClass;

  /**
   * The constructor.
   *
   * @param docDescriptors the {@link List} of {@link CodeDocDescriptor}s.
   * @param javaClass the source {@link JavaClass}.
   */
  public JavaDocContext(List<CodeDocDescriptor> docDescriptors, JavaClass javaClass) {

    super('.', docDescriptors);
    this.javaClass = javaClass;
  }

  @Override
  protected String getSimpleName() {

    return this.javaClass.getSimpleName();
  }

  @Override
  protected Pattern getTagPattern() {

    return PATTERN_JAVADOC_TAG;
  }

  @Override
  protected String qualify(String simpleName) {

    if (getSimpleName().equals(simpleName)) {
      return this.javaClass.getFullyQualifiedName();
    }
    JavaSource source = this.javaClass.getSource();
    for (String qualifiedName : source.getImports()) {
      if (qualifiedName.endsWith(simpleName)) {
        char separator = qualifiedName.charAt(qualifiedName.length() - simpleName.length() - 1);
        if (separator == '.') {
          return qualifiedName;
        }
      }
    }
    String packageName = source.getPackageName();
    if (packageName.isEmpty()) {
      return simpleName;
    }
    if (JAVA_LANG_TYPES.contains(simpleName)) {
      return simpleName;
    }
    return packageName + '.' + simpleName;
  }

}
