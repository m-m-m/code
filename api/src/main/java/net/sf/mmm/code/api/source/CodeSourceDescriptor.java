/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.source;

/**
 * Descriptor with details of a {@link CodeSource}.
 *
 * @see CodeSource#getDescriptor()
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeSourceDescriptor {

  /** The undefined {@link #getVersion() version}. */
  String VERSION_UNDEFINED = "undefined";

  /**
   * @return the unique ID of this source. E.g. "«{@link #getGroupId() groupId}»:«{@link #getArtifactId()
   *         artifactId}»" for a maven project. May be the {@link CodeSource#getUri() URI} if no other
   *         information is available.
   */
  String getId();

  /**
   * @return the group ID of the {@link CodeSource} or {@code null} if undefined.
   */
  String getGroupId();

  /**
   * @return the artifact ID of the {@link CodeSource}. Never {@code null}.
   */
  String getArtifactId();

  /**
   * @return the version of this source if available or {@link #VERSION_UNDEFINED}.
   */
  String getVersion();

  /**
   * @return the base URL of the JavaDoc for this source. May be {@code null} if unavailable.
   */
  String getDocUrl();

}
