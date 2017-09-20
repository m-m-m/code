/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.source;

import net.sf.mmm.code.impl.java.JavaExtendedContext;

/**
 * Abstract base implemenation of {@link JavaSourceProvider}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class AbstractJavaSourceProvider implements JavaSourceProvider {

  private JavaExtendedContext context;

  /**
   * The constructor.
   */
  public AbstractJavaSourceProvider() {

    super();
  }

  /**
   * @return the {@link JavaExtendedContext}.
   */
  public JavaExtendedContext getContext() {

    return this.context;
  }

  @Override
  public void setContext(JavaExtendedContext context) {

    if (this.context == null) {
      this.context = context;
    }
    if (this.context != context) {
      throw new IllegalStateException("Already initialized!");
    }
  }

}
