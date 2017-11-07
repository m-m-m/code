/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.io.File;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import net.sf.mmm.code.api.expression.CodeExpression;
import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.base.loader.BaseLoader;
import net.sf.mmm.code.base.source.BaseSource;
import net.sf.mmm.code.base.source.BaseSourceImpl;
import net.sf.mmm.code.base.source.BaseSourceProvider;
import net.sf.mmm.code.base.type.BaseType;
import net.sf.mmm.code.base.type.BaseTypeWildcard;
import net.sf.mmm.util.exception.api.DuplicateObjectException;
import net.sf.mmm.util.exception.api.ObjectMismatchException;

/**
 * Implementation of {@link JavaContext} that inherits from a {@link #getParent() parent} context.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaExtendedContext extends JavaContext {

  private final JavaContext parent;

  private final BaseSourceProvider sourceProvider;

  private final Map<String, BaseSource> sourceMap;

  private final JavaClassLoader loader;

  /**
   * The constructor.
   *
   * @param source the {@link #getSource() source}.
   * @param sourceProvider the {@link BaseSourceProvider}.
   */
  public JavaExtendedContext(BaseSourceImpl source, BaseSourceProvider sourceProvider) {

    this(JavaRootContext.get(), source, sourceProvider);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent context}.
   * @param source the {@link #getSource() source}.
   * @param sourceProvider the {@link BaseSourceProvider}.
   */
  public JavaExtendedContext(JavaContext parent, BaseSourceImpl source, BaseSourceProvider sourceProvider) {

    super(source);
    this.parent = parent;
    this.loader = new JavaClassLoader();
    this.sourceProvider = sourceProvider;
    this.sourceProvider.setContext(this);
    this.sourceMap = new HashMap<>();
    registerSource(source);
  }

  @Override
  protected BaseLoader getLoader() {

    return this.loader;
  }

  /**
   * @param id the {@link BaseSource#getId() ID} of the requested source.
   * @param sourceSupplier the {@link Supplier} used as factory to {@link Supplier#get() create} the source if
   *        it does not already exist.
   * @return the existing {@link BaseSource} for the given {@link BaseSource#getId() ID}.
   */
  @Override
  public BaseSource getOrCreateSource(String id, Supplier<BaseSource> sourceSupplier) {

    BaseSource source = getSource(id);
    if (source == null) {
      source = sourceSupplier.get();
      Objects.requireNonNull(source, "source");
      if (!source.getId().equals(id)) {
        throw new ObjectMismatchException(source.getId(), id, BaseSource.class);
      }
      registerSource(source);
    }
    return source;
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

  private void registerSource(BaseSource source) {

    BaseSource duplicate = this.sourceMap.put(source.getId(), source);
    if (duplicate != null) {
      throw new DuplicateObjectException(source, source.getId(), duplicate);
    }
  }

  /**
   * This is an internal method that should only be used from implementations of {@link BaseSourceProvider}.
   *
   * @param byteCodeLocation the {@link BaseSource#getByteCodeLocation() byte code location}.
   * @param sourceCodeLocation the {@link BaseSource#getSourceCodeLocation() source code location}.
   * @return the existing or otherwise created {@link BaseSource}.
   */
  public BaseSource getOrCreateSource(File byteCodeLocation, File sourceCodeLocation) {

    File location;
    if (byteCodeLocation != null) {
      location = byteCodeLocation;
    } else {
      location = sourceCodeLocation;
    }
    String id = BaseSourceImpl.getNormalizedId(location);
    BaseSource source = getSource(id);
    if (source == null) {
      source = this.sourceProvider.create(byteCodeLocation, sourceCodeLocation);
      registerSource(source);
    }
    return source;
  }

  @Override
  public BaseSource getSource(String id) {

    BaseSource javaSource = this.sourceMap.get(id);
    if (javaSource != null) {
      return javaSource;
    }
    return this.parent.getSource(id);
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

}
