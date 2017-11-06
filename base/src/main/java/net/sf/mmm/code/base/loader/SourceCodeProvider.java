/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.loader;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * The interface for a provider of source-code from an arbitrary location.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface SourceCodeProvider {

  /**
   * @param qualifiedName the qualified name of the {@link net.sf.mmm.code.api.type.CodeType} to open.
   * @return a new {@link Reader} to read the source-code from or {@code null} if the requested type was not
   *         found.
   * @throws IOException on I/O error.
   * @see BaseLoader#getType(String)
   */
  Reader openType(String qualifiedName) throws IOException;

  /**
   * @param qualifiedName the qualified name of the {@link net.sf.mmm.code.api.CodePackage} to open.
   * @return a new {@link Reader} to read the source-code from or {@code null} if the requested package was
   *         not found.
   * @throws IOException on I/O error.
   */
  Reader openPackage(String qualifiedName) throws IOException;

  /**
   * @param qualifiedName the qualified name of the {@link net.sf.mmm.code.api.CodePackage} to scan.
   * @return a {@link List} with the {@link net.sf.mmm.code.api.type.CodeType#getSimpleName() simple names} of
   *         the {@link net.sf.mmm.code.api.type.CodeType}s in the specified package or {@code null} if scan
   *         is not supported.
   * @throws IOException on I/O error.
   */
  List<String> scanPackage(String qualifiedName) throws IOException;

}
