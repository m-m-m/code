/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.io.File;

import net.sf.mmm.code.api.expression.CodeExpression;
import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.base.loader.BaseLoader;
import net.sf.mmm.code.base.source.BaseSourceImpl;
import net.sf.mmm.code.base.source.BaseSourceProvider;
import net.sf.mmm.code.base.type.BaseType;
import net.sf.mmm.code.base.type.BaseTypeWildcard;
import net.sf.mmm.code.impl.java.source.maven.MavenClassLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link JavaContext} that inherits from a {@link #getParent() parent} context.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaExtendedContext extends JavaContext {

  private final JavaContext parent;

  private final JavaClassLoader loader;

  private static final Logger LOG = LoggerFactory.getLogger(JavaExtendedContext.class);

  /**
   * The constructor.
   *
   * @param source the {@link #getSource() source}.
   * @param sourceProvider the {@link BaseSourceProvider}.
   * @param isExternal
   */
  public JavaExtendedContext(BaseSourceImpl source, BaseSourceProvider sourceProvider, boolean isExternal) {

    this(JavaRootContext.get(), source, sourceProvider, isExternal);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent context}.
   * @param source the {@link #getSource() source}.
   * @param sourceProvider the {@link BaseSourceProvider}.
   * @param isExternal
   */
  public JavaExtendedContext(JavaContext parent, BaseSourceImpl source, BaseSourceProvider sourceProvider,
      boolean isExternal) {

    super(source, sourceProvider);

    this.parent = parent;

    if (isExternal) {
      // Create a File object on the root directory of the classes
      File byteCodeLocation = null;
      String byteCodeLocationString = source.getByteCodeLocation().toString();
      if (byteCodeLocationString.substring(byteCodeLocationString.lastIndexOf(File.separator) + 1)
          .equals("test-classes")) {
        byteCodeLocation = source.getByteCodeLocation().getParentFile().toPath().resolve("classes" + File.separator)
            .toFile();
      } else {
        byteCodeLocation = source.getByteCodeLocation();
      }

      try {
        MavenClassLoader mvnClassLoader = new MavenClassLoader(Thread.currentThread().getContextClassLoader(),
            byteCodeLocation.toURI().toURL());

        this.loader = new JavaClassLoader(mvnClassLoader);
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    } else {
      this.loader = new JavaClassLoader();
    }

  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent context}.
   * @param source the {@link #getSource() source}.
   * @param sourceProvider the {@link BaseSourceProvider}.
   */
  public JavaExtendedContext(JavaContext parent, BaseSourceImpl source, BaseSourceProvider sourceProvider,
      File mavenProjectLocation) {

    super(source, sourceProvider);
    this.parent = parent;
    this.loader = new JavaClassLoader();
  }

  @Override
  public BaseLoader getLoader() {

    return this.loader;
  }

  @Override
  protected BaseType getTypeFromCache(String qualifiedName) {

    BaseType type = super.getTypeFromCache(qualifiedName);
    if (type == null) {
      return this.parent.getTypeFromCache(qualifiedName);
    }
    return type;
  }

  @Override
  public JavaContext getParent() {

    return this.parent;
  }

  @Override
  public JavaRootContext getRootContext() {

    return this.parent.getRootContext();
  }

  @Override
  public CodeExpression createExpression(Object value, boolean primitive) {

    return this.parent.createExpression(value, primitive);
  }

  @Override
  public BaseTypeWildcard getUnboundedWildcard() {

    return this.parent.getUnboundedWildcard();
  }

  @Override
  public CodeLanguage getLanguage() {

    return this.parent.getLanguage();
  }

  @Override
  public BaseType getRootType() {

    return this.parent.getRootType();
  }

  @Override
  public BaseType getRootEnumerationType() {

    return this.parent.getRootEnumerationType();
  }

  @Override
  public BaseType getVoidType() {

    return this.parent.getVoidType();
  }

  @Override
  public BaseType getRootExceptionType() {

    return this.parent.getRootExceptionType();
  }

  @Override
  public BaseType getNonPrimitiveType(BaseType javaType) {

    return this.parent.getNonPrimitiveType(javaType);
  }

  @Override
  public String getQualifiedNameForStandardType(String simpleName, boolean omitStandardPackages) {

    return this.parent.getQualifiedNameForStandardType(simpleName, omitStandardPackages);
  }

  @Override
  public ClassLoader getClassLoader() {

    return this.loader.getClassLoader();
  }

}
