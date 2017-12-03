/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.security.CodeSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.CodeName;
import net.sf.mmm.code.api.element.CodeElementWithDeclaringType;
import net.sf.mmm.code.api.element.CodeElementWithTypeVariables;
import net.sf.mmm.code.api.node.CodeNode;
import net.sf.mmm.code.api.type.CodeTypeVariable;
import net.sf.mmm.code.base.BaseContext;
import net.sf.mmm.code.base.BaseContextImplWithCache;
import net.sf.mmm.code.base.arg.BaseOperationArg;
import net.sf.mmm.code.base.element.BaseElement;
import net.sf.mmm.code.base.element.BaseElementWithDeclaringType;
import net.sf.mmm.code.base.loader.BaseLoader;
import net.sf.mmm.code.base.member.BaseOperation;
import net.sf.mmm.code.base.node.BaseNodeItem;
import net.sf.mmm.code.base.source.BaseSource;
import net.sf.mmm.code.base.source.BaseSourceImpl;
import net.sf.mmm.code.base.source.BaseSourceProvider;
import net.sf.mmm.code.base.type.BaseArrayType;
import net.sf.mmm.code.base.type.BaseGenericType;
import net.sf.mmm.code.base.type.BaseParameterizedType;
import net.sf.mmm.code.base.type.BaseType;
import net.sf.mmm.code.base.type.BaseTypeVariable;
import net.sf.mmm.code.base.type.BaseTypeVariables;
import net.sf.mmm.code.base.type.BaseTypeWildcard;
import net.sf.mmm.util.exception.api.IllegalCaseException;

/**
 * Implementation of {@link JavaContext} for the {@link #getParent() root} context.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class JavaContext extends BaseContextImplWithCache {

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
      throw new IllegalCaseException(type.getClass().getSimpleName());
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
      throw new IllegalCaseException(node.getClass().getSimpleName());
    }
  }

  /**
   * Implementation of {@link BaseLoader} to load classes from byte-code.
   *
   * @see JavaContext#getLoader()
   */
  protected class JavaClassLoader implements BaseLoader {

    private final ClassLoader classloader;

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
      this.classloader = classloader;
    }

    @Override
    public BaseSource getSource() {

      return JavaContext.this.getSource();
    }

    @Override
    public BaseContext getContext() {

      return JavaContext.this;
    }

    @Override
    public BaseType getType(String qualifiedName) {

      try {
        Class<?> clazz = this.classloader.loadClass(qualifiedName);
        if (clazz.isArray()) {
          throw new IllegalArgumentException(qualifiedName);
        }
        return (BaseType) getContext().getType(clazz);
      } catch (ClassNotFoundException e) {
        LOG.debug("Class {} not found.", qualifiedName, e);
        return null;
      }
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
      return source.getLoader().getType(clazz);
    }

  }

}
