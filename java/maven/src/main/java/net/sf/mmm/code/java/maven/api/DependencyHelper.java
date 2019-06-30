/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.java.maven.api;

import java.util.Collection;
import java.util.Objects;

import org.apache.maven.model.Dependency;

/**
 * Simple helper to deal with maven {@link Dependency} objects.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class DependencyHelper implements MavenConstants {

  /**
   * {@link Dependency} does not properly implement {@link Dependency#equals(Object) equals}. This method compares two
   * given {@link Dependency} objects.
   *
   * @param dep1 the first {@link Dependency}.
   * @param dep2 the second {@link Dependency}.
   * @return {@code true} if both {@link Dependency} objects are semantically equal to each other.
   */
  public static boolean equals(Dependency dep1, Dependency dep2) {

    if (dep1 == dep2) {
      return true;
    }
    if (!Objects.equals(dep1.getGroupId(), dep2.getGroupId())) {
      return false;
    }
    if (!Objects.equals(dep1.getArtifactId(), dep2.getArtifactId())) {
      return false;
    }
    if (!Objects.equals(dep1.getVersion(), dep2.getVersion())) {
      return false;
    }
    if (!getType(dep1).equals(getType(dep2))) {
      return false;
    }
    if (!getScope(dep1).equals(getScope(dep2))) {
      return false;
    }
    if (dep1.isOptional() != dep2.isOptional()) {
      return false;
    }
    if (!Objects.equals(dep1.getClassifier(), dep2.getClassifier())) {
      return false;
    }
    if (!Objects.equals(dep1.getSystemPath(), dep2.getSystemPath())) {
      return false;
    }
    return true;
  }

  /**
   * @param dependency the {@link Dependency}.
   * @return the {@link Dependency#getScope() scope} or {@link #SCOPE_COMPILE} if {@code null}.
   */
  public static String getScope(Dependency dependency) {

    String scope = dependency.getScope();
    if (scope == null) {
      scope = SCOPE_COMPILE; // default
    }
    return scope;
  }

  /**
   * @param dependency the {@link Dependency}.
   * @return the {@link Dependency#getType() type} or {@link #TYPE_JAR} if {@code null}.
   */
  public static String getType(Dependency dependency) {

    String type = dependency.getType();
    if (type == null) {
      type = TYPE_JAR; // default
    }
    return type;
  }

  /**
   * @param dependencies the {@link Collection} of {@link Dependency} objects. See
   *        {@link org.apache.maven.model.Model#getDependencies()}.
   * @param dependency the {@link Dependency} to look for.
   * @return the number of {@link Dependency} objects in the given {@link Collection} that are
   *         {@link #equals(Dependency, Dependency) equal} to the given {@link Dependency}.
   */
  public static Dependency findFirst(Collection<Dependency> dependencies, Dependency dependency) {

    return dependencies.stream().filter(x -> equals(x, dependency)).findFirst().orElse(null);
  }

  /**
   * @param dependencies the {@link Collection} of {@link Dependency} objects. See
   *        {@link org.apache.maven.model.Model#getDependencies()}.
   * @param dependency the {@link Dependency} to look for.
   * @return the number of {@link Dependency} objects in the given {@link Collection} that are
   *         {@link #equals(Dependency, Dependency) equal} to the given {@link Dependency}.
   */
  public static int containsCount(Collection<Dependency> dependencies, Dependency dependency) {

    return (int) dependencies.stream().filter(x -> equals(x, dependency)).count();
  }

  /**
   * @param dependencies the {@link Collection} of {@link Dependency} objects. See
   *        {@link org.apache.maven.model.Model#getDependencies()}.
   * @param dependency the {@link Dependency} to look for.
   * @return {@code true} if the given {@link Collection} {@link Collection#contains(Object) contains} a
   *         {@link Dependency} {@link #equals(Dependency, Dependency) equal} to the given {@link Dependency}.
   */
  public static boolean contains(Collection<Dependency> dependencies, Dependency dependency) {

    return containsCount(dependencies, dependency) > 0;
  }

  /**
   * @param groupId - {@link Dependency#getGroupId()}.
   * @param artifactId - {@link Dependency#getArtifactId()}.
   * @param version - - {@link Dependency#getVersion()}.
   * @return the new {@link Dependency}.
   */
  public static Dependency create(String groupId, String artifactId, String version) {

    return create(groupId, artifactId, version, SCOPE_COMPILE);
  }

  /**
   * @param groupId - {@link Dependency#getGroupId()}.
   * @param artifactId - {@link Dependency#getArtifactId()}.
   * @param version - - {@link Dependency#getVersion()}.
   * @param scope - {@link Dependency#getScope()}.
   * @return the new {@link Dependency}.
   */
  public static Dependency create(String groupId, String artifactId, String version, String scope) {

    return create(groupId, artifactId, version, scope, TYPE_JAR);
  }

  /**
   * @param groupId - {@link Dependency#getGroupId()}.
   * @param artifactId - {@link Dependency#getArtifactId()}.
   * @param version - - {@link Dependency#getVersion()}.
   * @param scope - {@link Dependency#getScope()}.
   * @param type - {@link Dependency#getType()}.
   * @return the new {@link Dependency}.
   */
  public static Dependency create(String groupId, String artifactId, String version, String scope, String type) {

    return create(groupId, artifactId, version, scope, type, null);
  }

  /**
   * @param groupId - {@link Dependency#getGroupId()}.
   * @param artifactId - {@link Dependency#getArtifactId()}.
   * @param version - - {@link Dependency#getVersion()}.
   * @param scope - {@link Dependency#getScope()}.
   * @param type - {@link Dependency#getType()}.
   * @param classifier - {@link Dependency#getClassifier()}.
   * @return the new {@link Dependency}.
   */
  public static Dependency create(String groupId, String artifactId, String version, String scope, String type, String classifier) {

    Dependency dependency = new Dependency();
    dependency.setGroupId(groupId);
    dependency.setArtifactId(artifactId);
    dependency.setVersion(version);
    dependency.setType(type);
    dependency.setScope(scope);
    dependency.setClassifier(classifier);
    return dependency;
  }

  /**
   * @param dependency the regular artifact dependency (should not have a {@link Dependency#getClassifier()
   *        classifier}).
   * @return a new {@link Dependency} pointing to the sources of the given {@link Dependency} or {@code null} if the
   *         given {@link Dependency} does not point to a code artifact (e.g. {@link Dependency#getType() type} is
   *         {@link #TYPE_POM pom}).
   */
  public static Dependency createSource(Dependency dependency) {

    if (TYPE_POM.equals(dependency.getType())) {
      return null;
    }
    Dependency sourceDependency = dependency.clone();
    sourceDependency.setClassifier(CLASSIFIER_SOURCES);
    return sourceDependency;
  }

  /**
   * @param dependency the {@link Dependency}.
   * @return the GAV-coordninates as "«{@link Dependency#getGroupId() groupId}»:«{@link Dependency#getArtifactId()
   *         artifactId}»:«{@link Dependency#getVersion() version}»".
   */
  public static String getGav(Dependency dependency) {

    StringBuilder buffer = new StringBuilder();
    buffer.append(dependency.getGroupId());
    buffer.append(':');
    buffer.append(dependency.getArtifactId());
    buffer.append(':');
    buffer.append(dependency.getVersion());
    return buffer.toString();
  }

}
