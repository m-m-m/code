/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.source;

/**
 * Implementation of {@link AbstractCodeSourceDescriptor} as mutable Java bean.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class CodeSourceDescriptorBean extends AbstractCodeSourceDescriptor {

  private String groupId;

  private String artifactId;

  private String version;

  private String id;

  private String docUrl;

  /**
   * The constructor.
   */
  public CodeSourceDescriptorBean() {

    super();
  }

  /**
   * The constructor.
   *
   * @param id the {@link #getId() id}.
   */
  public CodeSourceDescriptorBean(String id) {

    super();
    this.id = id;
  }

  /**
   * The constructor.
   *
   * @param groupId the {@link #getGroupId() groupId}.
   * @param artifactId the {@link #getArtifactId() artifactId}.
   * @param version the {@link #getVersion() version}.
   * @param docUrl the {@link #getDocUrl() doc URL}.
   */
  public CodeSourceDescriptorBean(String groupId, String artifactId, String version, String docUrl) {

    super();
    this.groupId = groupId;
    this.artifactId = artifactId;
    this.version = version;
    this.docUrl = docUrl;
    this.id = createId();
  }

  @Override
  public String getId() {

    if (this.id == null) {
      return createId();
    }
    return this.id;
  }

  /**
   * @param id the new value of {@link #getId()}.
   */
  public void setId(String id) {

    this.id = id;
  }

  @Override
  public String getGroupId() {

    return this.groupId;
  }

  /**
   * @param groupId the new value of {@link #getGroupId()}.
   */
  public void setGroupId(String groupId) {

    this.groupId = groupId;
  }

  @Override
  public String getArtifactId() {

    return this.artifactId;
  }

  /**
   * @param artifactId the new value of {@link #getArtifactId()}.
   */
  public void setArtifactId(String artifactId) {

    this.artifactId = artifactId;
  }

  @Override
  public String getVersion() {

    return this.version;
  }

  /**
   * @param version the new value of {@link #getVersion()}.
   */
  public void setVersion(String version) {

    this.version = version;
  }

  @Override
  public String getDocUrl() {

    return this.docUrl;
  }

  /**
   * @param docUrl the new value of {@link #getDocUrl()}.
   */
  public void setDocUrl(String docUrl) {

    this.docUrl = docUrl;
  }

}
