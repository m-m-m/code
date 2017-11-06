/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base;

import java.util.HashMap;
import java.util.Map;

import net.sf.mmm.code.api.CodeName;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.base.loader.BaseLoader;
import net.sf.mmm.code.base.source.BaseSourceImpl;
import net.sf.mmm.code.base.type.BaseGenericType;
import net.sf.mmm.code.base.type.BaseType;

/**
 * Extends {@link BaseContextImpl} with caching to speed up lookups.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BaseContextImplWithCache extends BaseContextImpl {

  private final Map<String, BaseType> typeCache;

  /**
   * The constructor.
   *
   * @param source the toplevel {@link #getSource() source}.
   */
  public BaseContextImplWithCache(BaseSourceImpl source) {

    super(source);
    this.typeCache = createCache();
  }

  /**
   * @param <K> key type.
   * @param <V> value type.
   * @return a new empty {@link Map} instance to use as cache. May be a regular {@link HashMap} but can also
   *         be a full blown cache implementation that will automatically evict old items if a specific size
   *         is reached.
   */
  protected <K, V> Map<K, V> createCache() {

    return new HashMap<>();
  }

  /**
   * @return the {@link BaseLoader} to load {@link BaseType}s.
   */
  protected BaseLoader getLoader() {

    return getSource();
  }

  @Override
  public BaseType getType(String qualifiedName) {

    BaseType type = getTypeFromCache(qualifiedName);
    if (type != null) {
      return type;
    }
    type = getLoader().getType(qualifiedName);
    if (type != null) {
      this.typeCache.put(qualifiedName, type);
    }
    return type;
  }

  @Override
  public BaseType getType(CodeName qName) {

    String qualifiedName = qName.getFullName();
    BaseType type = getTypeFromCache(qualifiedName);
    if (type != null) {
      return type;
    }
    type = getLoader().getType(qName);
    if (type != null) {
      this.typeCache.put(qualifiedName, type);
    }
    return type;
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
    if (type != null) {
      this.typeCache.put(qualifiedName, type);
    }
    return type;
  }

  /**
   * @param qualifiedName the {@link CodeType#getQualifiedName() qualified name} of the requested
   *        {@link CodeType}.
   * @return the requested {@link CodeType} from the cache or {@code null} if not in cache.
   */
  protected BaseType getTypeFromCache(String qualifiedName) {

    return this.typeCache.get(qualifiedName);
  }

}
