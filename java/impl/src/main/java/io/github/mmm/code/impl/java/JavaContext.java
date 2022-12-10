/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.security.CodeSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.code.api.CodeName;
import io.github.mmm.code.api.element.CodeElementWithDeclaringType;
import io.github.mmm.code.api.element.CodeElementWithTypeVariables;
import io.github.mmm.code.api.node.CodeNode;
import io.github.mmm.code.api.type.CodeTypeVariable;
import io.github.mmm.code.base.AbstractBaseContextWithCache;
import io.github.mmm.code.base.BaseContext;
import io.github.mmm.code.base.arg.BaseOperationArg;
import io.github.mmm.code.base.element.BaseElement;
import io.github.mmm.code.base.element.BaseElementWithDeclaringType;
import io.github.mmm.code.base.loader.BaseLoader;
import io.github.mmm.code.base.member.BaseOperation;
import io.github.mmm.code.base.node.BaseNodeItem;
import io.github.mmm.code.base.source.BaseSource;
import io.github.mmm.code.base.source.BaseSourceImpl;
import io.github.mmm.code.base.source.BaseSourceProvider;
import io.github.mmm.code.base.type.BaseArrayType;
import io.github.mmm.code.base.type.BaseGenericType;
import io.github.mmm.code.base.type.BaseParameterizedType;
import io.github.mmm.code.base.type.BaseType;
import io.github.mmm.code.base.type.BaseTypeVariable;
import io.github.mmm.code.base.type.BaseTypeVariables;
import io.github.mmm.code.base.type.BaseTypeWildcard;

/**
 * Implementation of {@link io.github.mmm.code.api.CodeContext} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class JavaContext extends AbstractBaseContextWithCache {

  private static final Logger LOG = LoggerFactory.getLogger(JavaContext.class);

  /**
   * The constructor.
   *
   * @param source the top-level {@link #getSource() source}.
   */
  protected JavaContext(BaseSourceImpl source) {

    super(source);
  }

  /**
   * The constructor.
   *
   * @param source the top-level {@link #getSource() source}.
   * @param sourceProvider the {@link BaseSourceProvider}.
   */
  public JavaContext(BaseSourceImpl source, BaseSourceProvider sourceProvider) {

    super(source, sourceProvider);
  }

  /**
   * @return the root {@link JavaContext context} responsible for the fundamental code (from JDK).
   */
  @Override
  public abstract JavaRootContext getRootContext();

  @Override
  protected BaseType getTypeFromCache(String qualifiedName) { // make visible

    return super.getTypeFromCache(qualifiedName);
  }

  @Override
  public BaseGenericType getType(Type type, CodeElementWithDeclaringType declaringElement) {

    if (type instanceof Class) {
      return getType((Class<?>) type);
    } else if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;
      return new BaseParameterizedType((BaseElement) declaringElement, parameterizedType);
    } else if (type instanceof TypeVariable) {
      TypeVariable<?> typeVar = (TypeVariable<?>) type;
      if (declaringElement instanceof BaseType) {
        BaseType declaringType = (BaseType) declaringElement;
        assert (typeVar.getGenericDeclaration() == declaringType.getReflectiveObject());
        return new BaseTypeVariable(declaringType.getTypeParameters(), typeVar);
      } else if (declaringElement instanceof BaseOperation) {
        BaseOperation operation = (BaseOperation) declaringElement;
        if (typeVar.getGenericDeclaration() == operation.getReflectiveObject()) {
          return new BaseTypeVariable(operation.getTypeParameters(), typeVar);
        }
      }
      BaseTypeVariable typeVariable = (BaseTypeVariable) findTypeVariable(declaringElement, typeVar.getName());
      if (typeVariable != null) {
        return typeVariable;
      }
      LOG.warn("Could not find type variable {} in {}", typeVar, declaringElement);
      return new BaseTypeVariable((BaseTypeVariables) declaringElement.getDeclaringType().getTypeParameters(), typeVar);
    } else if (type instanceof WildcardType) {
      WildcardType wildcard = (WildcardType) type;
      BaseNodeItem parent;
      if (declaringElement instanceof BaseParameterizedType) {
        parent = ((BaseParameterizedType) declaringElement).getTypeParameters();
      } else {
        parent = (BaseNodeItem) declaringElement;
      }
      return new BaseTypeWildcard(parent, wildcard);
    } else if (type instanceof GenericArrayType) {
      return new BaseArrayType((BaseElementWithDeclaringType) declaringElement, type);
    } else {
      throw new IllegalStateException(type.getClass().getSimpleName());
    }
  }

  private static CodeTypeVariable findTypeVariable(CodeNode node, String name) {

    CodeElementWithTypeVariables elementWithTypeVariables = findElementWithTypeVariables(node);
    if (elementWithTypeVariables != null) {
      return elementWithTypeVariables.getTypeParameters().get(name, true);
    }
    return null;
  }

  private static CodeElementWithTypeVariables findElementWithTypeVariables(CodeNode node) {

    if (node instanceof CodeElementWithTypeVariables) {
      return (CodeElementWithTypeVariables) node;
    } else if (node instanceof BaseOperationArg) {
      BaseOperationArg arg = (BaseOperationArg) node;
      BaseOperation operation = arg.getDeclaringOperation();
      if (operation != null) {
        return operation;
      } else {
        return arg.getDeclaringType();
      }
    } else if (node instanceof BaseGenericType) {
      return findElementWithTypeVariables(node.getParent());
    } else if (node instanceof BaseTypeVariables) {
      return findElementWithTypeVariables(node.getParent());
    } else {
      throw new IllegalStateException(node.getClass().getSimpleName());
    }
  }

  /**
   * @return the {@link ClassLoader} used by this context to load byte-code.
   */
  public abstract ClassLoader getClassLoader();

  /**
   * Implementation of {@link BaseLoader} to load classes from byte-code.
   *
   * @see JavaContext#getLoader()
   */
  protected class JavaClassLoader implements BaseLoader {

    private final ClassLoader classLoader;

    /**
     * The constructor.
     */
    public JavaClassLoader() {

      this(Thread.currentThread().getContextClassLoader());
    }

    /**
     * The constructor.
     *
     * @param classloader the explicit {@link ClassLoader} to use.
     */
    public JavaClassLoader(ClassLoader classloader) {

      super();
      this.classLoader = classloader;
    }

    @Override
    public BaseSource getSource() {

      return JavaContext.this.getSource();
    }

    @Override
    public BaseContext getContext() {

      return JavaContext.this;
    }

    /**
     * @return the {@link ClassLoader} used to load byte-code.
     */
    public ClassLoader getClassLoader() {

      return this.classLoader;
    }

    @Override
    public BaseType getType(String qualifiedName) {

      if (this.classLoader != null) {
        Class<?> clazz = null;
        try {
          clazz = this.classLoader.loadClass(qualifiedName);
          if (clazz.isArray()) {
            throw new IllegalArgumentException(qualifiedName);
          }
          return (BaseType) getContext().getType(clazz);
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
          if (LOG.isTraceEnabled()) {
            LOG.debug("Class {} not found.", qualifiedName, e);
          } else {
            LOG.debug("Class {} not found: {}", qualifiedName, e.toString());
          }
        }
      }
      BaseType type = getSource().getLoader().getType(parseName(qualifiedName));
      if (type != null) {
        // TODO make or create as system immutable to prevent eager init
        type.setImmutable();
      }
      return type;
    }

    @Override
    public BaseType getType(CodeName qualifiedName) {

      return getType(qualifiedName.getFullName());
    }

    @Override
    public BaseGenericType getType(Class<?> clazz) {

      if (clazz.isArray()) {
        BaseGenericType componentType = getType(clazz.getComponentType());
        return componentType.createArray();
      }
      CodeSource codeSource = clazz.getProtectionDomain().getCodeSource();
      BaseSource source = getOrCreateSource(codeSource);
      if (source == null) {
        return null;
      }
      return source.getLoader().getType(clazz);
    }

  }

}
