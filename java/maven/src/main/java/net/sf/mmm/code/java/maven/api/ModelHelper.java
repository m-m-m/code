/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.java.maven.api;

import java.io.File;

import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;

/**
 * Simple helper to deal with maven {@link Model} objects.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class ModelHelper implements MavenConstants {

  /**
   * @param model the {@link Model}.
   * @return the {@link Build#getSourceDirectory() source directory} as {@link File}.
   */
  public static File getSourceDirectory(Model model) {

    String sourceDirectory = DEFAULT_SOURCE_DIRECTORY;
    Build build = model.getBuild();
    if (build != null) {
      String srcDir = build.getSourceDirectory();
      if (srcDir != null) {
        sourceDirectory = srcDir;
      }
    }
    return getDirectory(model, sourceDirectory);
  }

  /**
   * @param model the {@link Model}.
   * @return the {@link Build#getTestSourceDirectory() test source directory} as {@link File}.
   */
  public static File getTestSourceDirectory(Model model) {

    String testSourceDirectory = DEFAULT_TEST_SOURCE_DIRECTORY;
    Build build = model.getBuild();
    if (build != null) {
      String testSrcDir = build.getTestSourceDirectory();
      if (testSrcDir != null) {
        testSourceDirectory = testSrcDir;
      }
    }
    return getDirectory(model, testSourceDirectory);
  }

  /**
   * @param model the {@link Model}.
   * @return the {@link Build#getDirectory() build directory} as {@link File}.
   */
  public static File getBuildDirectory(Model model) {

    String buildDirectory = DEFAULT_BUILD_DIRECTORY;
    Build build = model.getBuild();
    if (build != null) {
      String buildDir = build.getDirectory();
      if (buildDir != null) {
        buildDirectory = buildDir;
      }
    }
    return getDirectory(model, buildDirectory);
  }

  /**
   * @param model the {@link Model}.
   * @return the {@link Build#getOutputDirectory() output directory} as {@link File}.
   */
  public static File getOutputDirectory(Model model) {

    String outputDirectory = DEFAULT_OUTPUT_DIRECTORY;
    Build build = model.getBuild();
    if (build != null) {
      String outDir = build.getOutputDirectory();
      if (outDir != null) {
        outputDirectory = outDir;
      }
    }
    return getDirectory(model, outputDirectory);
  }

  private static File getDirectory(Model model, String path) {

    File file = new File(path);
    if (file.isAbsolute()) {
      return file;
    }
    return new File(model.getPomFile().getParentFile(), path);
  }

  /**
   * @param model the {@link Model}.
   * @return the {@link Build#getTestOutputDirectory() test output directory} as {@link File}.
   */
  public static File getTestOutputDirectory(Model model) {

    String testOutputDirectory = DEFAULT_TEST_OUTPUT_DIRECTORY;
    Build build = model.getBuild();
    if (build != null) {
      String testOutDir = build.getTestOutputDirectory();
      if (testOutDir != null) {
        testOutputDirectory = testOutDir;
      }
    }
    return getDirectory(model, testOutputDirectory);
  }

  /**
   * @param model the {@link Model} of the maven project.
   * @return the GAV-coordninates as "«{@link Model#getGroupId() groupId}»:«{@link Model#getArtifactId()
   *         artifactId}»:«{@link Model#getVersion() version}»".
   */
  public static String getGav(Model model) {

    StringBuilder buffer = new StringBuilder();
    buffer.append(model.getGroupId());
    buffer.append(':');
    buffer.append(model.getArtifactId());
    buffer.append(':');
    buffer.append(model.getVersion());
    return buffer.toString();
  }

  /**
   * @param parent the {@link Parent} of a maven project.
   * @return the GAV-coordninates as "«{@link Parent#getGroupId() groupId}»:«{@link Parent#getArtifactId()
   *         artifactId}»:«{@link Parent#getVersion() version}»".
   */
  public static String getGav(Parent parent) {

    StringBuilder buffer = new StringBuilder();
    buffer.append(parent.getGroupId());
    buffer.append(':');
    buffer.append(parent.getArtifactId());
    buffer.append(':');
    buffer.append(parent.getVersion());
    return buffer.toString();
  }

}
