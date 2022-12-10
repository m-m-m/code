/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.loader;

import io.github.mmm.code.base.BasePackage;

/**
 * {@link BaseLoader} that encapsulates the physical loading of code from a
 * {@link io.github.mmm.code.base.source.BaseSource}.
 *
 * @see io.github.mmm.code.base.source.BaseSourceImpl#getLoader()
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface BaseSourceLoader extends BaseLoader, AutoCloseable {

  /**
   * @param pkg the {@link BasePackage} to scan. Will load all {@link BasePackage#getChildren() children} of the
   *        package.
   */
  void scan(BasePackage pkg);

  @Override
  void close();

}
