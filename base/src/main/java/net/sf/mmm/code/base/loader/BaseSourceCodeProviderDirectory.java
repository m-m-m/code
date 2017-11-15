/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base implementation of {@link BaseSourceCodeProvider} for a source-code directory in the filesystem.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseSourceCodeProviderDirectory extends BaseSourceCodeProvider {

  private File sourceFolder;

  /**
   * The constructor.
   *
   * @param sourceFolder the {@link File} pointing to the top-level source-code folder (where the root/default
   *        package is located).
   */
  public BaseSourceCodeProviderDirectory(File sourceFolder) {

    this(sourceFolder, TYPE_EXTENSION_JAVA);
  }

  /**
   * The constructor.
   *
   * @param sourceFolder the {@link File} pointing to the top-level source-code folder (where the root/default
   *        package is located).
   * @param typeExtension the {@link #getTypeExtension() type extension}.
   */
  public BaseSourceCodeProviderDirectory(File sourceFolder, String typeExtension) {

    super(typeExtension);
    assert (sourceFolder.isDirectory());
    this.sourceFolder = sourceFolder;
  }

  @Override
  public Reader openType(String qualifiedName) throws IOException {

    String path = qualifiedName2TypePath(qualifiedName);
    File sourceFile = new File(this.sourceFolder, path);
    if (sourceFile.exists()) {
      InputStream in = new FileInputStream(sourceFile);
      return openReader(in);
    }
    return null;
  }

  @Override
  public Reader openPackage(String qualifiedName) throws IOException {

    String path = qualifiedName2PackagePath(qualifiedName);
    File sourceFile = new File(this.sourceFolder, path);
    if (sourceFile.exists()) {
      InputStream in = new FileInputStream(sourceFile);
      return openReader(in);
    }
    return null;
  }

  @Override
  public List<String> scanPackage(String qualifiedName) {

    String path = qualifiedName2Path(qualifiedName);
    File packageFolder = new File(this.sourceFolder, path);
    if (packageFolder.isDirectory()) {
      File[] children = packageFolder.listFiles();
      if (children.length > 0) {
        List<String> list = new ArrayList<>();
        for (File child : children) {
          if (child.isFile()) {
            String simpleName = filename2TypeSimpleName(child.getName());
            if (simpleName != null) {
              list.add(simpleName);
            }
          }
        }
        return list;
      }
    }
    return Collections.emptyList();
  }

  @Override
  public void close() throws Exception {

    this.sourceFolder = null;
  }

}
