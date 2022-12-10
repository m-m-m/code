/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.source;

import io.github.mmm.code.api.source.CodeSource;
import io.github.mmm.code.base.BaseContext;
import io.github.mmm.code.base.BasePackage;
import io.github.mmm.code.base.BaseProvider;
import io.github.mmm.code.base.loader.BaseSourceLoader;
import io.github.mmm.code.base.type.BaseType;

/**
 * Base implementation of {@link CodeSource}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface BaseSource extends CodeSource, BaseProvider {

  @Override
  BaseSource getParent();

  @Override
  default BaseSource getSource() {

    return this;
  }

  @Override
  BasePackage getRootPackage();

  @Override
  default BaseContext getContext() {

    return getParent().getContext();
  }

  @Override
  BaseSourceDependencies getDependencies();

  /**
   * @return the {@link BaseSourceLoader} to load {@link BasePackage}s and {@link BaseType}s.
   */
  BaseSourceLoader getLoader();

}
