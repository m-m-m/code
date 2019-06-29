/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.source.maven;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import net.sf.mmm.code.base.loader.BaseSourceCodeProviderArchive;
import net.sf.mmm.code.base.loader.BaseSourceCodeProviderDirectory;
import net.sf.mmm.code.base.loader.BaseSourceLoader;
import net.sf.mmm.code.base.loader.SourceCodeProvider;
import net.sf.mmm.code.base.loader.SourceCodeProviderProxy;
import net.sf.mmm.code.base.source.BaseSource;
import net.sf.mmm.code.base.source.BaseSourceHelper;
import net.sf.mmm.code.base.source.BaseSourceImpl;
import net.sf.mmm.code.base.source.BaseSourceProvider;
import net.sf.mmm.code.base.source.BaseSourceProviderImpl;
import net.sf.mmm.code.impl.java.JavaContext;
import net.sf.mmm.code.impl.java.JavaExtendedContext;
import net.sf.mmm.code.impl.java.JavaRootContext;
import net.sf.mmm.code.impl.java.loader.JavaSourceLoader;
import net.sf.mmm.code.java.maven.api.DependencyHelper;
import net.sf.mmm.code.java.maven.api.MavenBridge;
import net.sf.mmm.code.java.maven.api.MavenConstants;
import net.sf.mmm.code.java.maven.api.ModelHelper;
import net.sf.mmm.code.java.maven.impl.MavenBridgeImpl;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link BaseSourceProvider} using maven to read and extract metadata from POMs.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaSourceProviderUsingMaven extends BaseSourceProviderImpl implements MavenConstants {

  private MavenBridge mavenBridge;

  private static final Logger LOG = LoggerFactory.getLogger(JavaSourceProviderUsingMaven.class);

  /**
   * The constructor.
   */
  public JavaSourceProviderUsingMaven() {

    this(MavenBridgeImpl.getDefault());
  }

  /**
   * The constructor.
   *
   * @param mavenBridge the {@link MavenBridge} instance to use.
   */
  public JavaSourceProviderUsingMaven(MavenBridge mavenBridge) {

    super();
    this.mavenBridge = mavenBridge;
  }

  @Override
  public BaseSource create(CodeSource source) {

    Objects.requireNonNull(source, "source");
    File location = BaseSourceHelper.asFile(source.getLocation());
    Supplier<Model> supplier = createModelSupplier(location);
    SourceCodeProvider sourceCodeProvider = new SourceCodeProviderProxy(() -> createSourceCodeProvider(location, supplier));
    BaseSourceLoader loader = new JavaSourceLoader(sourceCodeProvider);
    return new JavaSourceUsingMaven(this, source, supplier, loader);
  }

  private SourceCodeProvider createSourceCodeProvider(File location, Supplier<Model> supplier) {

    File artifactSources = this.mavenBridge.findArtifactSources(location);
    if (artifactSources != null) {
      return new BaseSourceCodeProviderArchive(artifactSources);
    }
    if (location.isDirectory()) {
      Model model = supplier.get();
      if (model != null) {
        return new BaseSourceCodeProviderDirectory(ModelHelper.getSourceDirectory(model));
      }
    }
    return null;
  }

  @Override
  public BaseSource create(File byteCodeLocation, File sourceCodeLocation) {

    File location;
    if (byteCodeLocation != null) {
      location = byteCodeLocation;
    } else {
      location = sourceCodeLocation;
    }
    Supplier<Model> modelSupplier = createModelSupplier(location);
    return new JavaSourceUsingMaven(this, byteCodeLocation, sourceCodeLocation, modelSupplier, null, createLoader(sourceCodeLocation));
  }

  @SuppressWarnings("deprecation")
  private Supplier<Model> createModelSupplier(File location) {

    return new net.sf.mmm.code.impl.java.supplier.SupplierAdapter<>(() -> parseModel(location));
  }

  private static BaseSourceLoader createLoader(File sourceCodeLocation) {

    SourceCodeProvider sourceCodeProvider;
    if ((sourceCodeLocation == null) || !sourceCodeLocation.exists()) {
      sourceCodeProvider = null;
    } else if (sourceCodeLocation.isDirectory()) {
      sourceCodeProvider = new BaseSourceCodeProviderDirectory(sourceCodeLocation);
    } else {
      sourceCodeProvider = new BaseSourceCodeProviderArchive(sourceCodeLocation);
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

    try {
      URL reflectiveObjectURL = byteCodeArtifact.toURI().toURL();
      CodeSource dependencyCodeSource = new CodeSource(reflectiveObjectURL, (Certificate[]) null);

      return new JavaSourceUsingMaven(this, dependencyCodeSource, byteCodeArtifact, sourceCodeArtifact, () -> parseModel(byteCodeArtifact),
          dependency.getScope(), loader);

    } catch (MalformedURLException e) {
      LOG.error("Malformed URL of the byte code artifact");
    }

    return new JavaSourceUsingMaven(this, byteCodeArtifact, sourceCodeArtifact, () -> parseModel(byteCodeArtifact), dependency.getScope(),
        loader);
  }

  /**
   * @param parentContext the {@link JavaContext} to inherit and use as {@link JavaContext#getParent() parent}. Most
   *        likely {@link net.sf.mmm.code.impl.java.JavaRootContext#get()}.
   * @return the {@link BaseSourceImpl source} for the Maven project at the current {@link File} location (CWD).
   */
  public BaseSourceImpl createFromLocalMavenProject(JavaContext parentContext) {

    return createFromLocalMavenProject(parentContext, getCwd());
  }

  /**
   * @param parentContext the {@link JavaContext} to inherit and use as {@link JavaContext#getParent() parent}. Most
   *        likely {@link net.sf.mmm.code.impl.java.JavaRootContext#get()}.
   * @param location the {@link File} pointing to the Maven project.
   * @return the {@link BaseSourceImpl source} for the Maven project at the given {@link File} location.
   */
  public JavaSourceUsingMaven createFromLocalMavenProject(JavaContext parentContext, File location) {

    final Model model = parseModel(location);

    if (model == null) {
      throw new IllegalArgumentException("Could not find pom.xml for basedir: " + location);
    }
    Supplier<Model> modelSupplier = () -> model;
    File byteCodeLocation = ModelHelper.getOutputDirectory(model);
    File sourceCodeLocation = ModelHelper.getSourceDirectory(model);
    JavaSourceUsingMaven compileDependency = new JavaSourceUsingMaven(this, byteCodeLocation, sourceCodeLocation, modelSupplier,
        SCOPE_COMPILE, createLoader(sourceCodeLocation));
    File testByteCodeLocation = ModelHelper.getTestOutputDirectory(model);
    File testSourceCodeLocation = ModelHelper.getTestSourceDirectory(model);
    BaseSourceLoader testLoader = createLoader(testSourceCodeLocation);
    JavaSourceUsingMaven testDependency = new JavaSourceUsingMaven(this, compileDependency, testByteCodeLocation, testSourceCodeLocation,
        modelSupplier, testLoader);
    return testDependency;
  }

  /**
   * @return the {@link JavaContext} for the local Maven project in the current working directory.
   */
  public static JavaContext createFromLocalMavenProject() {

    return createFromLocalMavenProject(getCwd(), false);
  }

  /**
   * @param location the {@link File} pointing to the Maven project.
   * @return the {@link JavaContext} for the Maven project at the given {@code location}.
   */
  public static JavaContext createFromLocalMavenProject(File location) {

    return createFromLocalMavenProject(location, true);
  }

  /**
   * @param location the {@link File} pointing to the Maven project.
   * @param buildClasspath - {@code true} to build a custom {@link ClassLoader} for the maven project, {@code false} to
   *        use the existing {@link Thread#getContextClassLoader() CCL}.
   * @return the {@link JavaContext} for the Maven project at the given {@code location}.
   */
  private static JavaContext createFromLocalMavenProject(File location, boolean buildClasspath) {

    JavaSourceProviderUsingMaven provider = new JavaSourceProviderUsingMaven();
    JavaSourceUsingMaven source = provider.createFromLocalMavenProject(JavaRootContext.get(), location);

    if (buildClasspath) {
      File byteCodeLocation = getByteCodeLocation(source);
      Set<URL> dependenciesUrls = new HashSet<>();

      try {
        // First eclipse target because it is the most updated version of the classes
        dependenciesUrls.add(getEclipseByteCodeLocation(location));
        dependenciesUrls.add(byteCodeLocation.toURI().toURL());

      } catch (MalformedURLException e1) {
        LOG.debug("Not able to get the URL of " + byteCodeLocation.getName() + " or any of its dependencies.");
        return new JavaExtendedContext(source, provider, null);
      }
      // Now we need dependencies from the modules
      provider.collectModuleDependenciesUrls(location, dependenciesUrls);
      // Iterate over all dependencies, get URLS and construct MavenClassLoader
      provider.collectDependenciesUrls(source.getModel(), dependenciesUrls, 0);

      try {
        URL[] dependencies = new URL[dependenciesUrls.size()];
        dependencies = dependenciesUrls.toArray(dependencies);

        MavenClassLoader mvnClassLoader = new MavenClassLoader(Thread.currentThread().getContextClassLoader(), dependencies);

        return new JavaExtendedContext(source, provider, mvnClassLoader);
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    } else {
      return new JavaExtendedContext(source, provider, null);
    }
  }

  /**
   * Retrieves the URL where the eclipse-target folder should be located. It contains bytecode. This is useful for
   * devon4j projects, but will not harm other projects.
   *
   * @param projectLocation current location of the byte code
   * @return the URL with the location of eclipse-target folder
   * @throws MalformedURLException throws {@link MalformedURLException}
   */
  private static URL getEclipseByteCodeLocation(File projectLocation) throws MalformedURLException {

    Path moduleLocation = projectLocation.toPath().resolve("eclipse-target" + File.separator + "classes");

    return moduleLocation.toUri().toURL();
  }

  /**
   * Gets the byte code location, that is located on classes/ folder
   *
   * @param source where to get the byte code location from
   * @return File where the byte code is located
   */
  private static File getByteCodeLocation(JavaSourceUsingMaven source) {

    // Create a File object on the root directory of the classes
    File byteCodeLocation = null;
    String byteCodeLocationString = source.getByteCodeLocation().toString();
    if (byteCodeLocationString.substring(byteCodeLocationString.lastIndexOf(File.separator) + 1).equals("test-classes")) {
      byteCodeLocation = source.getByteCodeLocation().getParentFile().toPath().resolve("classes" + File.separator).toFile();
    } else {
      byteCodeLocation = source.getByteCodeLocation();
    }
    return byteCodeLocation;
  }

  /**
   * Recursively gets dependencies from the model, and stores them on a list.
   *
   * @param model parsed from the POM of a project
   * @param dependencyUrls {@link Set} where to add the dependency {@link URL}s.
   * @param recursiveness level of recursiveness. How deep we get into dependencies.
   */
  private void collectDependenciesUrls(Model model, Set<URL> dependencyUrls, int recursiveness) {

    if (recursiveness >= 4) {
      return;
    }
    recursiveness++;

    if (model == null) {
      return;
    }
    List<Dependency> dependencies = model.getDependencies();

    for (Dependency dependency : dependencies) {
      try {
        File artifact = this.mavenBridge.findArtifact(dependency);
        URL jarUrl = new URL(artifact.toURI().toString());

        if (dependencyUrls.contains(jarUrl)) {
          return;
        }

        dependencyUrls.add(jarUrl);

        File artifactPom = this.mavenBridge.findPom(dependency);
        Model artifactModel = this.mavenBridge.readEffectiveModel(artifactPom);

        collectDependenciesUrls(artifactModel, dependencyUrls, recursiveness);

      } catch (MalformedURLException e) {
        LOG.warn("Problem when getting dependency URL " + dependency.getArtifactId() + " from the current project", e);
      } catch (IllegalStateException e) {
        LOG.warn("Problem when reading pom of dependency " + dependency.getArtifactId() + ". Not adding it to the classpath.");
      }
    }
  }

  /**
   * Tries to find the parent POM of this project in order to retrieve the modules defined-
   *
   * @param location current project, in which we want to check if it has a parent POM with modules
   * @param dependenciesUrls {@link Set} where to add the collected dependency {@link URL}s.
   */
  private void collectModuleDependenciesUrls(File location, Set<URL> dependenciesUrls) {

    Model model = parseModel(location);

    if (model == null) {
      return;
    }

    Parent parent = model.getParent();

    try {
      while (parent != null) {
        File parentPom = location.toPath().resolve(parent.getRelativePath()).toFile().getCanonicalFile();
        Model parentModel = parseModel(parentPom);

        if (parentModel == null) {
          break;
        }

        for (String module : parentModel.getModules()) {
          String[] segments = getPathSegmentsFromFile(parentPom);
          if (segments.length - 1 == 0) {
            break;
          }

          segments[segments.length - 1] = module;

          Path moduleLocation = Paths.get("", segments);

          Model moduleModel = parseModel(moduleLocation.toFile());

          // We add eclipse-target URL
          URL eclipseTargetUrl = getEclipseByteCodeLocation(moduleLocation.toFile());
          dependenciesUrls.add(eclipseTargetUrl);
          collectDependenciesUrls(moduleModel, dependenciesUrls, 1);
        }

        collectDependenciesUrls(parentModel, dependenciesUrls, 1);

        model = parentModel;
        parent = model.getParent();
        location = parentPom.getParentFile();
      }
    } catch (IOException e) {
      throw new IllegalStateException("I/O error whilst reading POMs and collecting dependencies.", e);
    }
  }

  private static File getCwd() {

    return new File(".").getAbsoluteFile().getParentFile();
  }

  /**
   * Useful for segmenting a path into an array. The path gets split by its path separator
   *
   * @param byteCodeLocation the file which you want to get its path segmented by the path separator
   * @return an array containing in each element one segment of the path
   */
  private static String[] getPathSegmentsFromFile(File file) {

    ArrayList<String> nameElements = new ArrayList<>();
    Path path = file.toPath();

    nameElements.add(path.getRoot().toFile().toString());

    while (path.getParent() != null) {
      nameElements.add(1, path.toFile().getName());
      path = path.getParent();
    }

    return nameElements.toArray(new String[0]);
  }

}
