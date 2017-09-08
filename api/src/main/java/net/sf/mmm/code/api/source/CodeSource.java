/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.source;

import java.net.URL;

import net.sf.mmm.code.api.CodeFile;
import net.sf.mmm.code.api.CodePackage;
import net.sf.mmm.code.api.CodeProvider;
import net.sf.mmm.code.api.node.CodeContainer;

/**
 * A {@link CodeSource} represents a {@link #getUri() physical location} where {@link CodePackage}s and
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
  CodeSourceDependencies getDependencies();

  /**
   * @return the first parent from {@link #getDependencies()} or {@code null} if the {@link #getDependencies()
   *         parents} are empty. In the latter case this is the root source (in Java from system classloader
   *         with the JDK code).
   */
  @Override
  CodeSource getParent();

  /**
   * @return {@code true} if this source is pointing to actual source-code that will be parsed for analysis,
   *         {@code false} otherwise (if only byte-code analysis e.g. via {@link ClassLoader} is available).
   */
  boolean isSourceCodeAvailable();

  /**
   * @return the top-level package containing actual content in this source. When traversing the
   *         {@link #getRootPackage() root package} this is the first {@link CodePackage} that is empty or
   *         contains more than just one single {@link #getPackage(CodePackage, String) child package}.
   */
  CodePackage getToplevelPackage();

  /**
   * @return the URI pointing to the physical source. May be used to find and select a particular source via
   *         {@link String#equals(Object) equals} or {@link String#contains(CharSequence) contains}. E.g. you
   *         might want to find the {@link CodeSource} for {@code "src/main/java"} of your current maven
   *         project or the JAR source of an external dependency.
   */
  String getUri();

  /**
   * @return the {@link URL} to the physical source or {@code null} if not available.
   */
  URL getLocation();

  /**
   * @return the {@link CodeSourceDescriptor} of this source.
   */
  CodeSourceDescriptor getDescriptor();

}
