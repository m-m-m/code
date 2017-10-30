/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.loader;

import net.sf.mmm.code.base.BasePackage;

/**
 * {@link BaseLoader} that encapsulates the physical loading of code.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface BaseCodeLoader extends BaseLoader {

  /**
   * @return {@code true} if loading of byte-code is supported, {@code false} otherwise.
   * @see net.sf.mmm.code.api.source.CodeSource#getByteCodeLocation()
   */
  boolean isSupportByteCode();

  /**
   * @return {@code true} if loading from source-code is supported, {@code false} otherwise.
   * @see net.sf.mmm.code.api.source.CodeSource#getSourceCodeLocation()
   */
  boolean isSupportSourceCode();

  /**
   * @param pkg the {@link BasePackage} to scan. Will load all {@link BasePackage#getChildren() children} of
   *        the package.
   */
  void scan(BasePackage pkg);

}
