/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.source;

import io.github.mmm.code.base.AbstractBaseContext;

/**
 * Abstract base implemenation of {@link BaseSourceProvider}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BaseSourceProviderImpl implements BaseSourceProvider {

  private AbstractBaseContext context;

  /**
   * The constructor.
   */
  public BaseSourceProviderImpl() {

    super();
  }

  /**
   * @return the {@link AbstractBaseContext}.
   */
  public AbstractBaseContext getContext() {

    return this.context;
  }

  @Override
  public void setContext(AbstractBaseContext context) {

    if (this.context == null) {
      this.context = context;
    }
    if (this.context != context) {
      throw new IllegalStateException("Already initialized!");
    }
  }

}
