/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.loader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    this(archiveFile, TYPE_EXTENSION_JAVA);
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
  public Reader openType(String qualifiedName) throws IOException {

    Path path = this.fileSystem.getPath(qualifiedName2TypePath(qualifiedName));
    if (Files.isRegularFile(path)) {
      InputStream in = Files.newInputStream(path);
      return openReader(in);
    }
    return null;
  }

  @Override
  public Reader openPackage(String qualifiedName) throws IOException {

    Path path = this.fileSystem.getPath(qualifiedName2PackagePath(qualifiedName));
    if (Files.isRegularFile(path)) {
      InputStream in = Files.newInputStream(path);
      return openReader(in);
    }
    return null;
  }

  @Override
  public List<String> scanPackage(String qualifiedName) {

    Path path = this.fileSystem.getPath(qualifiedName2Path(qualifiedName));
    List<String> result = null;
    for (Path child : path) {
      String simpleName = filename2TypeSimpleName(child.getFileName().toString());
      if (simpleName != null) {
        if (Files.isRegularFile(child)) {
          if (result == null) {
            result = new ArrayList<>();
            result.add(simpleName);
          }
        }
      }
    }
    if (result != null) {
      return result;
    }
    return Collections.emptyList();
  }

  @Override
  public void close() throws Exception {

    if (this.fileSystem == null) {
      return;
    }
    this.fileSystem.close();
    this.fileSystem = null;
  }

}
