/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.java.maven.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.mmm.code.java.maven.api.MavenBridge;
import net.sf.mmm.code.java.maven.api.MavenConstants;
import net.sf.mmm.util.io.api.IoMode;
import net.sf.mmm.util.io.api.RuntimeIoException;
import net.sf.mmm.util.xml.base.XmlInvalidException;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.building.DefaultModelBuilder;
import org.apache.maven.model.building.DefaultModelBuilderFactory;
import org.apache.maven.model.building.DefaultModelBuildingRequest;
import org.apache.maven.model.building.ModelBuildingException;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.model.building.ModelBuildingResult;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link MavenBridge}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class MavenBridgeImpl implements MavenBridge, MavenConstants {

  private static final Logger LOG = LoggerFactory.getLogger(MavenBridgeImpl.class);

  private static final Pattern PATTERN_PROPERTY_PARAMETER = Pattern
      .compile("-D([a-zA-Z0-9.-_]+)=\"?(([^ ]| (?!-D))*)\"?");

  private static final MavenBridgeImpl INSTANCE = new MavenBridgeImpl();

  private final MavenXpp3Reader pomReader;

  private final MavenResolver resolver;

  /**
   * The constructor.
   */
  public MavenBridgeImpl() {

    this(null);
  }

  /**
   * The constructor.
   *
   * @param localRepository the {@link File} pointing to the maven local repository.
   */
  public MavenBridgeImpl(File localRepository) {

    super();
    this.pomReader = new MavenXpp3Reader();
    File localRepo = localRepository;
    if (localRepo == null) {
      localRepo = MavenLocalRepositoryLocator.getLocalRepository(this);
    }
    this.resolver = new MavenResolver(localRepo);
  }

  @Override
  public File findPom(File source) {

    String filename = source.getName();
    if (source.isFile()) {
      String basename;
      File pomFile;
      int lastDot = filename.lastIndexOf('.');
      if (lastDot > 0) {
        basename = filename.substring(0, lastDot);
        // If the file is already a pom.xml, we should not change it to pom.pom
        if (basename.equals("pom")) {
          pomFile = source;
        } else {
          pomFile = new File(source.getParent(), basename + POM_EXTENSION);
        }
        if (pomFile.exists()) {
          return pomFile;
        }
      }
      int lastSlash = filename.indexOf('-');
      if (lastSlash > 0) {
        basename = filename.substring(0, lastSlash);
        pomFile = new File(source.getParent(), basename + POM_EXTENSION);
        if (pomFile.exists()) {
          return pomFile;
        }
      }
    } else if (source.isDirectory()) {
      return findPomFromFolder(source, 0);
    }
    return null;
  }

  @Override
  public File findArtifactSources(File artifact) {

    if (artifact.isFile()) {
      String filename = artifact.getName();
      int lastDot = filename.lastIndexOf('.');
      if (lastDot > 0) {
        String extension = filename.substring(lastDot);
        String sourcesBasename = filename.substring(0, lastDot) + "-" + CLASSIFIER_SOURCES;
        File sourcesFile = new File(artifact.getParent(), sourcesBasename + extension);
        if (sourcesFile.exists()) {
          return sourcesFile;
        }
        sourcesFile = new File(artifact.getParent(), sourcesBasename + ".zip");
        if (sourcesFile.exists()) {
          return sourcesFile;
        }
      }
    }
    return null;
  }

  private File findPomFromFolder(File folder, int recursion) {

    if (folder == null) {
      return null;
    }
    File pomFile = new File(folder, POM_XML);
    if (pomFile.exists()) {
      return pomFile;
    }
    if (recursion > 4) {
      return null;
    }
    return findPomFromFolder(folder.getParentFile(), recursion + 1);
  }

  @Override
  public File findPom(Dependency dependency) {

    return this.resolver.resolvePom(dependency);
  }

  @Override
  public File findArtifact(Dependency dependency) {

    return this.resolver.resolveArtifact(dependency);
  }

  @Override
  public Model readModel(File pomFile) {

    LOG.debug("Reading raw model of {}", pomFile);
    try (InputStream in = new FileInputStream(pomFile)) {
      Model model = this.pomReader.read(in);
      model.setPomFile(pomFile);
      return model;
    } catch (IOException e) {
      throw new RuntimeIoException(e, IoMode.READ);
    } catch (XmlPullParserException e) {
      throw new XmlInvalidException(e, pomFile);
    }
  }

  @Override
  public Model readEffectiveModel(File pomFile) {

    LOG.debug("Reading effective model of {}", pomFile);
    try {
      Properties properties = System.getProperties();
      properties = resolveProperties(properties, pomFile.getAbsoluteFile().getParentFile());
      ModelBuildingRequest buildingRequest = new DefaultModelBuildingRequest().setSystemProperties(properties)
          .setPomFile(pomFile).setModelResolver(this.resolver);
      DefaultModelBuilder defaultModelBuilder = new DefaultModelBuilderFactory().newInstance();
      ModelBuildingResult buildingResult = defaultModelBuilder.build(buildingRequest);
      return buildingResult.getEffectiveModel();
    } catch (ModelBuildingException e) {
      throw new IllegalStateException("Failed to read effective model for " + pomFile
          + ". Try to run 'mvn help:effective-pom' in that folder manually. If that fails, fix your pom.xml. Otherwise report a bug at https://github.com/m-m-m/code/issues/new/choose",
          e);
    }
  }

  @Override
  public Model readEffectiveModelFromLocation(File location, boolean fallback) {

    File pomFile = findPom(location);
    if ((pomFile == null) || !pomFile.isFile()) {
      return null;
    }
    try {
      return readEffectiveModel(pomFile);
    } catch (Throwable e) {
      if (fallback) {
        LOG.warn("Failed to resolve effective POM for {}. Trying to continue without resolving effective POM...",
            location, e);
        return readModel(pomFile);
      } else {
        throw e;
      }
    }
  }

  private Properties resolveProperties(Properties properties, File projectBaseDir) {

    if (projectBaseDir.getParentFile() == null) {
      return properties;
    }

    try {
      File mvnDir = new File(projectBaseDir, ".mvn");
      if (mvnDir.isDirectory()) {
        File mvnConfig = new File(mvnDir, "maven.config");
        if (mvnConfig.isFile()) {
          List<String> lines = Files.readAllLines(mvnConfig.toPath());
          for (String line : lines) {
            resolveProperties(properties, line.trim());
          }
          return properties;
        }
      }
      return resolveProperties(properties, projectBaseDir.getParentFile());
    } catch (IOException e) {
      throw new IllegalStateException("Error reading maven.config from " + projectBaseDir, e);
    }
  }

  private void resolveProperties(Properties properties, String line) {

    Matcher matcher = PATTERN_PROPERTY_PARAMETER.matcher(line);
    while (matcher.find()) {
      String key = matcher.group(1);
      String value = matcher.group(2);
      properties.put(key, value);
    }
  }

  /**
   * @return the default instance.
   */
  public static MavenBridge getDefault() {

    return INSTANCE;
  }

}
