/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Named;

import net.sf.mmm.code.api.member.CodeFields;
import net.sf.mmm.code.api.member.CodeMethod;
import net.sf.mmm.code.api.source.CodeSource;
import net.sf.mmm.code.api.source.CodeSourceDescriptor;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.impl.java.source.maven.JavaSourceProviderUsingMaven;
import net.sf.mmm.code.impl.java.source.maven.MavenClassLoader;

import org.junit.Test;

/**
 * Test of {@link JavaExtendedContext} using {@link JavaSourceProviderUsingMaven}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaExtendedContextWithMavenAndSourceCodeTest extends AbstractBaseTypeTest {

    private static final Pattern VERSION_PATTERN = Pattern.compile("[0-9]+\\.[0-9]+\\.[0-9]+(-SNAPSHOT)?");

    /**
     * Root Path where to test data is stored
     */
    private static final Path rootTestPath = new File("src/test/resources/testdata/").toPath();

    /**
     * Get context from current project
     *
     * @return the {@link JavaContext} of the current project
     */
    private JavaContext getContext() {

        return new JavaSourceProviderUsingMaven().createFromLocalMavenProject();
    }

    /**
     * Get context from a local Maven project
     *
     * @return the {@link JavaContext} of the local Maven project
     */
    private JavaContext getContextFromLocalMavenProject(File projectLocation) {

        return new JavaSourceProviderUsingMaven().createFromLocalMavenProject(projectLocation);
    }

    /**
     * Test the very basic methods of {@link JavaContext}.
     */
    @Test
    public void testBasics() {

        // given
        JavaContext context = getContext();
        // when
        CodeSource source = context.getSource();
        // then
        verifyDependency(source, "target/test-classes", "src/test/java", "test");
        assertThat(source.getId()).contains("/code/java/impl");
        assertThat(source.getSource()).isSameAs(source);

        List<? extends CodeSource> dependencies = source.getDependencies().getDeclared();
        assertThat(dependencies).hasSize(2);
        CodeSource compileDependency = dependencies.get(0);
        verifyDependency(compileDependency, "target/classes", "src/main/java", "compile");
        assertThat(source.getParent()).isSameAs(compileDependency);
        CodeSource testDependency = dependencies.get(1);
        verifyDescriptor(testDependency.getDescriptor(), "test", "mmm-util-test");
    }

    /**
     * Testing the retrieval of the context (including class loaders) from a local Maven project. Also tests
     * whether we are able to retrieve a class from this local Maven project
     *
     * @throws ClassNotFoundException
     *             throws {@link ClassNotFoundException} when the class from the local Maven project has not
     *             been found
     */
    @Test
    public void testContextLoadingFromLocalMavenProject() throws ClassNotFoundException {

        // given
        String entityClass = "com.maven.project.sampledatamanagement.dataaccess.api.SampleDataEntity";

        // Local Maven project we want to test
        File mavenProjectDirectory = rootTestPath.resolve("localmavenproject/maven.project/core").toFile();
        JavaContext context = getContextFromLocalMavenProject(mavenProjectDirectory);

        CodeType type = context.getType(entityClass);
        assertThat(type.getDoc().getLines()).containsExactly("This is the JavaDoc of {@link SampleDataEntity}.");
        assertThat(type.getDoc().getSourceCode())
            .isEqualTo("/** This is the JavaDoc of {@link SampleDataEntity}. */\n");
        CodeSource source = type.getSource();
        assertThat(source.getByteCodeLocation().getName()).isEqualTo("classes");

        CodeFields fields = type.getFields();

        // assert
        assertThat(fields.getDeclared("name")).isNotNull();
        assertThat(fields.getDeclared("mail")).isNotNull();
        assertThat(fields.getDeclared("surname")).isNotNull();
        assertThat(fields.getDeclared("age")).isNotNull();

        Class<?> classFile =
            context.getClassLoader().loadClass("com.devonfw.module.basic.common.api.entity.GenericEntity");
        assertThat(classFile).isNotNull();

        MavenClassLoader classLoader = (MavenClassLoader) context.getClassLoader();

        List<? extends CodeSource> dependencies = source.getDependencies().getDeclared();
        assertThat(dependencies).hasSize(2);

        type = context.getType("javax.persistence.Entity");
        assertThat(type.getMethods().toString()).isEqualTo("String name();");
    }

    private void verifyDescriptor(CodeSourceDescriptor descriptor, String scope) {

        verifyDescriptor(descriptor, scope, "mmm-code-java-impl");
    }

    private void verifyDescriptor(CodeSourceDescriptor descriptor, String scope, String artifactId) {

        verifyDescriptor(descriptor, scope, artifactId, "net.sf.m-m-m");
    }

    private void verifyDescriptor(CodeSourceDescriptor descriptor, String scope, String artifactId, String groupId) {

        assertThat(descriptor.getGroupId()).isEqualTo(groupId);
        assertThat(descriptor.getArtifactId()).isEqualTo(artifactId);
        String version = descriptor.getVersion();
        assertThat(version).matches(VERSION_PATTERN);
        String expectedId = groupId + ":" + artifactId + ":" + version;
        if (scope != null) {
            expectedId = expectedId + ":" + scope;
        }
        assertThat(descriptor.getId()).isEqualTo(expectedId);
    }

    private void verifyDependency(CodeSource source, String bytePath, String sourcePath, String scope) {

        assertThat(source.getByteCodeLocation().getAbsolutePath()).isEqualTo(new File(bytePath).getAbsolutePath());
        assertThat(source.getSourceCodeLocation().getAbsolutePath()).isEqualTo(new File(sourcePath).getAbsolutePath());
        verifyDescriptor(source.getDescriptor(), scope);
    }

    /**
     * Test full integration of {@link JavaContext#getType(String)} from byte-code and source-code (from local
     * filesystem).
     */
    @Test
    public void testTypeWithSourceFromFilesystem() {

        // given
        JavaContext context = getContext();
        Class<JavaContext> clazz = JavaContext.class;

        // when
        CodeType type = context.getType(clazz.getName());

        // then
        verifyHeader(type.getFile());
        verifyClass(type, clazz, context);
        assertThat(type.getDoc().getLines()).containsExactly(
            "Implementation of {@link JavaContext} for the {@link #getParent() root} context.", "",
            "@author Joerg Hohwiller (hohwille at users.sourceforge.net)", "@since 1.0.0");
        assertThat(type.getMethods().getDeclared("getRootContext").getReturns().getDoc().getLines())
            .containsExactly("the root {@link JavaContext context} responsible for the fundamental code (from JDK).");
    }

    /**
     * Test full integration of {@link JavaContext#getType(String)} from byte-code and source-code (from JAR
     * files).
     */
    @Test
    public void testTypeWithSourceFromJar() {

        // given
        JavaContext context = getContext();
        // javax.inject (JSR-330) is expected to be stable so the test will not break
        Class<?> clazz = Named.class;

        // when
        CodeType type = context.getType(clazz.getName());

        // then
        assertThat(type.getFile().getComment().getCommentLines()).containsExactly(
            "Copyright (C) 2009 The JSR-330 Expert Group", "",
            "Licensed under the Apache License, Version 2.0 (the \"License\");",
            "you may not use this file except in compliance with the License.",
            "You may obtain a copy of the License at", "", "     http://www.apache.org/licenses/LICENSE-2.0", "",
            "Unless required by applicable law or agreed to in writing, software",
            "distributed under the License is distributed on an \"AS IS\" BASIS,",
            "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.",
            "See the License for the specific language governing permissions and", "limitations under the License.");

        assertThat(type.getDoc().getLines()).containsExactly("String-based {@linkplain Qualifier qualifier}.", "",
            "<p>Example usage:", "", "<pre>", "  public class Car {",
            "    &#064;Inject <b>@Named(\"driver\")</b> Seat driverSeat;",
            "    &#064;Inject <b>@Named(\"passenger\")</b> Seat passengerSeat;", "    ...", "  }</pre>");

        List<? extends CodeMethod> methods = type.getMethods().getDeclared();
        assertThat(methods).hasSize(1);
        CodeMethod method = methods.get(0);
        assertThat(method.getName()).isEqualTo("value");
        assertThat(method.getDoc().getLines()).containsExactly("The name.");
    }

}
