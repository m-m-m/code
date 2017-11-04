/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.io.File;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.Map;

import net.sf.mmm.code.base.source.BaseSource;
import net.sf.mmm.code.base.source.BaseSourceImpl;
import net.sf.mmm.code.base.type.BaseType;
import net.sf.mmm.code.base.type.BaseTypeWildcard;
import net.sf.mmm.code.impl.java.loader.AbstractJavaCodeLoader;
import net.sf.mmm.code.impl.java.source.BaseSourceProvider;
import net.sf.mmm.util.exception.api.DuplicateObjectException;

/**
 * {@link JavaContext} that inherits from a {@link #getParent() parent} context.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaExtendedContext extends JavaContext {

  private final JavaContext parent;

  private final BaseSourceProvider sourceProvider;

  private final Map<String, BaseSource> sourceMap;

  /**
   * The constructor.
   *
   * @param sourceProvider the {@link BaseSourceProvider}.
   * @param loader the {@link #getLoader() loader}.
   * @param source the {@link #getSource() source}.
   */
  public JavaExtendedContext(BaseSourceProvider sourceProvider, AbstractJavaCodeLoader loader, BaseSourceImpl source) {

    this(JavaRootContext.get(), sourceProvider, loader, source);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent context}.
   * @param sourceProvider the {@link BaseSourceProvider}.
   * @param loader the {@link #getLoader() loader}.
   * @param source the {@link #getSource() source}.
   */
  public JavaExtendedContext(JavaContext parent, BaseSourceProvider sourceProvider, AbstractJavaCodeLoader loader, BaseSourceImpl source) {

    super(loader, source);
    this.parent = parent;
    this.sourceProvider = sourceProvider;
    this.sourceProvider.setContext(this);
    this.sourceMap = new HashMap<>();
    registerSource(source);
  }

  private void registerSource(BaseSource source) {

    BaseSource duplicate = this.sourceMap.put(source.getId(), source);
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
  protected BaseSource getOrCreateSource(CodeSource codeSource) {

    if (codeSource == null) {
      return getRootContext().getSource();
    }
    String id = BaseSourceImpl.getNormalizedId(codeSource);
    BaseSource source = getSource(id);
    if (source == null) {
      source = this.sourceProvider.create(codeSource);
      registerSource(source);
    }
    return source;
  }

  /**
   * This is an internal method that should only be used from implementations of {@link BaseSourceProvider}.
   *
   * @param byteCodeLocation the {@link BaseSource#getByteCodeLocation() byte code location}.
   * @param sourceCodeLocation the {@link BaseSource#getSourceCodeLocation() source code location}.
   * @return the existing or otherwise created {@link BaseSource}.
   */
  public BaseSource getOrCreateSource(File byteCodeLocation, File sourceCodeLocation) {

    String id;
    if (byteCodeLocation != null) {
      id = byteCodeLocation.toString();
    } else {
      id = sourceCodeLocation.toString();
    }
    BaseSource source = getSource(id);
    if (source == null) {
      source = this.sourceProvider.create(byteCodeLocation, sourceCodeLocation);
      registerSource(source);
    }
    return source;
  }

  BaseSource getSource(String id) {

    BaseSource javaSource = this.sourceMap.get(id);
    if (javaSource != null) {
      return javaSource;
    }
    if (this.parent instanceof JavaExtendedContext) {
      javaSource = ((JavaExtendedContext) this.parent).getSource(id);
    }
    return javaSource;
  }

  @Override
  public BaseType getRootType() {

    return getRootContext().getRootType();
  }

  @Override
  public BaseType getVoidType() {

    return getRootContext().getVoidType();
  }

  @Override
  public BaseType getRootExceptionType() {

    return getRootContext().getRootExceptionType();
  }

  @Override
  public BaseType getRootEnumerationType() {

    return getRootContext().getRootEnumerationType();
  }

  @Override
  public BaseTypeWildcard getUnboundedWildcard() {

    return getRootContext().getUnboundedWildcard();
  }

  @Override
  public BaseType getNonPrimitiveType(BaseType javaType) {

    return getRootContext().getNonPrimitiveType(javaType);
  }

  @Override
  public String getQualifiedNameForStandardType(String simpleName, boolean omitStandardPackages) {

    return getRootContext().getQualifiedNameForStandardType(simpleName, omitStandardPackages);
  }
}
