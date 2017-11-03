/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.source.maven;

import java.io.File;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;

import net.sf.mmm.code.api.source.CodeSourceDescriptor;
import net.sf.mmm.code.base.BasePackage;
import net.sf.mmm.code.base.source.BaseSourceDescriptorType;
import net.sf.mmm.code.impl.java.JavaContext;
import net.sf.mmm.code.impl.java.source.JavaSource;
import net.sf.mmm.code.impl.java.source.JavaSourceDependencies;
import net.sf.mmm.code.java.maven.api.MavenConstants;
import net.sf.mmm.code.java.maven.api.ModelHelper;
import net.sf.mmm.util.component.api.ResourceMissingException;

/**
 * {@link JavaSource} that provides {@link #getDependencies() dependencies} and {@link #getDescriptor()
 * descriptor} from maven POMs.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaSourceUsingMaven extends JavaSource implements MavenConstants {

  private final JavaSourceProviderUsingMaven sourceProvider;

  private JavaSource compileDependency;

  private Supplier<Model> modelSupplier;

  private Model model;

  private String scope;

  /**
   * The constructor.
   *
   * @param sourceProvider the {@link JavaSourceProviderUsingMaven maven source provider} required for lazy
   *        fabrication of {@link #getDependencies() dependencies}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May not be {@code null}.
   * @param modelSupplier the {@link Supplier} for the maven {@link Model}.
   */
  public JavaSourceUsingMaven(JavaSourceProviderUsingMaven sourceProvider, CodeSource reflectiveObject, Supplier<Model> modelSupplier) {

    this(sourceProvider, reflectiveObject, null, null, null, null, modelSupplier, null, null, null);
  }

  /**
   * The constructor for test-source (e.g. "{@code src/test/java}").
   *
   * @param sourceProvider the {@link JavaSourceProviderUsingMaven maven source provider} required for lazy
   *        fabrication of {@link #getDependencies() dependencies}.
   * @param compileDependency the {@link JavaSource} for the compile dependencies (representing
   *        {@code src/main/java} with compile dependencies as {@link #getDependencies() dependencies}.
   * @param byteCodeLocation the {@link #getByteCodeLocation() byte code location}.
   * @param sourceCodeLocation the {@link #getSourceCodeLocation() source code location}.
   * @param modelSupplier the {@link Supplier} for the maven {@link Model}.
   */
  public JavaSourceUsingMaven(JavaSourceProviderUsingMaven sourceProvider, JavaSource compileDependency, File byteCodeLocation, File sourceCodeLocation,
      Supplier<Model> modelSupplier) {

    this(sourceProvider, null, byteCodeLocation, sourceCodeLocation, null, compileDependency.getRootPackage(), modelSupplier, compileDependency, null,
        SCOPE_TEST);
  }

  /**
   * The constructor.
   *
   * @param sourceProvider the {@link JavaSourceProviderUsingMaven maven source provider} required for lazy
   *        fabrication of {@link #getDependencies() dependencies}.
   * @param byteCodeLocation the {@link #getByteCodeLocation() byte code location}.
   * @param sourceCodeLocation the {@link #getSourceCodeLocation() source code location}.
   * @param superLayerPackage the {@link BasePackage#getSuperLayerPackage() super layer package} to inherit
   *        from.
   * @param modelSupplier the {@link Supplier} for the maven {@link Model}.
   * @param scope the {@link #getScope() scope}.
   */
  public JavaSourceUsingMaven(JavaSourceProviderUsingMaven sourceProvider, File byteCodeLocation, File sourceCodeLocation, BasePackage superLayerPackage,
      Supplier<Model> modelSupplier, String scope) {

    this(sourceProvider, null, byteCodeLocation, sourceCodeLocation, null, superLayerPackage, modelSupplier, null, null, scope);
  }

  /**
   * The constructor for a local maven project.
   *
   * @see JavaSourceProviderUsingMaven#createFromLocalMavenProject(JavaContext, File)
   *
   * @param sourceProvider the {@link JavaSourceProviderUsingMaven maven source provider} required for lazy
   *        fabrication of {@link #getDependencies() dependencies}.
   * @param id the {@link #getId() ID}.
   * @param compileDependency the compile {@link #getDependencies() dependency}.
   * @param testDependency the test {@link #getDependencies() dependency}
   * @param modelSupplier the {@link Supplier} for the maven {@link Model}.
   */
  public JavaSourceUsingMaven(JavaSourceProviderUsingMaven sourceProvider, String id, JavaSourceUsingMaven compileDependency,
      JavaSourceUsingMaven testDependency, Supplier<Model> modelSupplier) {

    this(sourceProvider, null, null, null, id, testDependency.getRootPackage(), modelSupplier, null, Arrays.asList(compileDependency, testDependency), null);
  }

  private JavaSourceUsingMaven(JavaSourceProviderUsingMaven sourceProvider, CodeSource reflectiveObject, File byteCodeLocation, File sourceCodeLocation,
      String id, BasePackage superLayerPackage, Supplier<Model> modelSupplier, JavaSource compileDependency, List<JavaSource> dependencies, String scope) {

    super(reflectiveObject, byteCodeLocation, sourceCodeLocation, id, null, dependencies, superLayerPackage);
    this.sourceProvider = sourceProvider;
    this.modelSupplier = modelSupplier;
    this.compileDependency = compileDependency;
    this.scope = scope;
  }

  /**
   * @return the maven {@link Model}.
   */
  public Model getModel() {

    if (this.model == null) {
      this.model = this.modelSupplier.get();
      if (this.model == null) {
        throw new ResourceMissingException("model");
      }
      this.modelSupplier = null;
    }
    return this.model;
  }

  /**
   * @return the optional scope. May be {@code null}.
   */
  public String getScope() {

    return this.scope;
  }

  @Override
  protected CodeSourceDescriptor createDescriptor() {

    Model mavenModel = getModel();
    String javadocUrl = null; // TODO
    return new BaseSourceDescriptorType(mavenModel.getGroupId(), mavenModel.getArtifactId(), mavenModel.getVersion(), this.scope, javadocUrl);
  }

  @Override
  protected JavaSourceDependencies createDependencies() {

    return new JavaSourceDependencies(this, this::getDependencyList);
  }

  @Override
  protected File createSourceCodeLocation() {

    File byteCodeLocation = getByteCodeLocation();
    if (byteCodeLocation == null) {
      return null; // actually inconsistent...
    }
    String filename = byteCodeLocation.getName();
    int lastDot = filename.lastIndexOf('.');
    if (lastDot == filename.length() - 4) { // regular extension such as ".jar"
      String basename = filename.substring(0, lastDot);
      String extension = filename.substring(lastDot);
      File sourceCodeLocation = new File(basename + "-" + CLASSIFIER_SOURCES + extension);
      if (sourceCodeLocation.exists()) {
        return sourceCodeLocation;
      }
    }
    if (byteCodeLocation.isDirectory()) {
      return ModelHelper.getSourceDirectory(getModel());
    }
    return null;
  }

  private List<JavaSource> getDependencyList() {

    List<Dependency> dependencies = getModel().getDependencies();
    List<JavaSource> sourceDependencies = new ArrayList<>(dependencies.size());
    if (this.compileDependency != null) {
      sourceDependencies.add(this.compileDependency);
    }
    boolean isTestScope = SCOPE_TEST.equals(this.scope);
    for (Dependency dependency : dependencies) {
      boolean isTestDependency = SCOPE_TEST.equals(dependency.getScope());
      if (isTestDependency == isTestScope) {
        JavaSource source = this.sourceProvider.create(dependency);
        sourceDependencies.add(source);
      }
    }
    return sourceDependencies;
  }

}
