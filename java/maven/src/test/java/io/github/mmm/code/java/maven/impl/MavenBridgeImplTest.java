/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.java.maven.impl;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.mmm.code.java.maven.api.DependencyHelper;
import io.github.mmm.code.java.maven.api.MavenConstants;

/**
 * Test of {@link MavenBridgeImpl}.
 */
public class MavenBridgeImplTest extends Assertions implements MavenConstants {

  private static final File POM_XML = new File(MavenConstants.POM_XML);

  /**
   * Root Path where test data is stored
   */
  private static final File ROOT_TEST_PATH = new File("src/test/resources/testdata/");

  private static final Pattern VERSION_PATTERN = Pattern.compile("[0-9]+(\\.[0-9]+)*(-beta[0-9]+)?(-SNAPSHOT)?");

  /**
   * Test of {@link MavenBridgeImpl#readModel(File)}
   */
  @Test
  public void testReadModel() {

    // given
    MavenBridgeImpl reader = new MavenBridgeImpl();
    String groupId = "io.github.m-m-m";

    // when
    Model model = reader.readModel(POM_XML);

    // then
    assertThat(model).isNotNull();
    assertThat(model.getArtifactId()).isEqualTo("mmm-code-java-maven");
    assertThat(model.getParent().getGroupId()).isEqualTo(groupId);
    assertThat(model.getParent().getVersion()).isEqualTo("${revision}");
    verifyDependencies(model, DependencyHelper.create(PROJECT_GROUP_ID, "mmm-code-base", null),
        DependencyHelper.create("org.apache.maven", "maven-core", "${maven.version}"));
  }

  /**
   * Tests if a valid child revision can be read from the maven.config
   */
  @Test
  public void testResolveRevisionParameterOfMavenConfig() {

    // given
    File mavenProjectDirectory = new File(ROOT_TEST_PATH, "localmavenproject/maven.project/core"); // test Maven project

    // when
    MavenBridgeImpl reader = new MavenBridgeImpl();
    Model model = reader.readEffectiveModel(new File(mavenProjectDirectory, "pom.xml"));

    // then
    assertThat(model.getVersion()).isEqualTo("1.0.0-SNAPSHOT");
  }

  /**
   * Test of {@link MavenBridgeImpl#readEffectiveModel(File)}
   */
  @Test
  public void testReadProject() {

    // given
    MavenBridgeImpl reader = new MavenBridgeImpl();
    String groupId = "io.github.m-m-m";

    // when
    Model model = reader.readEffectiveModel(POM_XML);

    // then
    assertThat(model).isNotNull();
    assertThat(model.getArtifactId()).isEqualTo("mmm-code-java-maven");
    assertThat(model.getGroupId()).isEqualTo(groupId);
    assertThat(model.getVersion()).matches(VERSION_PATTERN);
    verifyDependencies(model, DependencyHelper.create(groupId, "mmm-code-base", "*"),
        DependencyHelper.create("org.apache.maven", "maven-core", "3.6.1"),
        DependencyHelper.create("org.assertj", "assertj-core", "3.23.1", SCOPE_TEST),
        DependencyHelper.create("org.junit.jupiter", "junit-jupiter", "5.9.1", SCOPE_TEST),
        DependencyHelper.create("org.mockito", "mockito-junit-jupiter", "4.9.0", SCOPE_TEST),
        DependencyHelper.create("ch.qos.logback", "logback-classic", "1.4.5", SCOPE_TEST));
  }

  private void verifyDependencies(Model model, Dependency... dependencies) {

    List<Dependency> dependenciesList = Arrays.asList(dependencies);
    Set<Dependency> expected = new HashSet<>(dependenciesList);
    assertThat(dependencies).hasSize(expected.size());
    List<Dependency> modelDependencies = model.getDependencies();
    Set<Dependency> remaining = new HashSet<>(modelDependencies);
    for (Dependency dependency : dependencies) {
      Dependency match = DependencyHelper.findFirst(remaining, dependency);
      if (match != null) {
        expected.remove(dependency);
        remaining.remove(match);
      }
    }
    if (!expected.isEmpty() || !remaining.isEmpty()) {
      StringBuilder failure = new StringBuilder(128);
      failure.append("Expected ");
      failure.append(modelDependencies);
      failure.append(" to contain ");
      failure.append(dependenciesList);
      failure.append(" but ");
      if (!expected.isEmpty()) {
        failure.append("could not find ");
        failure.append(expected);
      }
      if (!remaining.isEmpty()) {
        if (expected.isEmpty()) {
          failure.append("did not expect ");
        } else {
          failure.append(" and did not expect ");
        }
        failure.append(remaining);
      }
      fail(failure.toString());
    }
  }

}
