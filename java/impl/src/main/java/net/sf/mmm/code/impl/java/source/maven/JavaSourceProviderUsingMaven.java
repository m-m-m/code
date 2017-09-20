/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.source.maven;

import java.io.File;
import java.security.CodeSource;
import java.util.Objects;
import java.util.function.Supplier;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;

import net.sf.mmm.code.base.source.CodeSourceHelper;
import net.sf.mmm.code.impl.java.JavaContext;
import net.sf.mmm.code.impl.java.JavaPackage;
import net.sf.mmm.code.impl.java.source.AbstractJavaSourceProvider;
import net.sf.mmm.code.impl.java.source.JavaSource;
import net.sf.mmm.code.impl.java.source.JavaSourceProvider;
import net.sf.mmm.code.java.maven.api.DependencyHelper;
import net.sf.mmm.code.java.maven.api.MavenBridge;
import net.sf.mmm.code.java.maven.api.ModelHelper;
import net.sf.mmm.code.java.maven.impl.MavenBridgeImpl;

/**
 * Implemenation of {@link JavaSourceProvider} using maven to read and extract metadata from POMs.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaSourceProviderUsingMaven extends AbstractJavaSourceProvider {

  private final MavenBridge mavenBridge;

  /**
   * The constructor.
   */
  public JavaSourceProviderUsingMaven() {

    super();
    this.mavenBridge = new MavenBridgeImpl();
  }

  @Override
  public JavaSource create(CodeSource source) {

    Objects.requireNonNull(source, "source");
    return new JavaSourceUsingMaven(this, source, () -> parseModel(CodeSourceHelper.asFile(source.getLocation())));
  }

  @Override
  public JavaSource create(File byteCodeLocation, File sourceCodeLocation) {

    File location;
    if (byteCodeLocation != null) {
      location = byteCodeLocation;
    } else {
      location = sourceCodeLocation;
    }
    JavaPackage superLayerPackage = null; // TODO
    return new JavaSourceUsingMaven(this, byteCodeLocation, sourceCodeLocation, superLayerPackage, () -> parseModel(location));
  }

  private Model parseModel(File location) {

    File pomFile = this.mavenBridge.findPom(location);
    if ((pomFile == null) || !pomFile.isFile()) {
      return null;
    }
    return this.mavenBridge.readEffectiveModel(pomFile);
  }

  JavaSource create(Dependency dependency) {

    File byteCodeArtifact = this.mavenBridge.findArtifact(dependency);
    File sourceCodeArtifact = null;
    Dependency sourceDependency = DependencyHelper.createSource(dependency);
    if (sourceDependency != null) {
      sourceCodeArtifact = this.mavenBridge.findArtifact(sourceDependency);
      sourceCodeArtifact = CodeSourceHelper.getFileOrNull(sourceCodeArtifact);
    }
    return getContext().getOrCreateSource(byteCodeArtifact, sourceCodeArtifact);
  }

  public JavaSource createFromLocalMavenProject(JavaContext parentContext) {

    return createFromLocalMavenProject(parentContext, new File("."));
  }

  public JavaSource createFromLocalMavenProject(JavaContext parentContext, File location) {

    final Model model = parseModel(location);
    if (model == null) {
      throw new IllegalArgumentException("Could not find pom.xml for basedir: " + location);
    }
    Supplier<Model> modelSupplier = () -> model;
    File byteCodeLocation = ModelHelper.getOutputDirectory(model);
    File sourceCodeLocation = ModelHelper.getSourceDirectory(model);
    JavaPackage superLayerPackage = parentContext.getSource().getRootPackage();
    JavaSourceUsingMaven compileDependency = new JavaSourceUsingMaven(this, byteCodeLocation, sourceCodeLocation, superLayerPackage, modelSupplier);
    File testSourceCodeLocation = ModelHelper.getSourceDirectory(model);
    JavaSourceUsingMaven testDependency = new JavaSourceUsingMaven(this, compileDependency, byteCodeLocation, testSourceCodeLocation, modelSupplier);
    return new JavaSourceUsingMaven(this, model.getPomFile().getParentFile().toString(), compileDependency, testDependency, modelSupplier);
  }

}
