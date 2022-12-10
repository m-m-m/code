/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.loader;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * Empty implementation of {@link SourceCodeProvider} for no sources.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class SourceCodeProviderNone implements SourceCodeProvider {

  /** The singleton instance. */
  public static final SourceCodeProviderNone INSTANCE = new SourceCodeProviderNone();

  /**
   * The constructor.
   */
  private SourceCodeProviderNone() {

    super();
  }

  @Override
  public Reader openType(String qualifiedName) throws IOException {

    return null;
  }

  @Override
  public Reader openPackage(String qualifiedName) throws IOException {

    return null;
  }

  @Override
  public List<String> scanPackage(String qualifiedName) throws IOException {

    return null;
  }

  @Override
  public void close() {

    // nothing to do
  }

}
