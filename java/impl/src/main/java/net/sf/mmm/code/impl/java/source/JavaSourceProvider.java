/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.source;

import java.io.File;
import java.security.CodeSource;

import net.sf.mmm.code.impl.java.JavaExtendedContext;

/**
 * Factory for {@link JavaSource} to decouple from specific implementations such as maven or eclipse
 * environments.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface JavaSourceProvider {

  /**
   * @param source the {@link CodeSource}.
   * @return the new {@link JavaSource} for the given {@link CodeSource}.
   */
  JavaSource create(CodeSource source);

  /**
   * @param byteCodeLocation the {@link JavaSource#getByteCodeLocation() byte code location}.
   * @param sourceCodeLocation the {@link JavaSource#getSourceCodeLocation() source code location}.
   * @return the new {@link JavaSource} for the given {@link File}s.
   */
  JavaSource create(File byteCodeLocation, File sourceCodeLocation);

  /**
   * @param context the {@link JavaExtendedContext}.
   */
  void setContext(JavaExtendedContext context);
}
