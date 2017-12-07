/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base;

import java.nio.file.Path;

import net.sf.mmm.code.api.CodePackage;
import net.sf.mmm.code.base.source.BaseSource;
import net.sf.mmm.code.base.source.BaseSourceImpl;

/**
 * Base implementation of {@link BaseContext}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BaseContextImpl extends BaseProviderImpl implements BaseContext {

  private BaseSourceImpl source;

  /**
   * The constructor.
   *
   * @param source the {@link #getSource() source}.
   */
  public BaseContextImpl(BaseSourceImpl source) {

    super();
    this.source = source;
    this.source.setContext(this);
  }

  @Override
  public BaseContext getContext() {

    return this;
  }

  @Override
  public BaseSource getSource() {

    return this.source;
  }

  @Override
  public void close() throws Exception {

    this.source.close();
    this.source = null;
  }

  /**
   * @return the encoding used by {@link #write(CodePackage, Path)}.
   */
  protected String getEncoding() {

    return "UTF-8";
  }

}
