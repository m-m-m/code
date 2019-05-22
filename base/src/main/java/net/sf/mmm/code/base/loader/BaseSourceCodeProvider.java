/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.loader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.language.JavaLanguage;

/**
 * Abstract base implementation of {@link SourceCodeProvider}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BaseSourceCodeProvider implements SourceCodeProvider {

  private static final Logger LOG = LoggerFactory.getLogger(BaseSourceCodeProvider.class);

  private final String typeExtension;

  /**
   * The constructor.
   */
  public BaseSourceCodeProvider() {

    this(JavaLanguage.TYPE_EXTENSION_JAVA);
  }

  /**
   * The constructor.
   *
   * @param typeExtension the {@link #getTypeExtension() type extension}.
   */
  public BaseSourceCodeProvider(String typeExtension) {

    super();
    Objects.requireNonNull(typeExtension, "typeExtension");
    this.typeExtension = typeExtension;
  }

  /**
   * @return the extension of a file containing the source-code of a {@link net.sf.mmm.code.api.type.CodeType}. Examples
   *         are ".java", ".ts", ".kt", ".cs", etc.
   */
  public String getTypeExtension() {

    return this.typeExtension;
  }

  @Override
  public Reader openType(String qualifiedName) throws IOException {

    requireNotClosed();
    String pathString = qualifiedName2TypePath(qualifiedName);
    return openFile(pathString);
  }

  @Override
  public Reader openPackage(String qualifiedName) throws IOException {

    requireNotClosed();
    String pathString = qualifiedName2PackagePath(qualifiedName);
    return openFile(pathString);
  }

  private Reader openFile(String pathString) throws IOException {

    Path path = getPath(pathString);
    if (Files.isRegularFile(path)) {
      LOG.debug("Opening file {} to parse source code.", pathString);
      InputStream in = Files.newInputStream(path);
      return openReader(in);
    } else {
      LOG.debug("File {} does not exist.", pathString);
    }
    return null;
  }

  @Override
  public List<String> scanPackage(String qualifiedName) {

    requireNotClosed();
    String pathString = qualifiedName2Path(qualifiedName);
    Path path = getPath(pathString);
    List<String> result = null;
    for (Path child : path) {
      String simpleName = filename2TypeSimpleName(child.getFileName().toString());
      if (simpleName != null) {
        if (Files.isRegularFile(child)) {
          if (result == null) {
            result = new ArrayList<>();
          }
          result.add(simpleName);
        }
      }
    }
    if (result != null) {
      return result;
    }
    return Collections.emptyList();
  }

  /**
   * @param path the {@link Path} as {@link String}.
   * @return the actual {@link Path}.
   */
  protected abstract Path getPath(String path);

  /**
   * @return {@code true} if {@link #close() close} was called, {@code false} otherwise.
   */
  protected abstract boolean isClosed();

  /**
   * Verifies that this provider has not yet been {@link #close()}d.
   */
  protected void requireNotClosed() {

    if (isClosed()) {
      throw new IllegalStateException("already closed!");
    }
  }

  /**
   * @param in the {@link InputStream} to read.
   * @return the {@link Reader} for the given {@link InputStream}.
   * @throws IOException on I/O error.
   */
  protected Reader openReader(InputStream in) throws IOException {

    InputStreamReader reader;
    try {
      reader = new InputStreamReader(in, "UTF-8");
      return reader;
    } catch (IOException | RuntimeException e) {
      in.close();
      throw e;
    }
  }

  /**
   * @param qualifiedName the {@link net.sf.mmm.code.api.item.CodeItemWithQualifiedName#getQualifiedName() qualified
   *        name}.
   * @return the filesystem path of the {@link net.sf.mmm.code.api.type.CodeType} for the given {@code qualifiedName}.
   */
  protected String qualifiedName2TypePath(String qualifiedName) {

    return qualifiedName2Path(qualifiedName) + this.typeExtension;
  }

  /**
   * @param filename the filename to convert.
   * @return the {@link net.sf.mmm.code.api.type.CodeType#getSimpleName() simple name} of the corresponding
   *         {@link net.sf.mmm.code.api.type.CodeType} or {@code null} if not a
   *         {@link net.sf.mmm.code.api.type.CodeType}.
   */
  protected String filename2TypeSimpleName(String filename) {

    if (JavaLanguage.PACKAGE_INFO_JAVA.equals(filename)) {
      return null;
    }
    if (filename.endsWith(this.typeExtension)) {
      return filename.substring(0, filename.length() - this.typeExtension.length());
    }
    return null;
  }

  /**
   * @param qualifiedName the {@link net.sf.mmm.code.api.item.CodeItemWithQualifiedName#getQualifiedName() qualified
   *        name}.
   * @return the filesystem path of the {@link net.sf.mmm.code.api.type.CodeType} for the given {@code qualifiedName}.
   */
  protected String qualifiedName2PackagePath(String qualifiedName) {

    return qualifiedName2Path(qualifiedName) + "/" + JavaLanguage.PACKAGE_INFO_JAVA;
  }

  /**
   * @param qualifiedName the {@link net.sf.mmm.code.api.item.CodeItemWithQualifiedName#getQualifiedName() qualified
   *        name}.
   * @return the filesystem path for the given {@code qualifiedName}.
   */
  protected String qualifiedName2Path(String qualifiedName) {

    return qualifiedName.replace('.', '/');
  }

  /**
   * @param sourceCodeLocation the {@link File} pointing to the location of the source code. See
   *        {@link net.sf.mmm.code.api.source.CodeSource#getSourceCodeLocation()}.
   * @return the {@link BaseSourceCodeProvider} or {@code null} if the give {@link File} is {@code null} or does not
   *         {@link File#exists() exist}.
   */
  public static BaseSourceCodeProvider of(File sourceCodeLocation) {

    return of(sourceCodeLocation, JavaLanguage.TYPE_EXTENSION_JAVA);
  }

  /**
   * @param sourceCodeLocation the {@link File} pointing to the location of the source code. See
   *        {@link net.sf.mmm.code.api.source.CodeSource#getSourceCodeLocation()}.
   * @param typeExtension the {@link #getTypeExtension() type extension}.
   * @return the {@link BaseSourceCodeProvider} or {@code null} if the give {@link File} is {@code null} or does not
   *         {@link File#exists() exist}.
   */
  public static BaseSourceCodeProvider of(File sourceCodeLocation, String typeExtension) {

    if (sourceCodeLocation == null) {
      return null;
    } else if (sourceCodeLocation.isDirectory()) {
      return new BaseSourceCodeProviderDirectory(sourceCodeLocation, typeExtension);
    } else if (sourceCodeLocation.isFile()) {
      return new BaseSourceCodeProviderArchive(sourceCodeLocation, typeExtension);
    }
    return null;
  }
}
