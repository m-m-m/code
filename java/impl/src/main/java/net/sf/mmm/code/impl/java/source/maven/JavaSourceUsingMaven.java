/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.source.maven;

import java.io.File;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import net.sf.mmm.code.api.source.CodeSourceDescriptor;
import net.sf.mmm.code.base.BaseContext;
import net.sf.mmm.code.base.loader.BaseSourceLoader;
import net.sf.mmm.code.base.source.BaseSource;
import net.sf.mmm.code.base.source.BaseSourceDependencies;
import net.sf.mmm.code.base.source.BaseSourceDescriptorType;
import net.sf.mmm.code.base.source.BaseSourceImpl;
import net.sf.mmm.code.impl.java.JavaContext;
import net.sf.mmm.code.java.maven.api.MavenConstants;
import net.sf.mmm.code.java.maven.api.ModelHelper;
import net.sf.mmm.util.component.api.ResourceMissingException;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;

/**
 * Extends {@link BaseSourceImpl} to provide {@link #getDependencies() dependencies} and {@link #getDescriptor()
 * descriptor} from maven POMs.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaSourceUsingMaven extends BaseSourceImpl implements MavenConstants {

  private final JavaSourceProviderUsingMaven sourceProvider;

  private BaseSource compileDependency;

  private Supplier<Model> modelSupplier;

  private Model model;

  private String scope;

  /**
   * The constructor.
   *
   * @param sourceProvider the {@link JavaSourceProviderUsingMaven maven source provider} required for lazy fabrication
   *        of {@link #getDependencies() dependencies}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May not be {@code null}.
   * @param modelSupplier the {@link Supplier} for the maven {@link Model}.
   * @param sourceLoader the {@link BaseSourceLoader}.
   */
  public JavaSourceUsingMaven(JavaSourceProviderUsingMaven sourceProvider, CodeSource reflectiveObject,
      Supplier<Model> modelSupplier, BaseSourceLoader sourceLoader) {

    this(sourceProvider, reflectiveObject, null, null, null, modelSupplier, null, null, null, sourceLoader, true);
  }

  /**
   * The constructor for test-source (e.g. "{@code src/test/java}").
   *
   * @param sourceProvider the {@link JavaSourceProviderUsingMaven maven source provider} required for lazy fabrication
   *        of {@link #getDependencies() dependencies}.
   * @param compileDependency the {@link BaseSource} for the compile dependencies (representing {@code src/main/java}
   *        with compile dependencies as {@link #getDependencies() dependencies}.
   * @param byteCodeLocation the {@link #getByteCodeLocation() byte code location}.
   * @param sourceCodeLocation the {@link #getSourceCodeLocation() source code location}.
   * @param modelSupplier the {@link Supplier} for the maven {@link Model}.
   * @param sourceLoader the {@link BaseSourceLoader}.
   */
  public JavaSourceUsingMaven(JavaSourceProviderUsingMaven sourceProvider, BaseSource compileDependency,
      File byteCodeLocation, File sourceCodeLocation, Supplier<Model> modelSupplier, BaseSourceLoader sourceLoader) {

    this(sourceProvider, null, byteCodeLocation, sourceCodeLocation, null, modelSupplier, compileDependency, null,
        SCOPE_TEST, sourceLoader, true);
  }

  /**
   * The constructor.
   *
   * @param sourceProvider the {@link JavaSourceProviderUsingMaven maven source provider} required for lazy fabrication
   *        of {@link #getDependencies() dependencies}.
   * @param byteCodeLocation the {@link #getByteCodeLocation() byte code location}.
   * @param sourceCodeLocation the {@link #getSourceCodeLocation() source code location}.
   * @param modelSupplier the {@link Supplier} for the maven {@link Model}.
   * @param scope the {@link #getScope() scope}.
   * @param sourceLoader the {@link BaseSourceLoader}.
   */
  public JavaSourceUsingMaven(JavaSourceProviderUsingMaven sourceProvider, File byteCodeLocation,
      File sourceCodeLocation, Supplier<Model> modelSupplier, String scope, BaseSourceLoader sourceLoader) {

    this(sourceProvider, null, byteCodeLocation, sourceCodeLocation, null, modelSupplier, null, null, scope,
        sourceLoader, true);
  }

  /**
   * The constructor.
   *
   * @param sourceProvider the {@link JavaSourceProviderUsingMaven maven source provider} required for lazy fabrication
   *        of {@link #getDependencies() dependencies}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May be {@code null}.
   * @param byteCodeLocation the {@link #getByteCodeLocation() byte code location}.
   * @param sourceCodeLocation the {@link #getSourceCodeLocation() source code location}.
   * @param modelSupplier the {@link Supplier} for the maven {@link Model}.
   * @param scope the {@link #getScope() scope}.
   * @param sourceLoader the {@link BaseSourceLoader}.
   */
  public JavaSourceUsingMaven(JavaSourceProviderUsingMaven sourceProvider, CodeSource reflectiveObject,
      File byteCodeLocation, File sourceCodeLocation, Supplier<Model> modelSupplier, String scope,
      BaseSourceLoader sourceLoader) {

    this(sourceProvider, reflectiveObject, byteCodeLocation, sourceCodeLocation, null, modelSupplier, null, null, scope,
        sourceLoader, true);
  }

  /**
   * The constructor for a local maven project.
   *
   * @see JavaSourceProviderUsingMaven#createFromLocalMavenProject(JavaContext, File)
   *
   * @param sourceProvider the {@link JavaSourceProviderUsingMaven maven source provider} required for lazy fabrication
   *        of {@link #getDependencies() dependencies}.
   * @param id the {@link #getId() ID}.
   * @param compileDependency the compile {@link #getDependencies() dependency}.
   * @param testDependency the test {@link #getDependencies() dependency}
   * @param modelSupplier the {@link Supplier} for the maven {@link Model}.
   * @param sourceLoader the {@link BaseSourceLoader}.
   * @param immutable the {@link #isImmutable() immutable} flag.
   */
  public JavaSourceUsingMaven(JavaSourceProviderUsingMaven sourceProvider, String id,
      JavaSourceUsingMaven compileDependency, JavaSourceUsingMaven testDependency, Supplier<Model> modelSupplier,
      BaseSourceLoader sourceLoader, boolean immutable) {

    this(sourceProvider, null, null, null, id, modelSupplier, null, Arrays.asList(compileDependency, testDependency),
        null, sourceLoader, immutable);
  }

  private JavaSourceUsingMaven(JavaSourceProviderUsingMaven sourceProvider, CodeSource reflectiveObject,
      File byteCodeLocation, File sourceCodeLocation, String id, Supplier<Model> modelSupplier,
      BaseSource compileDependency, List<BaseSource> dependencies, String scope, BaseSourceLoader sourceLoader,
      boolean immutable) {

    super(reflectiveObject, byteCodeLocation, sourceCodeLocation, id, null, dependencies, sourceLoader, immutable);
    this.sourceProvider = sourceProvider;
    this.modelSupplier = modelSupplier;
    this.compileDependency = compileDependency;
    this.scope = scope;
  }

  @Override
  public BaseContext getContext() {

    BaseContext context = super.getContext();
    if (context == null) {
      context = this.sourceProvider.getContext();
      setContext(context);
    }
    return context;
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
    return new BaseSourceDescriptorType(mavenModel.getGroupId(), mavenModel.getArtifactId(), mavenModel.getVersion(),
        this.scope, javadocUrl);
  }

  @Override
  protected BaseSourceDependencies createDependencies() {

    return new BaseSourceDependencies(this, this::getDependencyList);
  }

  @Override
  protected File createSourceCodeLocation() {

    File byteCodeLocation = getByteCodeLocation();
    if (byteCodeLocation == null) {
      return null; // actually inconsistent...
    }
    String filename = byteCodeLocation.getName();
    int lastDot = filename.lastIndexOf('.');
    if (lastDot == filename.length() - 4) { // TODO regular extension such as ".jar"
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

  private List<BaseSource> getDependencyList() {

    List<Dependency> dependencies = getModel().getDependencies();
    List<BaseSource> sourceDependencies = new ArrayList<>(dependencies.size());
    if (this.compileDependency != null) {
      sourceDependencies.add(this.compileDependency);
    }
    boolean isTestScope = SCOPE_TEST.equals(this.scope);
    for (Dependency dependency : dependencies) {
      boolean isTestDependency = SCOPE_TEST.equals(dependency.getScope());
      if (isTestDependency == isTestScope) {
        BaseSource source = this.sourceProvider.createSource(dependency);
        sourceDependencies.add(source);
      }
    }
    return sourceDependencies;
  }

}
