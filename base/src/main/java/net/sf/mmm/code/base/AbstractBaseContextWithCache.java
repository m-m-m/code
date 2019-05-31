/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base;

import java.io.File;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import net.sf.mmm.code.api.CodeName;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.base.loader.BaseLoader;
import net.sf.mmm.code.base.source.BaseSource;
import net.sf.mmm.code.base.source.BaseSourceImpl;
import net.sf.mmm.code.base.source.BaseSourceProvider;
import net.sf.mmm.code.base.type.BaseGenericType;
import net.sf.mmm.code.base.type.BaseType;
import net.sf.mmm.util.exception.api.DuplicateObjectException;
import net.sf.mmm.util.exception.api.ObjectMismatchException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    typeCache = createCache();
    this.sourceProvider = sourceProvider;
    if (this.sourceProvider != null) {
      this.sourceProvider.setContext(this);
    }
    sourceMap = new HashMap<>();
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
  public BaseType getType(String qualifiedName) {

    BaseType type = getTypeFromCache(qualifiedName);
    if (type != null) {
      return type;
    }
    type = getLoader().getType(qualifiedName);
    return getTypeAndPutInCache(qualifiedName, type);
  }

  @Override
  public BaseType getType(CodeName qName) {

    String qualifiedName = qName.getFullName();
    BaseType type = getTypeFromCache(qualifiedName);
    if (type != null) {
      return type;
    }
    type = getLoader().getType(qName);
    return getTypeAndPutInCache(qualifiedName, type);
  }

  @Override
  public BaseGenericType getType(Class<?> clazz) {

    if (clazz.isArray()) {
      BaseGenericType componentType = getType(clazz.getComponentType());
      return componentType.createArray();
    }
    String qualifiedName = clazz.getName();
    BaseType type = getTypeFromCache(qualifiedName);
    if (type != null) {
      return type;
    }
    type = (BaseType) getLoader().getType(clazz);
    return getTypeAndPutInCache(qualifiedName, type);
  }

  private BaseType getTypeAndPutInCache(String qualifiedName, BaseType type) {

    if (type != null) {
      typeCache.put(qualifiedName, type);
    } else {
      LOG.debug("Failed to get type {}", qualifiedName);
    }
    return type;
  }

  /**
   * @param qualifiedName the {@link CodeType#getQualifiedName() qualified name} of the requested {@link CodeType}.
   * @return the requested {@link CodeType} from the cache or {@code null} if not in cache.
   */
  protected BaseType getTypeFromCache(String qualifiedName) {

    return typeCache.get(qualifiedName);
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
      source = sourceProvider.create(byteCodeLocation, sourceCodeLocation);
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
      verifyCreateSource(id);
      source = sourceProvider.create(codeSource);
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
        throw new ObjectMismatchException(source.getId(), id, BaseSource.class);
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

    if ((sourceProvider == null) && (getParent() == null)) {
      return true;
    }
    return false;
  }

  private void verifyCreateSource(Object arg) {

    if (sourceProvider == null) {
      throw new IllegalStateException(
          "Can not create source for external code in " + getClass().getSimpleName() + ": " + arg);
    }
  }

  private void registerSource(BaseSource source) {

    BaseSource duplicate = sourceMap.put(source.getId(), source);
    if (duplicate != null) {
      throw new DuplicateObjectException(source, source.getId(), duplicate);
    }
  }

  @Override
  public BaseSource getSource(String id) {

    BaseSource source = sourceMap.get(id);
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
  public void close() throws Exception {

    super.close();
    typeCache = null;
    for (BaseSource src : sourceMap.values()) {
      src.close();
    }
    sourceMap = null;
    sourceProvider = null;
  }

}
