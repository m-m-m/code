package net.sf.mmm.code.impl.java.source.maven;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.mmm.code.java.maven.api.DependencyHelper;
import net.sf.mmm.code.java.maven.api.MavenBridge;
import net.sf.mmm.code.java.maven.api.MavenConstants;
import net.sf.mmm.code.java.maven.api.ModelHelper;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to collect {@link Dependency dependencies} of a maven project.
 *
 * @since 1.0.0
 * @see #collectWithModules(Model)
 * @see #asUrls()
 */
public class MavenDependencyCollector {

  private static final Logger LOG = LoggerFactory.getLogger(MavenDependencyCollector.class);

  private final MavenBridge mavenBridge;

  private final List<URL> dependencyList;

  private final Set<URL> dependencySet;

  private final String maxScope;

  private final String altBuildDir;

  /**
   * The constructor.
   *
   * @param mavenBridge the {@link MavenBridge} instance to use.
   * @param maxScope the maximum {@link Dependency#getScope() scope} of the {@link Dependency dependencies} to collect.
   * @param altBuildDir the alternative build directory (e.g. "eclipse-target").
   */
  public MavenDependencyCollector(MavenBridge mavenBridge, String maxScope, String altBuildDir) {

    super();
    this.mavenBridge = mavenBridge;
    this.dependencyList = new ArrayList<>();
    this.dependencySet = new HashSet<>();
    this.maxScope = maxScope;
    this.altBuildDir = altBuildDir;
  }

  private boolean add(File dependencyFile) {

    if (!dependencyFile.exists()) {
      return false;
    }
    try {
      return add(dependencyFile.toURI().toURL());
    } catch (MalformedURLException e) {
      throw new IllegalStateException("Failed to convert file to URL: " + dependencyFile, e);
    }
  }

  private boolean add(URL dependencyUrl) {

    boolean added = this.dependencySet.add(dependencyUrl);
    if (added) {
      this.dependencyList.add(dependencyUrl);
    }
    return added;
  }

  /**
   * @return the collected dependencies as {@link URL}s.
   */
  public URL[] asUrls() {

    URL[] dependencies = new URL[this.dependencyList.size()];
    return this.dependencyList.toArray(dependencies);
  }

  /**
   * @return a {@link ClassLoader} for the collected dependencies.
   */
  public ClassLoader asClassLoader() {

    return asClassLoader(ClassLoader.getSystemClassLoader().getParent());
  }

  /**
   * @param parent the parent {@link ClassLoader}.
   * @return a {@link ClassLoader} for the collected dependencies.
   */
  public ClassLoader asClassLoader(ClassLoader parent) {

    return new MavenClassLoader(parent, asUrls());
  }

  private Model parseModel(File location) {

    File pomFile = this.mavenBridge.findPom(location);
    if ((pomFile == null) || !pomFile.isFile()) {
      return null;
    }
    return this.mavenBridge.readEffectiveModel(pomFile);
  }

  /**
   * @param model the {@link Model} of the local maven project for which to collect all the dependencies including all
   *        its sibling modules by recursively traversing the parent POMs and collecting the modules.
   */
  public void collectWithModules(Model model) {

    File location = model.getPomFile().getParentFile();
    collect(model);
    File cwd = model.getPomFile();
    Parent parent = model.getParent();

    try {
      while (parent != null) {
        File parentPom = new File(cwd, parent.getRelativePath());
        Model parentModel = parseModel(parentPom);

        if (parentModel == null) {
          break;
        }
        collect(parentModel, 0);

        cwd = parentPom.getParentFile().getCanonicalFile();
        for (String module : parentModel.getModules()) {
          File moduleLocation = new File(cwd, module).getCanonicalFile();
          if (!moduleLocation.equals(location)) {
            Model moduleModel = parseModel(moduleLocation);
            collect(moduleModel);
          }
        }

        model = parentModel;
        parent = model.getParent();
      }
    } catch (IOException e) {
      throw new IllegalStateException("I/O error whilst reading POMs and collecting dependencies.", e);
    }
  }

  /**
   * Resolves and collects the dependencies of a local maven project.
   *
   * @param model parsed from the POM of a project
   */
  public void collect(Model model) {

    File outputDirectory = ModelHelper.getOutputDirectory(model);
    addOutputDirectory(outputDirectory, MavenConstants.DEFAULT_OUTPUT_FOLDER);
    if (MavenConstants.SCOPE_TEST.equals(this.maxScope)) {
      File testOutputDirectory = ModelHelper.getTestOutputDirectory(model);
      addOutputDirectory(testOutputDirectory, MavenConstants.DEFAULT_TEST_OUTPUT_FOLDER);
    }
    collect(model, 0);
  }

  private void addOutputDirectory(File outputDirectory, String defaultOutputFolder) {

    if (this.altBuildDir != null) {
      File buildDirectory = outputDirectory.getParentFile();
      File projectDirectory = outputDirectory.getParentFile();
      String name = buildDirectory.getName();
      if (name.equals(MavenConstants.DEFAULT_BUILD_DIRECTORY)) {
        File altBuildDirectory = new File(projectDirectory, this.altBuildDir);
        File altOutputDirectory = new File(altBuildDirectory, defaultOutputFolder);
        add(altOutputDirectory);
      } else if (name.equals(this.altBuildDir)) {
        add(outputDirectory);
        buildDirectory = new File(projectDirectory, MavenConstants.DEFAULT_BUILD_DIRECTORY);
        outputDirectory = new File(buildDirectory, defaultOutputFolder);
      }
    }
    add(outputDirectory);
  }

  /**
   * Recursively gets dependencies from the model, and stores them on a list.
   *
   * @param model {@link Model} parsed from the POM of a project.
   * @param recursiveness level of recursiveness. How deep we get into dependencies.
   */
  private void collect(Model model, int recursiveness) {

    if (model == null) {
      return;
    }
    if (recursiveness >= 100) {
      LOG.warn("Skipping transitive dependency at {} after 100 recursions!", model);
      return;
    }
    recursiveness++;
    List<Dependency> dependencies = model.getDependencies();

    for (Dependency dependency : dependencies) {
      if (DependencyHelper.hasScopeIncludedIn(dependency, this.maxScope)) {
        File artifact = this.mavenBridge.findArtifact(dependency);
        boolean added = add(artifact);
        if (!added) {
          return;
        }
        LOG.debug("Collected dependency {}", artifact);
        File artifactPom = this.mavenBridge.findPom(dependency);
        Model artifactModel = this.mavenBridge.readEffectiveModel(artifactPom);
        collect(artifactModel, recursiveness);
      }
    }
  }

}
