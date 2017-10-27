/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.lang.reflect.Type;
import java.security.CodeSource;
import java.util.List;

import net.sf.mmm.code.api.expression.CodeExpression;
import net.sf.mmm.code.api.syntax.CodeSyntax;
import net.sf.mmm.code.base.BaseContext;
import net.sf.mmm.code.base.BasePackage;
import net.sf.mmm.code.base.BasePathElement;
import net.sf.mmm.code.base.element.BaseElement;
import net.sf.mmm.code.base.type.BaseGenericType;
import net.sf.mmm.code.base.type.BaseType;
import net.sf.mmm.code.base.type.BaseTypeWildcard;
import net.sf.mmm.code.impl.java.expression.constant.JavaConstant;
import net.sf.mmm.code.impl.java.loader.JavaCodeLoader;
import net.sf.mmm.code.impl.java.source.JavaSource;

/**
 * Implementation of {@link net.sf.mmm.code.api.CodeContext} for Java.
 *
 * @author hohwille
 * @since 1.0.0
 */
public abstract class JavaContext extends JavaProvider implements BaseContext, JavaCodeLoader {

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

  @Override
  public CodeSyntax getSyntax() {

    return getRootContext().getSyntax();
  }

  /**
   * @return the root {@link JavaContext context} responsible for the fundamental code (from JDK).
   */
  public abstract JavaRootContext getRootContext();

  @Override
  public BasePackage getRootPackage() {

    return this.source.getRootPackage();
  }

  /**
   * @return the {@link JavaSource#getToplevelPackage() top-level package}.
   */
  public BasePackage getToplevelPackage() {

    BasePackage pkg = getRootPackage();
    boolean todo = true;
    while (todo) {
      todo = false;
      List<BasePathElement> children = pkg.getChildren().getInternalList();
      if (children.size() == 1) {
        BasePathElement child = children.get(0);
        if (child.isFile()) {
          pkg = (BasePackage) child;
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
  public BaseGenericType getType(Class<?> clazz) {

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
  public BaseGenericType getType(Type type, BaseElement declaringElement) {

    // TODO this is nuts. We need to reuse the package caching to retrieve existing objects instead of
    // creating new ones...
    return ((JavaContext) declaringElement.getSource().getContext()).getLoader().getType(type, declaringElement);
  }

  @Override
  public BasePackage getPackage(JavaSource javaSource, Package pkg) {

    return javaSource.getContext().getLoader().getPackage(javaSource, pkg);
  }

  @Override
  public void scan(BasePackage pkg) {

    ((JavaContext) pkg.getContext()).getLoader().scan(pkg);
  }

  /**
   * @param codeSource the {@link CodeSource}.
   * @return the existing or otherwise created {@link JavaSource}.
   */
  protected abstract JavaSource getOrCreateSource(CodeSource codeSource);

  @Override
  public abstract BaseType getRootType();

  @Override
  public abstract BaseType getRootEnumerationType();

  @Override
  public abstract BaseType getVoidType();

  @Override
  public abstract BaseType getRootExceptionType();

  @Override
  public abstract BaseTypeWildcard getUnboundedWildcard();

  @Override
  public CodeExpression createExpression(Object value, boolean primitive) {

    return JavaConstant.of(value, primitive);
  }

}
