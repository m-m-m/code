/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import net.sf.mmm.code.base.loader.BaseLoader;
import net.sf.mmm.code.base.source.BaseSourceImpl;
import net.sf.mmm.code.base.source.BaseSourceProvider;

/**
 * Implementation of {@link JavaContext} that inherits from a {@link #getParent() parent} context.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaExtendedContext extends JavaContext {

  private final JavaContext parent;

  private final JavaClassLoader loader;

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
  public JavaExtendedContext(JavaContext parent, BaseSourceImpl source, BaseSourceProvider sourceProvider, ClassLoader classLoader) {

    super(source, sourceProvider);

    this.parent = parent;

    if (classLoader == null) {
      this.loader = new JavaClassLoader();
    } else {
      this.loader = new JavaClassLoader(classLoader);
    }
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

}
