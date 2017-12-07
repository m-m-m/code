/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.loader;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Collections;

import net.sf.mmm.code.api.language.CodeLanguageJava;
import net.sf.mmm.util.io.api.IoMode;
import net.sf.mmm.util.io.api.RuntimeIoException;

/**
 * Base implementation of {@link BaseSourceCodeProvider} for a source-code directory in the filesystem.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseSourceCodeProviderArchive extends BaseSourceCodeProvider {

  private final File archiveFile;

  private FileSystem fileSystem;

  /**
   * The constructor.
   *
   * @param archiveFile the {@link File} pointing to the archive (e.g. *.jar or *.zip) with the source-code.
   */
  public BaseSourceCodeProviderArchive(File archiveFile) {

    this(archiveFile, CodeLanguageJava.TYPE_EXTENSION_JAVA);
  }

  /**
   * The constructor.
   *
   * @param archiveFile the {@link File} pointing to the archive (e.g. *.jar or *.zip) with the source-code.
   * @param typeExtension the {@link #getTypeExtension() type extension}.
   */
  public BaseSourceCodeProviderArchive(File archiveFile, String typeExtension) {

    super(typeExtension);
    assert (archiveFile.isFile());
    this.archiveFile = archiveFile;
    URI uri;
    try {
      uri = new URI("jar", this.archiveFile.toURI().toString(), null);
    } catch (URISyntaxException e) {
      throw new IllegalStateException("Failed to create JAR URI for: " + archiveFile, e);
    }
    try {
      this.fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
    } catch (IOException e) {
      throw new RuntimeIoException(e, IoMode.READ);
    }
  }

  @Override
  protected Path getPath(String path) {

    return this.fileSystem.getPath(path);
  }

  @Override
  public void close() throws Exception {

    if (this.fileSystem == null) {
      return;
    }
    this.fileSystem.close();
    this.fileSystem = null;
  }

  @Override
  protected boolean isClosed() {

    return (this.fileSystem == null);
  }

}
