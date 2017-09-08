/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.source;

import net.sf.mmm.code.api.source.CodeSourceDescriptor;

/**
 * Implementation of {@link CodeSourceDescriptor} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaSourceDescriptor implements CodeSourceDescriptor {

  private final String groupId;

  private final String artifactId;

  private final String version;

  private final String id;

  private final String docUrl;

  /**
   * The constructor.
   *
   * @param id the {@link #getId() id}.
   */
  public JavaSourceDescriptor(String id) {

    super();
    this.id = id;
    this.groupId = null;
    this.artifactId = null;
    this.version = null;
    this.docUrl = null;
  }

  /**
   * The constructor.
   *
   * @param groupId the {@link #getGroupId() groupId}.
   * @param artifactId the {@link #getArtifactId() artifactId}.
   * @param version the {@link #getVersion() version}.
   * @param docUrl the {@link #getDocUrl() doc URL}.
   */
  public JavaSourceDescriptor(String groupId, String artifactId, String version, String docUrl) {

    super();
    this.groupId = groupId;
    this.artifactId = artifactId;
    this.version = version;
    this.docUrl = docUrl;
    this.id = createId();
  }

  private String createId() {

    StringBuilder buffer = new StringBuilder();
    if (this.groupId != null) {
      buffer.append(this.groupId);
      buffer.append(':');
    }
    buffer.append(this.artifactId);
    buffer.append(':');
    buffer.append(this.version);
    return buffer.toString();
  }

  @Override
  public String getId() {

    return this.id;
  }

  @Override
  public String getGroupId() {

    return this.groupId;
  }

  @Override
  public String getArtifactId() {

    return this.artifactId;
  }

  @Override
  public String getVersion() {

    return this.version;
  }

  @Override
  public String getDocUrl() {

    return this.docUrl;
  }

  @Override
  public String toString() {

    return this.id;
  }

}
