/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.loader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

/**
 * Abstract base implementation of {@link SourceCodeProvider}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BaseSourceCodeProvider implements SourceCodeProvider {

  /** {@link #getTypeExtension() Type extension} for Java. */
  protected static final String TYPE_EXTENSION_JAVA = ".java";

  private static final String PACKAGE_INFO_JAVA = "package-info.java";

  private final String typeExtension;

  /**
   * The constructor.
   */
  public BaseSourceCodeProvider() {

    this(TYPE_EXTENSION_JAVA);
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
   * @return the extension of a file containing the source-code of a
   *         {@link net.sf.mmm.code.api.type.CodeType}. Examples are ".java", ".ts", ".kt", ".cs", etc.
   */
  public String getTypeExtension() {

    return this.typeExtension;
  }

  /**
   * @param in the {@link InputStream} to read.
   * @return the {@link Reader} for the given {@link InputStream}.
   * @throws IOException on I/O error.
   */
  protected Reader openReader(InputStream in) throws IOException {

    return new InputStreamReader(in, "UTF-8");
  }

  /**
   * @param qualifiedName the {@link net.sf.mmm.code.api.item.CodeItemWithQualifiedName#getQualifiedName()
   *        qualified name}.
   * @return the filesystem path of the {@link net.sf.mmm.code.api.type.CodeType} for the given
   *         {@code qualifiedName}.
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

    if (PACKAGE_INFO_JAVA.equals(filename)) {
      return null;
    }
    if (filename.endsWith(this.typeExtension)) {
      return filename.substring(0, filename.length() - this.typeExtension.length());
    }
    return null;
  }

  /**
   * @param qualifiedName the {@link net.sf.mmm.code.api.item.CodeItemWithQualifiedName#getQualifiedName()
   *        qualified name}.
   * @return the filesystem path of the {@link net.sf.mmm.code.api.type.CodeType} for the given
   *         {@code qualifiedName}.
   */
  protected String qualifiedName2PackagePath(String qualifiedName) {

    return qualifiedName2Path(qualifiedName) + "/" + PACKAGE_INFO_JAVA;
  }

  /**
   * @param qualifiedName the {@link net.sf.mmm.code.api.item.CodeItemWithQualifiedName#getQualifiedName()
   *        qualified name}.
   * @return the filesystem path for the given {@code qualifiedName}.
   */
  protected String qualifiedName2Path(String qualifiedName) {

    return qualifiedName.replace('.', '/');
  }

}
