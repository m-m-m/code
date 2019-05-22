/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.io.File;

import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.api.language.JavaLanguage;
import net.sf.mmm.code.api.source.CodeSourceDescriptor;
import net.sf.mmm.code.base.loader.BaseLoader;
import net.sf.mmm.code.base.loader.SourceCodeProvider;
import net.sf.mmm.code.base.source.BaseSourceDescriptorType;
import net.sf.mmm.code.base.source.BaseSourceImpl;
import net.sf.mmm.code.base.type.BaseType;
import net.sf.mmm.code.base.type.BaseTypeWildcard;
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

  private final JavaFactory factory;

  private BaseTypeWildcard unboundedWildcard;

    /**
     * The constructor.
     *
     * @param source
     *            the toplevel {@link #getSource() source}.
     */
    public JavaRootContext(BaseSourceImpl source) {

    super(source);
    this.loader = new JavaClassLoader(ClassLoader.getSystemClassLoader());
    this.factory = new JavaFactory();
    for (Class<?> primitive : JavaConstants.PRIMITIVE_TYPES) {
      getType(primitive);
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

        return loader;
    }

    @Override
    public CodeLanguage getLanguage() {

    return JavaLanguage.get();
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
  public BaseType getBooleanType(boolean primitive) {

    if (primitive) {
      return (BaseType) getType(boolean.class);
    } else {
      return (BaseType) getType(Boolean.class);
    }
  }

  @Override
  public BaseTypeWildcard getUnboundedWildcard() {

        if (unboundedWildcard == null) {
            unboundedWildcard = new BaseTypeWildcard(getRootType(), JavaConstants.UNBOUNDED_WILDCARD);
        }
        return unboundedWildcard;
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
        String javaHome = System.getProperty("java.home");
        File byteCodeLocation = new File(javaHome);
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
        return new BaseSourceImpl(byteCodeLocation, sourceCodeLocation, null, descriptor, loader);
    }

  @Override
  public JavaFactory getFactory() {

    return this.factory;
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

    @Override
    public ClassLoader getClassLoader() {
        return loader.getClassLoader();
    }

}
