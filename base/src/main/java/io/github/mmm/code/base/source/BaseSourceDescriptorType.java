/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.source;

import io.github.mmm.code.api.source.CodeSourceDescriptor;

/**
 * Base implementation of {@link BaseSourceDescriptor} as immutable type.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseSourceDescriptorType extends BaseSourceDescriptor {

  private final String groupId;

  private final String artifactId;

  private final String version;

  private final String scope;

  private final String id;

  private final String docUrl;

  /**
   * The constructor.
   *
   * @param id the {@link #getId() id}.
   */
  public BaseSourceDescriptorType(String id) {

    super();
    this.id = id;
    this.groupId = null;
    this.artifactId = null;
    this.scope = null;
    this.version = null;
    this.docUrl = null;
  }

  /**
   * The constructor.
   *
   * @param groupId the {@link #getGroupId() groupId}.
   * @param artifactId the {@link #getArtifactId() artifactId}.
   * @param version the {@link #getVersion() version}.
   * @param scope the {@link #getScope() scope}.
   * @param docUrl the {@link #getDocUrl() doc URL}.
   */
  public BaseSourceDescriptorType(String groupId, String artifactId, String version, String scope, String docUrl) {

    super();
    this.groupId = groupId;
    this.artifactId = artifactId;
    this.version = version;
    this.scope = scope;
    this.docUrl = docUrl;
    this.id = createId();
  }

  /**
   * The constructor.
   *
   * @param template the {@link CodeSourceDescriptor} to copy from.
   */
  public BaseSourceDescriptorType(CodeSourceDescriptor template) {

    super();
    this.groupId = template.getGroupId();
    this.artifactId = template.getArtifactId();
    this.version = template.getVersion();
    this.scope = template.getScope();
    this.docUrl = template.getDocUrl();
    this.id = template.getId();
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
  public String getScope() {

    return this.scope;
  }

  @Override
  public String getDocUrl() {

    return this.docUrl;
  }

}
