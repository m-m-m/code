/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.source.maven;

import java.io.File;
import java.security.CodeSource;
import java.util.Objects;
import java.util.function.Supplier;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;

import net.sf.mmm.code.base.loader.BaseSourceCodeProviderDirectory;
import net.sf.mmm.code.base.loader.BaseSourceLoader;
import net.sf.mmm.code.base.loader.SourceCodeProvider;
import net.sf.mmm.code.base.source.BaseSource;
import net.sf.mmm.code.base.source.BaseSourceHelper;
import net.sf.mmm.code.base.source.BaseSourceImpl;
import net.sf.mmm.code.base.source.BaseSourceProvider;
import net.sf.mmm.code.base.source.BaseSourceProviderImpl;
import net.sf.mmm.code.impl.java.JavaContext;
import net.sf.mmm.code.impl.java.loader.JavaSourceLoader;
import net.sf.mmm.code.java.maven.api.DependencyHelper;
import net.sf.mmm.code.java.maven.api.MavenBridge;
import net.sf.mmm.code.java.maven.api.MavenConstants;
import net.sf.mmm.code.java.maven.api.ModelHelper;
import net.sf.mmm.code.java.maven.impl.MavenBridgeImpl;

/**
 * Implemenation of {@link BaseSourceProvider} using maven to read and extract metadata from POMs.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaSourceProviderUsingMaven extends BaseSourceProviderImpl implements MavenConstants {

  private final MavenBridge mavenBridge;

  /**
   * The constructor.
   */
  public JavaSourceProviderUsingMaven() {

    super();
    this.mavenBridge = new MavenBridgeImpl();
  }

  @Override
  public BaseSource create(CodeSource source) {

    Objects.requireNonNull(source, "source");
    Supplier<Model> supplier = () -> parseModel(BaseSourceHelper.asFile(source.getLocation()));
    // TODO find sources archive from maven repo corresponding to CodeSource
    SourceCodeProvider sourceCodeProvider = null;
    BaseSourceLoader loader = new JavaSourceLoader(sourceCodeProvider);
    return new JavaSourceUsingMaven(this, source, supplier, loader);
  }

  @Override
  public BaseSource create(File byteCodeLocation, File sourceCodeLocation) {

    File location;
    if (byteCodeLocation != null) {
      location = byteCodeLocation;
    } else {
      location = sourceCodeLocation;
    }
    return new JavaSourceUsingMaven(this, byteCodeLocation, sourceCodeLocation, () -> parseModel(location), null, createLoader(sourceCodeLocation));
  }

  private BaseSourceLoader createLoader(File sourceCodeLocation) {

    SourceCodeProvider sourceCodeProvider;
    if (sourceCodeLocation == null) {
      sourceCodeProvider = null;
    } else if (sourceCodeLocation.isDirectory()) {
      sourceCodeProvider = new BaseSourceCodeProviderDirectory(sourceCodeLocation);
    } else {
      // TODO
      // throw new IllegalStateException("TODO implement");
      sourceCodeProvider = null;
      // sourceCodeProvider = new BaseSourceCodeProviderArchive(sourceCodeLocation);
    }
    return new JavaSourceLoader(sourceCodeProvider);
  }

  private Model parseModel(File location) {

    File pomFile = this.mavenBridge.findPom(location);
    if ((pomFile == null) || !pomFile.isFile()) {
      return null;
    }
    return this.mavenBridge.readEffectiveModel(pomFile);
  }

  BaseSource createSource(Dependency dependency) {

    File byteCodeArtifact = this.mavenBridge.findArtifact(dependency);
    JavaContext context = (JavaContext) getContext();
    String id = BaseSourceImpl.getNormalizedId(byteCodeArtifact);
    return context.getOrCreateSource(id, () -> createSource(dependency, byteCodeArtifact));
  }

  private BaseSource createSource(Dependency dependency, File byteCodeArtifact) {

    File sourceCodeArtifact = null;
    Dependency sourceDependency = DependencyHelper.createSource(dependency);
    if (sourceDependency != null) {
      sourceCodeArtifact = this.mavenBridge.findArtifact(sourceDependency);
      sourceCodeArtifact = BaseSourceHelper.getFileOrNull(sourceCodeArtifact);
    }
    BaseSourceLoader loader = createLoader(sourceCodeArtifact);
    return new JavaSourceUsingMaven(this, byteCodeArtifact, sourceCodeArtifact, () -> parseModel(byteCodeArtifact), dependency.getScope(), loader);
  }

  public BaseSourceImpl createFromLocalMavenProject(JavaContext parentContext) {

    return createFromLocalMavenProject(parentContext, new File(".").getAbsoluteFile().getParentFile());
  }

  public BaseSourceImpl createFromLocalMavenProject(JavaContext parentContext, File location) {

    final Model model = parseModel(location);
    if (model == null) {
      throw new IllegalArgumentException("Could not find pom.xml for basedir: " + location);
    }
    Supplier<Model> modelSupplier = () -> model;
    File byteCodeLocation = ModelHelper.getOutputDirectory(model);
    File sourceCodeLocation = ModelHelper.getSourceDirectory(model);
    JavaSourceUsingMaven compileDependency = new JavaSourceUsingMaven(this, byteCodeLocation, sourceCodeLocation, modelSupplier, SCOPE_COMPILE,
        createLoader(sourceCodeLocation));
    File testByteCodeLocation = ModelHelper.getTestOutputDirectory(model);
    File testSourceCodeLocation = ModelHelper.getTestSourceDirectory(model);
    BaseSourceLoader testLoader = createLoader(testSourceCodeLocation);
    JavaSourceUsingMaven testDependency = new JavaSourceUsingMaven(this, compileDependency, testByteCodeLocation, testSourceCodeLocation, modelSupplier,
        testLoader);
    return testDependency;
  }

}
