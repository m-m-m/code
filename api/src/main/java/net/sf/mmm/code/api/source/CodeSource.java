/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.source;

import java.io.File;

import net.sf.mmm.code.api.CodeFile;
import net.sf.mmm.code.api.CodePackage;
import net.sf.mmm.code.api.CodeProvider;
import net.sf.mmm.code.api.node.CodeContainer;

/**
 * A {@link CodeSource} represents a {@link #getId() physical location} where {@link CodePackage}s and
 * {@link CodeFile}s are retrieved from.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeSource extends CodeProvider, CodeContainer {

  /**
   * @return the {@link CodeSourceDependencies} containing {@link CodeSource}s that are the parents of this
   *         source. These can be considered of the dependencies of this source (like in Maven, Gradle, Yarn,
   *         etc.)
   */
  CodeSourceDependencies<?> getDependencies();

  /**
   * @return the first parent from {@link #getDependencies()} or {@code null} if the {@link #getDependencies()
   *         parents} are empty. In the latter case this is the root source (in Java from system classloader
   *         with the JDK code).
   */
  @Override
  CodeSource getParent();

  /**
   * @return the top-level package containing actual content in this source. When traversing the
   *         {@link #getRootPackage() root package} this is the first {@link CodePackage} that is empty or
   *         contains more than just one single {@link CodePackage#getChildren() child}.
   */
  CodePackage getToplevelPackage();

  /**
   * @return the unique ID of this source. May be the {@link File#toString() string representation} of the
   *         {@link #getByteCodeLocation() byte} or {@link #getSourceCodeLocation() source code location}.
   */
  String getId();

  /**
   * @return the {@link File} to the physical byte-code or {@code null} if not available. Can point to a
   *         directory, archive (JAR, WAR, EAR, etc.), or whatever format may be used for the according
   *         language. Therefore it is recommended to manually operate on this information.
   */
  File getByteCodeLocation();

  /**
   * @return the {@link File} to the physical source-code or {@code null} if not available. Can point to a
   *         directory, archive (ZIP, sources.jar, etc.), or whatever format may be used for the according
   *         language. Therefore it is not recommended to manually operate on this information.
   */
  File getSourceCodeLocation();

  /**
   * @return the {@link CodeSourceDescriptor} of this source.
   */
  CodeSourceDescriptor getDescriptor();

}
