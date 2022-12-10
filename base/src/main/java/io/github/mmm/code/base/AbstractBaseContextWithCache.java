/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base;

import java.io.File;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.base.exception.DuplicateObjectException;
import io.github.mmm.base.exception.ObjectMismatchException;
import io.github.mmm.code.api.CodeName;
import io.github.mmm.code.api.type.CodeType;
import io.github.mmm.code.base.loader.BaseLoader;
import io.github.mmm.code.base.source.BaseSource;
import io.github.mmm.code.base.source.BaseSourceImpl;
import io.github.mmm.code.base.source.BaseSourceProvider;
import io.github.mmm.code.base.type.BaseGenericType;
import io.github.mmm.code.base.type.BaseType;

/**
 * Extends {@link AbstractBaseContext} with caching to speed up lookups.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class AbstractBaseContextWithCache extends AbstractBaseContext {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractBaseContextWithCache.class);

  private Map<String, BaseType> typeCache;

  private Map<String, BaseSource> sourceMap;

  private BaseSourceProvider sourceProvider;

  /**
   * The constructor.
   *
   * @param source the top-level {@link #getSource() source}.
   */
  public AbstractBaseContextWithCache(BaseSourceImpl source) {

    this(source, null);
  }

  /**
   * The constructor.
   *
   * @param source the top-level {@link #getSource() source}.
   * @param sourceProvider the {@link BaseSourceProvider}.
   */
  public AbstractBaseContextWithCache(BaseSourceImpl source, BaseSourceProvider sourceProvider) {

    super(source);
    this.typeCache = createCache();
    this.sourceProvider = sourceProvider;
    if (this.sourceProvider != null) {
      this.sourceProvider.setContext(this);
    }
    this.sourceMap = new HashMap<>();
    registerSource(source);
  }

  /**
   * @param <K> key type.
   * @param <V> value type.
   * @return a new empty {@link Map} instance to use as cache. May be a regular {@link HashMap} but can also be a full
   *         blown cache implementation that will automatically evict old items if a specific size is reached.
   */
  protected <K, V> Map<K, V> createCache() {

    return new HashMap<>();
  }

  /**
   * @return the {@link BaseLoader} to load {@link BaseType}s.
   */
  protected abstract BaseLoader getLoader();

  @Override
  public BaseType getOrCreateType(String qualifiedName, boolean add) {

    BaseType type = getType(qualifiedName);
    if (type == null) {
      BaseFile file = getSource().getRootPackage().getChildren().getOrCreateFile(parseName(qualifiedName), add);
      type = file.getType();
      putTypeInCache(qualifiedName, type);
    }
    return type;
  }

  @Override
  public BaseType getType(String qualifiedName) {

    BaseType type = getTypeFromCache(qualifiedName);
    if (type != null) {
      return type;
    }
    type = getLoader().getType(qualifiedName);
    return putTypeInCache(qualifiedName, type);
  }

  @Override
  public BaseType getType(CodeName qName) {

    String qualifiedName = qName.getFullName();
    BaseType type = getTypeFromCache(qualifiedName);
    if (type != null) {
      return type;
    }
    type = getLoader().getType(qName);
    return putTypeInCache(qualifiedName, type);
  }

  @Override
  public BaseGenericType getType(Class<?> clazz) {

    if (clazz.isArray()) {
      BaseGenericType componentType = getType(clazz.getComponentType());
      return componentType.createArray();
    }
    String qualifiedName = clazz.getName();
    BaseGenericType type = getTypeFromCache(qualifiedName);
    if (type != null) {
      return type;
    }
    type = getLoader().getType(clazz);
    return putTypeInCache(qualifiedName, (BaseType) type);
  }

  @Override
  protected BaseType getTypeFromCache(String qualifiedName) {

    AbstractBaseContext parent = getParent();
    if (parent != null) {
      BaseType type = parent.getTypeFromCache(qualifiedName);
      if (type != null) {
        return type;
      }
    }
    return this.typeCache.get(qualifiedName);
  }

  private BaseType putTypeInCache(String qualifiedName, BaseType type) {

    if (type != null) {
      this.typeCache.put(qualifiedName, type);
      // TODO prevent eager init...?
      for (CodeType nested : type.getNestedTypes().getDeclared()) {
        putTypeInCache(nested.getQualifiedName(), (BaseType) nested);
      }
    } else {
      LOG.trace("Failed to get type {}", qualifiedName);
    }
    return type;
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
      verifyCreateSource(id);
      source = this.sourceProvider.create(byteCodeLocation, sourceCodeLocation);
      registerSource(source);
    }
    return source;
  }

  /**
   * @param codeSource the {@link CodeSource}.
   * @return the existing or otherwise created {@link BaseSource}.
   */
  protected BaseSource getOrCreateSource(CodeSource codeSource) {

    if (codeSource == null) {
      return getRootContext().getSource();
    } else if (this.sourceProvider == null) {
      return null;
    }
    BaseSource source = null;
    String id = null;
    if (codeSource.getLocation() == null) {
      source = getSource();
    } else {
      id = BaseSourceImpl.getNormalizedId(codeSource);
      source = getSource(id);
    }
    if (source == null) {
      source = this.sourceProvider.create(codeSource);
      registerSource(source);
    }
    return source;
  }

  /**
   * <b>Attention:</b> This is an internal method that shall not be used from outside. Use {@link #getSource(String)}
   * instead.
   *
   * @param id the {@link BaseSource#getId() ID} of the requested source.
   * @param sourceSupplier the {@link Supplier} used as factory to {@link Supplier#get() create} the source if it does
   *        not already exist.
   * @return the existing {@link BaseSource} for the given {@link BaseSource#getId() ID}.
   */
  public BaseSource getOrCreateSource(String id, Supplier<BaseSource> sourceSupplier) {

    BaseSource source = getSource(id);
    if (source == null) {
      if (isPreventRegisterSource()) {
        verifyCreateSource(id);
      }
      source = sourceSupplier.get();
      Objects.requireNonNull(source, "source");
      if (!source.getId().equals(id)) {
        throw new ObjectMismatchException(source.getId(), id);
      }
      registerSource(source);
    }
    return source;
  }

  /**
   * @return {@code true} if {@link #getOrCreateSource(String, Supplier)} may not be called to register a new source,
   *         {@code false} otherwise.
   */
  protected boolean isPreventRegisterSource() {

    if ((this.sourceProvider == null) && (getParent() == null)) {
      return true;
    }
    return false;
  }

  private void verifyCreateSource(Object arg) {

    if (this.sourceProvider == null) {
      throw new IllegalStateException(
          "Can not create source for external code in " + getClass().getSimpleName() + ": " + arg);
    }
  }

  private void registerSource(BaseSource source) {

    BaseSource duplicate = this.sourceMap.put(source.getId(), source);
    if (duplicate != null) {
      throw new DuplicateObjectException(source, source.getId(), duplicate);
    }
  }

  @Override
  public BaseSource getSource(String id) {

    BaseSource source = this.sourceMap.get(id);
    if (source != null) {
      return source;
    }
    BaseContext parent = getParent();
    if (parent != null) {
      source = parent.getSource(id);
    }
    return source;
  }

  @Override
  public void close() {

    super.close();
    this.typeCache = null;
    for (BaseSource src : this.sourceMap.values()) {
      src.close();
    }
    this.sourceMap = null;
    this.sourceProvider = null;
  }

}
