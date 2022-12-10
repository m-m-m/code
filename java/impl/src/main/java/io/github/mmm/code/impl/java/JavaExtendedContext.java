/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java;

import io.github.mmm.code.base.loader.BaseLoader;
import io.github.mmm.code.base.source.BaseSourceImpl;
import io.github.mmm.code.base.source.BaseSourceProvider;

/**
 * Implementation of {@link JavaContext} that inherits from a {@link #getParent() parent} context.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaExtendedContext extends JavaContext {

  private final JavaContext parent;

  private final BaseLoader loader;

  private final ClassLoader classLoader;

  /**
   * The constructor.
   *
   * @param source the {@link #getSource() source}.
   * @param sourceProvider the {@link BaseSourceProvider}.
   * @param classLoader the explicit {@link ClassLoader} used to load the byte-code.
   */
  public JavaExtendedContext(BaseSourceImpl source, BaseSourceProvider sourceProvider, ClassLoader classLoader) {

    this(JavaRootContext.get(), source, sourceProvider, classLoader);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent context}.
   * @param source the {@link #getSource() source}.
   * @param sourceProvider the {@link BaseSourceProvider}.
   * @param classLoader the explicit {@link ClassLoader} used to load the byte-code.
   */
  public JavaExtendedContext(JavaContext parent, BaseSourceImpl source, BaseSourceProvider sourceProvider,
      ClassLoader classLoader) {

    super(source, sourceProvider);

    this.parent = parent;

    JavaClassLoader jcl;
    if (classLoader == null) {
      jcl = new JavaClassLoader();
    } else {
      jcl = new JavaClassLoader(classLoader);
    }
    this.loader = jcl;
    this.classLoader = jcl.getClassLoader();
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent context}.
   * @param source the {@link #getSource() source}.
   * @param sourceProvider the {@link BaseSourceProvider}.
   */
  public JavaExtendedContext(JavaContext parent, BaseSourceImpl source, BaseSourceProvider sourceProvider) {

    super(source, sourceProvider);

    this.parent = parent;

    this.classLoader = null;
    this.loader = new JavaClassLoader(null);
  }

  @Override
  public BaseLoader getLoader() {

    return this.loader;
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
  public ClassLoader getClassLoader() {

    return this.classLoader;
  }

}
