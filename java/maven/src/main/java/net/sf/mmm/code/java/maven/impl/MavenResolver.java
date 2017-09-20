/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.java.maven.impl;

import java.io.File;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Repository;
import org.apache.maven.model.building.FileModelSource;
import org.apache.maven.model.building.ModelSource;
import org.apache.maven.model.resolution.ModelResolver;

import net.sf.mmm.code.java.maven.api.MavenConstants;
import net.sf.mmm.util.file.api.FileNotExistsException;

/**
 * Simple implementation of {@link ModelResolver}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
@SuppressWarnings("deprecation")
public class MavenResolver implements ModelResolver, MavenConstants {

  private final File localRepository;

  /**
   * The constructor.
   */
  public MavenResolver() {

    this(MavenLocalRepositoryLocator.getDefaultLocalRepository());
  }

  /**
   * The constructor.
   *
   * @param localRepository the path to the local maven repository including a terminating slash.
   */
  public MavenResolver(File localRepository) {

    super();
    this.localRepository = localRepository;
    if (!this.localRepository.isDirectory()) {
      throw new FileNotExistsException(this.localRepository.getPath(), true);
    }
  }

  @Override
  public ModelSource resolveModel(String groupId, String artifactId, String version) {

    File pomFile = resolveFile(groupId, artifactId, version, null, TYPE_POM);
    return new FileModelSource(pomFile);
  }

  File resolveArtifact(Dependency dependency) {

    return resolveFile(dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion(), dependency.getClassifier(), dependency.getType());
  }

  File resolvePom(Dependency dependency) {

    return resolveFile(dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion(), dependency.getClassifier(), TYPE_POM);
  }

  private File resolveFile(String groupId, String artifactId, String version, String classifier, String type) {

    File groupFolder = new File(this.localRepository, groupId.replace('.', '/'));
    File artifactFolder = new File(groupFolder, artifactId);
    File versionFolder = new File(artifactFolder, version);
    StringBuilder pomFilename = new StringBuilder(artifactId);
    pomFilename.append('-');
    pomFilename.append(version);
    if ((classifier != null) && !TYPE_POM.equals(type)) {
      pomFilename.append('-');
      pomFilename.append(classifier);
    }
    pomFilename.append('.');
    pomFilename.append(type);
    return new File(versionFolder, pomFilename.toString());
  }

  @Override
  public ModelSource resolveModel(Parent parent) {

    return resolveModel(parent.getGroupId(), parent.getArtifactId(), parent.getVersion());
  }

  @Override
  public ModelSource resolveModel(Dependency dependency) {

    File pomFile = resolvePom(dependency);
    return new FileModelSource(pomFile);
  }

  @Override
  public void addRepository(Repository repository) {

    // ignore
  }

  @Override
  public void addRepository(Repository repository, boolean replace) {

    // ignore
  }

  @Override
  public ModelResolver newCopy() {

    return this;
  }

}
