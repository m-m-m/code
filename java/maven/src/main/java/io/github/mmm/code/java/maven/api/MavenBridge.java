/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.java.maven.api;

import java.io.File;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;

/**
 * A bridge to get selected features from maven such as reading POMs.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface MavenBridge {

  /**
   * @param source the {@link File} pointing to a code source of any kind. May be a JAR from the local repository, a
   *        source folder of a maven project, or any other kind of source.
   * @return the corresponding {@code pom.xml} file or {@code null} if not found.
   */
  File findPom(File source);

  /**
   * @param dependency the {@link Dependency}.
   * @return the corresponding {@code pom.xml} file or {@code null} if not found.
   */
  File findPom(Dependency dependency);

  /**
   * @param dependency the {@link Dependency}.
   * @return the {@link File} pointing to the artifact of the {@link Dependency}.
   */
  File findArtifact(Dependency dependency);

  /**
   * @param artifact the {@link File} (potentially) pointing to an artifact (e.g. JAR) in the local repository.
   * @return the corresponding sources archive or {@code null} if the given {@link File} was not pointing to a maven
   *         artifact.
   */
  File findArtifactSources(File artifact);

  /**
   * @param pomFile the {@link File} pointing to the maven {@code pom.xml} file to parse.
   * @return the plain {@link Model} of the parsed POM.
   * @see #readEffectiveModel(File)
   */
  Model readModel(File pomFile);

  /**
   * @param pomFile the {@link File} pointing to the maven {@code pom.xml} file to parse.
   * @return the effective {@link Model} of the parsed POM with variables resolved and parent inheritance.
   * @see #readModel(File)
   */
  Model readEffectiveModel(File pomFile);

  /**
   * @param location the location where the module or POM of the project should be found.
   * @param fallback - {@code true} to use {@link #readModel(File) raw model} as fallback in case
   *        {@link #readEffectiveModel(File) effective model} can not be computed, {@code false} otherwise (fast-fail).
   * @return the {@link #readEffectiveModel(File) effective model} or if {@code fallback} is {@code true} and the
   *         {@link #readEffectiveModel(File) effective model} failed to be computed, the {@link #readModel(File) raw
   *         model}.
   */
  Model readEffectiveModelFromLocation(File location, boolean fallback);

  /**
   * @param location the location where the module or POM of the project should be found.
   * @return the {@link #readEffectiveModel(File) effective model} or if the {@link #readEffectiveModel(File) effective
   *         model} failed to be computed, the {@link #readModel(File) raw model}.
   */
  default Model readEffectiveModelFromLocationWithFallback(File location) {

    return readEffectiveModelFromLocation(location, true);
  }

}
