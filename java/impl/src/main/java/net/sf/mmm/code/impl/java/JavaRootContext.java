/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.io.File;
import java.security.CodeSource;
import java.util.function.Supplier;

import net.sf.mmm.code.api.expression.CodeExpression;
import net.sf.mmm.code.api.source.CodeSourceDescriptor;
import net.sf.mmm.code.api.syntax.CodeSyntax;
import net.sf.mmm.code.api.syntax.CodeSyntaxJava;
import net.sf.mmm.code.base.loader.BaseLoader;
import net.sf.mmm.code.base.loader.SourceCodeProvider;
import net.sf.mmm.code.base.source.BaseSource;
import net.sf.mmm.code.base.source.BaseSourceDescriptorType;
import net.sf.mmm.code.base.source.BaseSourceImpl;
import net.sf.mmm.code.base.type.BaseType;
import net.sf.mmm.code.base.type.BaseTypeWildcard;
import net.sf.mmm.code.impl.java.expression.constant.JavaConstant;
import net.sf.mmm.code.impl.java.loader.JavaSourceLoader;

/**
 * Implementation of {@link JavaContext} for the {@link #getRootContext() root context}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaRootContext extends JavaContext {

  private static JavaRootContext instance;

  private final JavaClassLoader loader;

  private BaseTypeWildcard unboundedWildcard;

  /**
   * The constructor.
   *
   * @param source the toplevel {@link #getSource() source}.
   */
  public JavaRootContext(BaseSourceImpl source) {

    super(source);
    this.loader = new JavaClassLoader(ClassLoader.getSystemClassLoader());
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
  protected BaseLoader getLoader() {

    return this.loader;
  }

  @Override
  public BaseSource getSource(String id) {

    BaseSource source = getSource();
    if (source.getId().equals(id)) {
      return source;
    }
    return null;
  }

  @Override
  protected BaseSource getOrCreateSource(CodeSource codeSource) {

    if (codeSource == null) {
      return getSource();
    }
    return createSource(codeSource);
  }

  @Override
  public BaseSource getOrCreateSource(String id, Supplier<BaseSource> sourceSupplier) {

    BaseSource source = getSource(id);
    if (source != null) {
      return source;
    }
    return createSource(id);
  }

  private BaseSource createSource(Object arg) {

    throw new IllegalStateException("Can not create source for external code in root context: " + arg);
  }

  @Override
  public CodeSyntax getSyntax() {

    return CodeSyntaxJava.INSTANCE;
  }

  @Override
  public CodeExpression createExpression(Object value, boolean primitive) {

    return JavaConstant.of(value, primitive);
  }

  @Override
  public BaseType getRootType() {

    return (BaseType) getType(Object.class);
  }

  @Override
  public BaseType getRootEnumerationType() {

    return (BaseType) getType(Enum.class);
  }

  @Override
  public BaseType getRootExceptionType() {

    return (BaseType) getType(Throwable.class);
  }

  @Override
  public BaseType getVoidType() {

    return (BaseType) getType(void.class);
  }

  @Override
  public BaseTypeWildcard getUnboundedWildcard() {

    if (this.unboundedWildcard == null) {
      this.unboundedWildcard = new BaseTypeWildcard(getRootType(), JavaConstants.UNBOUNDED_WILDCARD);
    }
    return this.unboundedWildcard;
  }

  @Override
  public BaseType getNonPrimitiveType(BaseType javaType) {

    if (javaType.isPrimitive()) {
      String qualifiedName = JavaConstants.JAVA_PRIMITIVE_TYPES_MAP.get(javaType.getSimpleName());
      if (qualifiedName == null) {
        throw new IllegalArgumentException(javaType.toString());
      }
      return getType(qualifiedName);
    }
    return javaType;
  }

  @Override
  public String getQualifiedNameForStandardType(String simpleName, boolean omitStandardPackages) {

    if (JavaConstants.JAVA_LANG_TYPES.contains(simpleName)) {
      if (omitStandardPackages) {
        return simpleName;
      } else {
        return "java.lang." + simpleName;
      }
    }
    if (JavaConstants.JAVA_PRIMITIVE_TYPES_MAP.containsKey(simpleName)) {
      return simpleName;
    }
    return null;
  }

  private static BaseSourceImpl createRootSource() {

    SourceCodeProvider sourceCodeProvider = null; // TODO
    JavaSourceLoader loader = new JavaSourceLoader(sourceCodeProvider);
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
    CodeSourceDescriptor descriptor = new BaseSourceDescriptorType(groupId, artifactId, version, null, docUrl);
    return new BaseSourceImpl(byteCodeLocation, sourceCodeLocation, id, descriptor, loader);
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

  /**
   * @return the default instance of this class.
   */
  public static JavaRootContext get() {

    if (instance == null) {
      BaseSourceImpl source = createRootSource();
      instance = new JavaRootContext(source);
    }
    return instance;
  }

}
