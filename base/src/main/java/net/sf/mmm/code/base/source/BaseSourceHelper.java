/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.source;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Simple helper class for implementations related to {@link BaseSource}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseSourceHelper {

  /**
   * @param file the {@link File} to check. May be {@code null}.
   * @return the given {@link File} if it is an {@link File#isFile() existing regular file}, {@code null}
   *         otherwise.
   */
  public static File getFileOrNull(File file) {

    if ((file != null) && file.isFile()) {
      return file;
    }
    return null;
  }

  /**
   * @param file the {@link File} to convert. May be {@code null}
   * @return the {@link URL} pointing to the given {@link File} or {@code null} if the given {@link File} was
   *         {@code null}.
   */
  public static URL asUrl(File file) {

    if (file == null) {
      return null;
    }
    try {
      return file.toURI().toURL();
    } catch (MalformedURLException e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * @param url the {@link URL} to convert. May be {@code null}
   * @return the {@link File} pointing to the given {@link URL} or {@code null} if the given {@link URL} was
   *         {@code null}.
   */
  public static File asFile(URL url) {

    if (url == null) {
      return null;
    }
    try {
      return new File(url.toURI());
    } catch (URISyntaxException e) {
      throw new IllegalStateException(e);
    }
  }

}
