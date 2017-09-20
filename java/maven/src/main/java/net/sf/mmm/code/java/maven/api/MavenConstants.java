/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.java.maven.api;

/**
 * Constants for maven.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface MavenConstants {

  /** The filename of the local POM. */
  String POM_XML = "pom.xml";

  /** The file extension of a POM from the repository. */
  String POM_EXTENSION = ".pom";

  /** The name of the maven configuration folder */
  String CONFIG_FOLDER = ".m2";

  /** The name of the maven repository folder */
  String REPOSITORY_FOLDER = "repository";

  /** The name of the maven configuration file. */
  String SETTINGS_XML = "settings.xml";

  /** {@link org.apache.maven.model.Model#getGroupId() GroupId} {@value}. */
  String PROJECT_GROUP_ID = "${project.groupId}";

  /** {@link org.apache.maven.model.Model#getArtifactId() ArtifactId} {@value}. */
  String PROJECT_ARTIFACT_ID = "${project.artifactId}";

  /** {@link org.apache.maven.model.Model#getVersion() Version} {@value}. */
  String PROJECT_VERSION = "${project.version}";

  /** {@link org.apache.maven.model.Dependency#getScope() Scope} {@value}. */
  String SCOPE_COMPILE = "compile";

  /** {@link org.apache.maven.model.Dependency#getScope() Scope} {@value}. */
  String SCOPE_TEST = "test";

  /** {@link org.apache.maven.model.Dependency#getScope() Scope} {@value}. */
  String SCOPE_RUNTIME = "runtime";

  /** {@link org.apache.maven.model.Dependency#getScope() Scope} {@value}. */
  String SCOPE_PROVIDED = "provided";

  /** {@link org.apache.maven.model.Dependency#getType() Type} {@value}. */
  String TYPE_JAR = "jar";

  /** {@link org.apache.maven.model.Dependency#getType() Type} {@value}. */
  String TYPE_POM = "pom";

  /** {@link org.apache.maven.model.Dependency#getClassifier() Classifier} {@value}. */
  String CLASSIFIER_SOURCES = "sources";

  /** Default for {@link org.apache.maven.model.Build#getSourceDirectory()} */
  String DEFAULT_SOURCE_DIRECTORY = "src/main/java";

  /** Default for {@link org.apache.maven.model.Build#getTestSourceDirectory()} */
  String DEFAULT_TEST_SOURCE_DIRECTORY = "src/test/java";

  /** Default for {@link org.apache.maven.model.Build#getDirectory()} */
  String DEFAULT_BUILD_DIRECTORY = "target";

  /** Default for {@link org.apache.maven.model.Build#getOutputDirectory()} */
  String DEFAULT_OUTPUT_DIRECTORY = "target/classes";

  /** Default for {@link org.apache.maven.model.Build#getTestOutputDirectory()} */
  String DEFAULT_TEST_OUTPUT_DIRECTORY = "target/test-classes";

}
