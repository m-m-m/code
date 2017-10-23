/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.io.File;
import java.net.URL;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.Map;

import net.sf.mmm.code.impl.java.source.JavaSource;
import net.sf.mmm.code.impl.java.source.JavaSourceProvider;
import net.sf.mmm.code.impl.java.type.JavaType;
import net.sf.mmm.code.impl.java.type.JavaTypeWildcard;
import net.sf.mmm.util.exception.api.DuplicateObjectException;

/**
 * {@link JavaContext} that inherits from a {@link #getParent() parent} context.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaExtendedContext extends JavaContext {

  private final JavaContext parent;

  private final JavaSourceProvider sourceProvider;

  private final Map<String, JavaSource> sourceMap;

  /**
   * The constructor.
   *
   * @param sourceProvider the {@link JavaSourceProvider}.
   * @param loader the {@link #getLoader() loader}.
   * @param source the {@link #getSource() source}.
   */
  public JavaExtendedContext(JavaSourceProvider sourceProvider, AbstractJavaCodeLoader loader, JavaSource source) {

    this(JavaRootContext.get(), sourceProvider, loader, source);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent context}.
   * @param sourceProvider the {@link JavaSourceProvider}.
   * @param loader the {@link #getLoader() loader}.
   * @param source the {@link #getSource() source}.
   */
  public JavaExtendedContext(JavaContext parent, JavaSourceProvider sourceProvider, AbstractJavaCodeLoader loader, JavaSource source) {

    super(loader, source);
    this.parent = parent;
    this.sourceProvider = sourceProvider;
    this.sourceProvider.setContext(this);
    this.sourceMap = new HashMap<>();
    registerSource(source);
  }

  private void registerSource(JavaSource source) {

    JavaSource duplicate = this.sourceMap.put(source.getId(), source);
    if (duplicate != null) {
      throw new DuplicateObjectException(source, source.getId(), duplicate);
    }
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
  protected JavaSource getOrCreateSource(CodeSource codeSource) {

    if (codeSource == null) {
      return getRootContext().getSource();
    }
    URL location = codeSource.getLocation();
    String uri = location.toString();
    JavaSource source = getSource(uri);
    if (source == null) {
      source = this.sourceProvider.create(codeSource);
      registerSource(source);
    }
    return source;
  }

  /**
   * This is an internal method that should only be used from implementations of {@link JavaSourceProvider}.
   *
   * @param byteCodeLocation the {@link JavaSource#getByteCodeLocation() byte code location}.
   * @param sourceCodeLocation the {@link JavaSource#getSourceCodeLocation() source code location}.
   * @return the existing or otherwise created {@link JavaSource}.
   */
  public JavaSource getOrCreateSource(File byteCodeLocation, File sourceCodeLocation) {

    String id;
    if (byteCodeLocation != null) {
      id = byteCodeLocation.toString();
    } else {
      id = sourceCodeLocation.toString();
    }
    JavaSource source = getSource(id);
    if (source == null) {
      source = this.sourceProvider.create(byteCodeLocation, sourceCodeLocation);
      registerSource(source);
    }
    return source;
  }

  JavaSource getSource(String id) {

    JavaSource javaSource = this.sourceMap.get(id);
    if (javaSource != null) {
      return javaSource;
    }
    if (this.parent instanceof JavaExtendedContext) {
      javaSource = ((JavaExtendedContext) this.parent).getSource(id);
    }
    return javaSource;
  }

  @Override
  public JavaType getRootType() {

    return getRootContext().getRootType();
  }

  @Override
  public JavaType getVoidType() {

    return getRootContext().getVoidType();
  }

  @Override
  public JavaType getRootExceptionType() {

    return getRootContext().getRootExceptionType();
  }

  @Override
  public JavaType getRootEnumerationType() {

    return getRootContext().getRootEnumerationType();
  }

  @Override
  public JavaTypeWildcard getUnboundedWildcard() {

    return getRootContext().getUnboundedWildcard();
  }

  @Override
  public JavaType getNonPrimitiveType(JavaType javaType) {

    return getRootContext().getNonPrimitiveType(javaType);
  }

  @Override
  public String getQualifiedNameForStandardType(String simpleName, boolean omitStandardPackages) {

    return getRootContext().getQualifiedNameForStandardType(simpleName, omitStandardPackages);
  }
}
