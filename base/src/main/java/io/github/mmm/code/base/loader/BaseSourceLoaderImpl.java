/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.loader;

import io.github.mmm.code.base.BaseContext;
import io.github.mmm.code.base.node.BaseNodeItemContainerAccess;
import io.github.mmm.code.base.source.BaseSource;
import io.github.mmm.code.base.source.BaseSourceImpl;

/**
 * Abstract base implementation of {@link BaseSourceLoader}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BaseSourceLoaderImpl extends BaseNodeItemContainerAccess implements BaseSourceLoader {

  private BaseSource source;

  @Override
  public BaseSource getSource() {

    return this.source;
  }

  @Override
  public BaseContext getContext() {

    return this.source.getContext();
  }

  /**
   * @param source the {@link #getSource() source}.
   */
  public void setSource(BaseSourceImpl source) {

    if (this.source == null) {
      this.source = source;
    }
    if (this.source != source) {
      throw new IllegalStateException("Already initialized!");
    }
  }

}
