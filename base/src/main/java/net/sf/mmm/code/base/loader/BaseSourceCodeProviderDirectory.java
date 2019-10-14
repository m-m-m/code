/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.loader;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import net.sf.mmm.code.api.language.JavaLanguage;

/**
 * Base implementation of {@link BaseSourceCodeProvider} for a source-code directory in the filesystem.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseSourceCodeProviderDirectory extends BaseSourceCodeProvider {

  private String sourceDirectory;

  /**
   * The constructor.
   *
   * @param sourceFolder the {@link File} pointing to the top-level source-code folder (where the root/default package
   *        is located).
   */
  public BaseSourceCodeProviderDirectory(File sourceFolder) {

    this(sourceFolder, JavaLanguage.TYPE_EXTENSION_JAVA);
  }

  /**
   * The constructor.
   *
   * @param sourceFolder the {@link File} pointing to the top-level source-code folder (where the root/default package
   *        is located).
   * @param typeExtension the {@link #getTypeExtension() type extension}.
   */
  public BaseSourceCodeProviderDirectory(File sourceFolder, String typeExtension) {

    super(typeExtension);
    assert (sourceFolder.isDirectory());
    this.sourceDirectory = sourceFolder.getAbsolutePath();
  }

  @Override
  protected Path getPath(String path) {

    return Paths.get(this.sourceDirectory, path);
  }

  @Override
  public void close() {

    this.sourceDirectory = null;
  }

  @Override
  protected boolean isClosed() {

    return (this.sourceDirectory == null);
  }

}
