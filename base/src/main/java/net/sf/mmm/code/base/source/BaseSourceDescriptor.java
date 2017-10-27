/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.source;

import java.util.Objects;

import net.sf.mmm.code.api.source.CodeSourceDescriptor;

/**
 * Base implementation of {@link CodeSourceDescriptor}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BaseSourceDescriptor implements CodeSourceDescriptor {

  /**
   * @return the {@link #getId() ID} composed as "«{@link #getGroupId() groupId}»:«{@link #getArtifactId()
   *         artifactId}»:«{@link #getVersion() version}»".
   */
  protected String createId() {

    StringBuilder buffer = new StringBuilder();
    String groupId = getGroupId();
    if (groupId != null) {
      buffer.append(groupId);
      buffer.append(':');
    }
    buffer.append(getArtifactId());
    buffer.append(':');
    buffer.append(getVersion());
    return buffer.toString();
  }

  @Override
  public final boolean equals(Object obj) {

    if (obj == this) {
      return true;
    }
    if (!(obj instanceof BaseSourceDescriptor)) {
      return false;
    }
    BaseSourceDescriptor other = (BaseSourceDescriptor) obj;
    if (!Objects.equals(getGroupId(), other.getGroupId())) {
      return false;
    }
    if (!Objects.equals(getArtifactId(), other.getArtifactId())) {
      return false;
    }
    if (!Objects.equals(getVersion(), other.getVersion())) {
      return false;
    }
    assert (getId().equals(other.getId()));
    return true;
  }

  @Override
  public final int hashCode() {

    return Objects.hashCode(getId());
  }

  @Override
  public final String toString() {

    return getId();
  }

}
