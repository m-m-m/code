/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.java.maven.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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

import net.sf.mmm.code.java.maven.api.MavenBridge;
import net.sf.mmm.code.java.maven.api.MavenConstants;
import net.sf.mmm.util.io.api.IoMode;
import net.sf.mmm.util.io.api.RuntimeIoException;
import net.sf.mmm.util.xml.base.XmlInvalidException;

/**
 * Implementation of {@link MavenBridge}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class MavenBridgeImpl implements MavenBridge, MavenConstants {

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
        pomFile = new File(source.getParent(), basename + POM_EXTENSION);
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

    try {
      ModelBuildingRequest buildingRequest = new DefaultModelBuildingRequest().setSystemProperties(System.getProperties()).setPomFile(pomFile)
          .setModelResolver(this.resolver);
      DefaultModelBuilder defaultModelBuilder = new DefaultModelBuilderFactory().newInstance();
      ModelBuildingResult buildingResult = defaultModelBuilder.build(buildingRequest);
      return buildingResult.getEffectiveModel();
    } catch (ModelBuildingException e) {
      throw new IllegalStateException(e);
    }
  }

}
