/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.source;

import java.io.File;
import java.security.CodeSource;

import io.github.mmm.code.base.AbstractBaseContext;

/**
 * Factory for {@link BaseSource} to decouple from specific implementations such as maven or eclipse
 * environments.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface BaseSourceProvider {

  /**
   * @param source the {@link CodeSource}.
   * @return the new {@link BaseSource} for the given {@link CodeSource}.
   */
  BaseSource create(CodeSource source);

  /**
   * @param byteCodeLocation the {@link BaseSource#getByteCodeLocation() byte code location}.
   * @param sourceCodeLocation the {@link BaseSource#getSourceCodeLocation() source code location}.
   * @return the new {@link BaseSource} for the given {@link File}s.
   */
  BaseSource create(File byteCodeLocation, File sourceCodeLocation);

  /**
   * @param context the {@link AbstractBaseContext}.
   */
  void setContext(AbstractBaseContext context);
}
