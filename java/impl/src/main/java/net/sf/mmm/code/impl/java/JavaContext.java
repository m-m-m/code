/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.lang.reflect.Type;
import java.security.CodeSource;
import java.util.List;

import net.sf.mmm.code.api.CodeContext;
import net.sf.mmm.code.impl.java.element.JavaElementNode;
import net.sf.mmm.code.impl.java.source.JavaSource;
import net.sf.mmm.code.impl.java.type.JavaGenericType;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link net.sf.mmm.code.api.CodeContext} for Java.
 *
 * @author hohwille
 * @since 1.0.0
 */
public abstract class JavaContext extends JavaProvider implements CodeContext, JavaCodeLoader {

  private final AbstractJavaCodeLoader loader;

  private final JavaSource source;

  /**
   * The constructor.
   *
   * @param loader the {@link JavaCodeLoader}.
   * @param source the {@link #getSource() source}.
   */
  public JavaContext(AbstractJavaCodeLoader loader, JavaSource source) {

    super();
    this.loader = loader;
    this.loader.setContext(this);
    this.source = source;
    this.source.setContext(this);
  }

  @Override
  public abstract JavaContext getParent();

  @Override
  public JavaContext getContext() {

    return this;
  }

  @Override
  public JavaSource getSource() {

    return this.source;
  }

  /**
   * @return the root {@link JavaContext context} responsible for the fundamental code (from JDK).
   */
  public abstract JavaRootContext getRootContext();

  @Override
  public JavaPackage getRootPackage() {

    return this.source.getRootPackage();
  }

  /**
   * @return the {@link JavaSource#getToplevelPackage() top-level package}.
   */
  public JavaPackage getToplevelPackage() {

    JavaPackage pkg = getRootPackage();
    boolean todo = true;
    while (todo) {
      todo = false;
      List<JavaPathElement> children = pkg.getChildren().getList();
      if (children.size() == 1) {
        JavaPathElement child = children.get(0);
        if (child.isFile()) {
          pkg = (JavaPackage) child;
          todo = true;
        }
      }
    }
    return pkg;
  }

  /**
   * @return the {@link AbstractJavaCodeLoader}.
   */
  protected AbstractJavaCodeLoader getLoader() {

    return this.loader;
  }

  @Override
  public JavaGenericType getType(Class<?> clazz) {

    return this.loader.getType(clazz);
  }

  @Override
  public boolean isSupportByteCode() {

    return this.loader.isSupportByteCode();
  }

  @Override
  public boolean isSupportSourceCode() {

    return this.loader.isSupportSourceCode();
  }

  @Override
  public JavaGenericType getType(Type type, JavaElementNode declaringElement) {

    // TODO this is nuts. We need to reuse the package caching to retrieve existing objects instead of
    // creating new ones...
    return declaringElement.getSource().getContext().getLoader().getType(type, declaringElement);
  }

  @Override
  public JavaPackage getPackage(JavaSource javaSource, Package pkg) {

    return javaSource.getContext().getLoader().getPackage(javaSource, pkg);
  }

  @Override
  public void scan(JavaPackage pkg) {

    pkg.getContext().getLoader().scan(pkg);
  }

  /**
   * @param codeSource the {@link CodeSource}.
   * @return the existing or otherwise created {@link JavaSource}.
   */
  protected abstract JavaSource getOrCreateSource(CodeSource codeSource);

  @Override
  public abstract JavaType getRootType();

  @Override
  public abstract JavaType getRootEnumerationType();

  @Override
  public abstract JavaType getVoidType();

  @Override
  public abstract JavaType getRootExceptionType();

  /**
   * @param javaType the {@link JavaType} that might be {@link JavaType#isPrimitive() primitive}.
   * @return the corresponding {@link JavaType#getNonPrimitiveType() non-primitive type}.
   */
  public abstract JavaType getNonPrimitiveType(JavaType javaType);

}
