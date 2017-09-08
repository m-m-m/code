/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.util.List;

import net.sf.mmm.code.api.CodeContext;
import net.sf.mmm.code.impl.java.source.JavaSource;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link net.sf.mmm.code.api.CodeContext} for Java.
 *
 * @author hohwille
 * @since 1.0.0
 */
public abstract class JavaContext extends JavaProvider implements CodeContext {

  private final JavaLoader loader;

  private final JavaSource source;

  private JavaPackage rootPackage;

  private JavaType rootExceptionType;

  /**
   * The constructor.
   */
  public JavaContext(JavaLoader loader, JavaSource source) {

    super();
    this.loader = loader;
    this.source = new JavaSource(this, System.getProperty("java.home"), null); // TODO
    this.source.setContext(this);
    JavaPackage superPackage = null; // TODO
    this.rootPackage = new JavaPackage(this.source, superPackage, null);
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

  @Override
  public JavaPackage getRootPackage() {

    return this.rootPackage;
  }

  /**
   * @return the {@link JavaSource#getToplevelPackage() top-level package}.
   */
  public JavaPackage getToplevelPackage() {

    JavaPackage pkg = this.rootPackage;
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

  JavaLoader getLoader() {

    return this.loader;
  }

  @Override
  public JavaType getRootType() {

    return getParent().getRootType();
  }

  @Override
  public JavaType getVoidType() {

    return getParent().getVoidType();
  }

  @Override
  public JavaType getRootExceptionType() {

    return getParent().getRootExceptionType();
  }

  /**
   * @param javaType the {@link JavaType} that might be {@link JavaType#isPrimitive() primitive}.
   * @return the corresponding {@link JavaType#getNonPrimitiveType() non-primitive type}.
   */
  public JavaType getNonPrimitiveType(JavaType javaType) {

    return getParent().getNonPrimitiveType(javaType);
  }

}
