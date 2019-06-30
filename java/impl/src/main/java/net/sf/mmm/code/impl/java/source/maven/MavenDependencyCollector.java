package net.sf.mmm.code.impl.java.source.maven;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
 * Class to collect {@link Dependency dependencies} of a maven project.
 *
 * @since 1.0.0
 * @see #collect(Model)
 * @see #asUrls()
 * @see #asClassLoader()
 */
public class MavenDependencyCollector {

  private static final Logger LOG = LoggerFactory.getLogger(MavenDependencyCollector.class);

  final MavenBridge mavenBridge;

  private final List<URL> dependencyList;

  private final Set<URL> dependencySet;

  private final Map<String, Model> gav2ProjectMap;

  private final Map<String, Model> dir2ProjectMap;

  private final boolean buildReactor;

  private final boolean includeTestDependencies;

  private final String altBuildDir;

  /**
   * The constructor.
   *
   * @param includeTestDependencies - {@code true} to include test-dependencies, {@code false} otherwise.
   * @param buildReactor - {@code true} to build the maven project reactor by traversing all parents and modules to
   *        resolve dependencies to sibling modules (slower but more accurate), {@code false} otherwise.
   * @param altBuildDir the alternative build directory (e.g. "eclipse-target").
   */
  public MavenDependencyCollector(boolean includeTestDependencies, boolean buildReactor, String altBuildDir) {

    this(MavenBridgeImpl.getDefault(), includeTestDependencies, buildReactor, altBuildDir);
  }

  /**
   * The constructor.
   *
   * @param mavenBridge the {@link MavenBridge} instance to use.
   * @param includeTestDependencies - {@code true} to include test-dependencies, {@code false} otherwise.
   * @param buildReactor - {@code true} to build the maven project reactor by traversing all parents and modules to
   *        resolve dependencies to sibling modules (slower but more accurate), {@code false} otherwise.
   * @param altBuildDir the alternative build directory (e.g. "eclipse-target").
   */
  public MavenDependencyCollector(MavenBridge mavenBridge, boolean includeTestDependencies, boolean buildReactor, String altBuildDir) {

    super();
    this.mavenBridge = mavenBridge;
    this.dependencyList = new ArrayList<>();
    this.dependencySet = new HashSet<>();
    this.buildReactor = buildReactor;
    if (buildReactor) {
      this.gav2ProjectMap = new HashMap<>();
      this.dir2ProjectMap = new HashMap<>();
    } else {
      this.gav2ProjectMap = null;
      this.dir2ProjectMap = null;
    }
    this.includeTestDependencies = includeTestDependencies;
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
      LOG.debug("Adding dependency " + dependencyUrl);
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

  private void collectWithReactor(Model model, boolean addDependencies) {

    if (model == null) {
      return;
    }
    String gav = ModelHelper.getGav(model);
    Model duplicate = this.gav2ProjectMap.put(gav, model);
    if (duplicate != null) {
      LOG.warn("Duplicate reactor project for GAV " + gav + ": " + duplicate + " replaced with " + model);
    }
    File modelBasedir = normalize(model.getPomFile().getParentFile());
    duplicate = this.dir2ProjectMap.put(modelBasedir.getPath(), model);
    if (duplicate != null) {
      LOG.warn("Duplicate reactor project for basedir " + modelBasedir + ": " + duplicate + " replaced with " + model);
    }
    Parent parent = model.getParent();
    if (parent != null) {
      String parentGav = ModelHelper.getGav(parent);
      if (this.gav2ProjectMap.containsKey(parentGav)) {
        LOG.debug("Already visited parent project {}", parentGav);
      } else {
        File parentPom = normalize(new File(modelBasedir, parent.getRelativePath()));
        Model parentModel = parseModel(parentPom);
        collectWithReactor(parentModel, addDependencies);
      }
    }
    for (String module : model.getModules()) {
      File moduleLocation = normalize(new File(modelBasedir, module));
      File moduleBasedir = moduleLocation;
      if (moduleLocation.isFile()) {
        moduleBasedir = moduleLocation.getParentFile();
      }
      String modulePath = moduleBasedir.getPath();
      if (this.dir2ProjectMap.containsKey(modulePath)) {
        LOG.debug("Already visited module project {}", modulePath);
      } else {
        Model moduleModel = parseModel(moduleLocation);
        collectWithReactor(moduleModel, false);
      }
    }
    if (addDependencies) {
      addOutputDirectories(model);
      collect(model, this.includeTestDependencies, 0);
    }
  }

  /**
   * Resolves and collects the dependencies of a local maven project.
   *
   * @param model parsed from the POM of a project
   */
  public void collect(Model model) {

    if (this.buildReactor) {
      collectWithReactor(model, true);
    } else {
      addOutputDirectories(model);
      collect(model, this.includeTestDependencies, 0);
    }
  }

  private void addOutputDirectories(Model model) {

    File outputDirectory = ModelHelper.getOutputDirectory(model);
    addOutputDirectory(outputDirectory, MavenConstants.DEFAULT_OUTPUT_FOLDER);
    if (this.includeTestDependencies) {
      File testOutputDirectory = ModelHelper.getTestOutputDirectory(model);
      addOutputDirectory(testOutputDirectory, MavenConstants.DEFAULT_TEST_OUTPUT_FOLDER);
    }
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
  private void collect(Model model, boolean includeTest, int recursiveness) {

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
      boolean isTestDependency = MavenConstants.SCOPE_TEST.equals(dependency.getScope());
      if (includeTest || !isTestDependency) {
        if ((recursiveness == 0) || !dependency.isOptional()) {
          Model artifactModel = null;
          if (this.buildReactor) {
            String gav = DependencyHelper.getGav(dependency);
            artifactModel = this.gav2ProjectMap.get(gav);
          }
          if (artifactModel == null) {
            File artifact = this.mavenBridge.findArtifact(dependency);
            boolean added = add(artifact);
            if (!added) {
              return;
            }
            File artifactPom = this.mavenBridge.findPom(dependency);
            artifactModel = this.mavenBridge.readEffectiveModel(artifactPom);
          } else {
            addOutputDirectories(artifactModel);
          }
          collect(artifactModel, isTestDependency, recursiveness);
        }
      } else {
        LOG.debug("Omitting optional dependency " + dependency);
      }
    }
  }

  static File normalize(File file) {

    try {
      file = file.getAbsoluteFile();
      return file.getCanonicalFile();
    } catch (IOException e) {
      LOG.debug("Failed to normize file {}", file, e);
      return file;
    }
  }

}
