/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.source.maven;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link BaseSourceProvider} using maven to read and extract metadata from POMs.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaSourceProviderUsingMaven extends BaseSourceProviderImpl implements MavenConstants {

    private final MavenBridge mavenBridge;

    private static final Logger LOG = LoggerFactory.getLogger(JavaSourceProviderUsingMaven.class);

    /**
     * The constructor.
     */
    public JavaSourceProviderUsingMaven() {

        super();
        mavenBridge = new MavenBridgeImpl();
    }

    @Override
    public BaseSource create(CodeSource source) {

        Objects.requireNonNull(source, "source");
        File location = BaseSourceHelper.asFile(source.getLocation());
        Supplier<Model> supplier = createModelSupplier(location);
        SourceCodeProvider sourceCodeProvider =
            new SourceCodeProviderProxy(() -> createSourceCodeProvider(location, supplier));
        BaseSourceLoader loader = new JavaSourceLoader(sourceCodeProvider);
        return new JavaSourceUsingMaven(this, source, supplier, loader);
    }

    private SourceCodeProvider createSourceCodeProvider(File location, Supplier<Model> supplier) {

        File artifactSources = mavenBridge.findArtifactSources(location);
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
        return new JavaSourceUsingMaven(this, byteCodeLocation, sourceCodeLocation, modelSupplier, null,
            createLoader(sourceCodeLocation));
    }

    @SuppressWarnings("deprecation")
    private Supplier<Model> createModelSupplier(File location) {

        return new net.sf.mmm.code.impl.java.supplier.SupplierAdapter<>(() -> parseModel(location));
    }

    private BaseSourceLoader createLoader(File sourceCodeLocation) {

        SourceCodeProvider sourceCodeProvider;
        if (sourceCodeLocation == null) {
            sourceCodeProvider = null;
        } else if (sourceCodeLocation.isDirectory()) {
            sourceCodeProvider = new BaseSourceCodeProviderDirectory(sourceCodeLocation);
        } else {
            sourceCodeProvider = new BaseSourceCodeProviderArchive(sourceCodeLocation);
        }
        return new JavaSourceLoader(sourceCodeProvider);
    }

    private Model parseModel(File location) {

        File pomFile = mavenBridge.findPom(location);
        if ((pomFile == null) || !pomFile.isFile()) {
            return null;
        }
        return mavenBridge.readEffectiveModel(pomFile);
    }

    BaseSource createSource(Dependency dependency) {

        File byteCodeArtifact = mavenBridge.findArtifact(dependency);
        JavaContext context = (JavaContext) getContext();
        String id = BaseSourceImpl.getNormalizedId(byteCodeArtifact);
        return context.getOrCreateSource(id, () -> createSource(dependency, byteCodeArtifact));
    }

    private BaseSource createSource(Dependency dependency, File byteCodeArtifact) {

        File sourceCodeArtifact = null;
        Dependency sourceDependency = DependencyHelper.createSource(dependency);
        if (sourceDependency != null) {
            sourceCodeArtifact = mavenBridge.findArtifact(sourceDependency);
            sourceCodeArtifact = BaseSourceHelper.getFileOrNull(sourceCodeArtifact);
        }
        BaseSourceLoader loader = createLoader(sourceCodeArtifact);
        return new JavaSourceUsingMaven(this, byteCodeArtifact, sourceCodeArtifact, () -> parseModel(byteCodeArtifact),
            dependency.getScope(), loader);
    }

    /**
     * @param parentContext
     *            the {@link JavaContext} to inherit and use as {@link JavaContext#getParent() parent}. Most
     *            likely {@link net.sf.mmm.code.impl.java.JavaRootContext#get()}.
     * @return the {@link BaseSourceImpl source} for the Maven project at the current {@link File} location
     *         (CWD).
     */
    public BaseSourceImpl createFromLocalMavenProject(JavaContext parentContext) {

        return createFromLocalMavenProject(parentContext, getCwd());
    }

    /**
     * @param parentContext
     *            the {@link JavaContext} to inherit and use as {@link JavaContext#getParent() parent}. Most
     *            likely {@link net.sf.mmm.code.impl.java.JavaRootContext#get()}.
     * @param location
     *            the {@link File} pointing to the Maven project.
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
        JavaSourceUsingMaven compileDependency = new JavaSourceUsingMaven(this, byteCodeLocation, sourceCodeLocation,
            modelSupplier, SCOPE_COMPILE, createLoader(sourceCodeLocation));
        File testByteCodeLocation = ModelHelper.getTestOutputDirectory(model);
        File testSourceCodeLocation = ModelHelper.getTestSourceDirectory(model);
        BaseSourceLoader testLoader = createLoader(testSourceCodeLocation);
        JavaSourceUsingMaven testDependency = new JavaSourceUsingMaven(this, compileDependency, testByteCodeLocation,
            testSourceCodeLocation, modelSupplier, testLoader);
        return testDependency;
    }

    /**
     * @return the {@link JavaContext} for the local Maven project in the current working directory.
     */
    public JavaContext createFromLocalMavenProject() {

        return createFromLocalMavenProject(getCwd(), false);
    }

    /**
     * @param location
     *            the {@link File} pointing to the Maven project.
     * @return the {@link JavaContext} for the Maven project at the given {@code location}.
     */
    public JavaContext createFromLocalMavenProject(File location) {

        return createFromLocalMavenProject(location, true);
    }

    /**
     * @param location
     *            the {@link File} pointing to the Maven project.
     * @return the {@link JavaContext} for the Maven project at the given {@code location}.
     */
    private JavaContext createFromLocalMavenProject(File location, Boolean isExternal) {

        JavaSourceProviderUsingMaven provider = new JavaSourceProviderUsingMaven();
        JavaSourceUsingMaven source = provider.createFromLocalMavenProject(JavaRootContext.get(), location);

        if (isExternal) {
            File byteCodeLocation = getByteCodeLocation(source);
            List<URL> dependenciesURLs = new ArrayList<>();

            // Iterate over all dependencies, get URLS and construct MavenClassLoader
            try {
                dependenciesURLs.add(byteCodeLocation.toURI().toURL());
            } catch (MalformedURLException e1) {
                LOG.debug("Not able to get the URL of " + byteCodeLocation.getName() + ".");
                return new JavaExtendedContext(source, provider, null);
            }
            dependenciesURLs.addAll(getDependenciesURLS(source.getModel()));

            try {
                URL[] dependencies = new URL[dependenciesURLs.size()];
                dependencies = dependenciesURLs.toArray(dependencies);

                MavenClassLoader mvnClassLoader =
                    new MavenClassLoader(Thread.currentThread().getContextClassLoader(), dependencies);

                return new JavaExtendedContext(source, provider, mvnClassLoader);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        } else {
            return new JavaExtendedContext(source, provider, null);
        }
    }

    /**
     * @param source
     * @return
     */
    private File getByteCodeLocation(JavaSourceUsingMaven source) {
        // Create a File object on the root directory of the classes
        File byteCodeLocation = null;
        String byteCodeLocationString = source.getByteCodeLocation().toString();
        if (byteCodeLocationString.substring(byteCodeLocationString.lastIndexOf(File.separator) + 1)
            .equals("test-classes")) {
            byteCodeLocation =
                source.getByteCodeLocation().getParentFile().toPath().resolve("classes" + File.separator).toFile();
        } else {
            byteCodeLocation = source.getByteCodeLocation();
        }
        return byteCodeLocation;
    }

    private ArrayList<URL> getDependenciesURLS(Model model) {
        List<Dependency> dependencies = model.getDependencies();
        ArrayList<URL> dependenciesURLS = new ArrayList<>();

        for (Dependency dependency : dependencies) {
            try {
                File artifact = mavenBridge.findArtifact(dependency);
                URL jarUrl = new URL(artifact.toURI().toString());

                dependenciesURLS.add(jarUrl);
            } catch (MalformedURLException e) {
                LOG.debug(
                    "Problem when getting dependency URL " + dependency.getArtifactId() + " from the current project",
                    e);
                continue;
            }
        }
        return dependenciesURLS;
    }

    private static File getCwd() {

        return new File(".").getAbsoluteFile().getParentFile();
    }

}
