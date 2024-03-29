/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Named;

import org.junit.jupiter.api.Test;

import io.github.mmm.code.api.item.CodeItem;
import io.github.mmm.code.api.member.CodeFields;
import io.github.mmm.code.api.member.CodeMethod;
import io.github.mmm.code.api.source.CodeSource;
import io.github.mmm.code.api.source.CodeSourceDescriptor;
import io.github.mmm.code.api.type.CodeGenericType;
import io.github.mmm.code.api.type.CodeType;
import io.github.mmm.code.base.type.BaseType;
import io.github.mmm.code.impl.java.source.maven.JavaSourceProviderUsingMaven;
import io.github.mmm.code.impl.java.source.maven.MavenClassLoader;
import io.github.mmm.code.impl.java.source.maven.MavenDependencyCollector;

/**
 * Test of {@link JavaExtendedContext} using {@link JavaSourceProviderUsingMaven}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaExtendedContextWithMavenAndSourceCodeTest extends AbstractBaseTypeTest {

  private static final Pattern VERSION_PATTERN = Pattern.compile("[0-9]+(\\.[0-9]+)*(-beta[0-9]+)?(-SNAPSHOT)?");

  /** Root Path where test data is stored. */
  private static final File ROOT_TEST_PATH = new File("target/test-classes/testdata/");

  /**
   * Get context from current project
   *
   * @return the {@link JavaContext} of the current project
   */
  private JavaContext getContext() {

    return JavaSourceProviderUsingMaven.createFromLocalMavenProject();
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
    assertThat(dependencies).hasSize(4);
    CodeSource compileDependency = dependencies.get(0);
    verifyDependency(compileDependency, "target/classes", "src/main/java", "compile");
    assertThat(source.getParent()).isSameAs(compileDependency);
    verifyContainsDescriptor(dependencies, "test", "assertj-core", "org.assertj");
    verifyContainsDescriptor(dependencies, "test", "junit-jupiter", "org.junit.jupiter");
    verifyContainsDescriptor(dependencies, "test", "logback-classic", "ch.qos.logback");
  }

  /**
   * Testing own maven project with own classloader.
   */
  @Test
  public void testContextFromOwnMavenProject() {

    // given
    File mavenProjectDirectory = new File(".");
    JavaContext context = JavaSourceProviderUsingMaven.createFromLocalMavenProject(mavenProjectDirectory, true);

    // when
    CodeType type = context.getRequiredType(CodeItem.class.getName());

    // then
    assertThat(type.getContext()).isSameAs(context);
    assertThat(type.getDoc().getLines()).contains(
        "Abstract top-level interface for any item of code as defined by this API. It reflects code structure.");
  }

  /**
   * Testing the retrieval of the context (including class loaders) from a local Maven project. Also tests whether we
   * are able to retrieve a class from this local Maven project
   */
  @Test
  public void testContextFromCustomMavenProject() {

    // given
    String entityClass = "com.maven.project.sampledatamanagement.dataaccess.api.SampleDataEntity";
    File mavenProjectDirectory = new File(ROOT_TEST_PATH, "localmavenproject/maven.project/core"); // test Maven project
    MavenDependencyCollector dependencyCollector = new MavenDependencyCollector(true, true, "eclipse-target");

    // when
    JavaContext context = JavaSourceProviderUsingMaven.createFromLocalMavenProject(mavenProjectDirectory,
        dependencyCollector);

    // then
    CodeGenericType objectType = context.getType(Object.class);
    assertThat(objectType.getContext()).isSameAs(JavaRootContext.get());
    assertThat(context.getType(JavaContext.class.getName())).isNull();

    CodeType type = context.getRequiredType(entityClass);
    assertThat(type.getContext()).isSameAs(context);
    assertThat(type.getDoc().getLines()).containsExactly("This is the JavaDoc of {@link SampleDataEntity}.");
    assertThat(type.getDoc().getSourceCode()).isEqualTo("/** This is the JavaDoc of {@link SampleDataEntity}. */\n");
    CodeSource source = type.getSource();
    assertThat(source.getByteCodeLocation().getName()).isEqualTo("classes");

    CodeFields fields = type.getFields();
    assertThat(fields.getDeclared("name")).isNotNull();
    assertThat(fields.getDeclared("mail")).isNotNull();
    assertThat(fields.getDeclared("surname")).isNotNull();
    assertThat(fields.getDeclared("age")).isNotNull();

    BaseType genericEntityType = context.getType("com.devonfw.module.basic.common.api.entity.GenericEntity");
    assertThat(genericEntityType).isNotNull();

    List<? extends CodeSource> dependencies = source.getDependencies().getDeclared();
    assertThat(dependencies).hasSize(2);

    type = context.getType("javax.persistence.Entity");
    assertThat(type.getMethods().toString()).isEqualTo("String name();");

    assertThat(context.getClassLoader()).isInstanceOf(MavenClassLoader.class);
  }

  private void verifyDescriptor(CodeSourceDescriptor descriptor, String scope) {

    verifyDescriptor(descriptor, scope, "mmm-code-java-impl");
  }

  private void verifyDescriptor(CodeSourceDescriptor descriptor, String scope, String artifactId) {

    verifyDescriptor(descriptor, scope, artifactId, "io.github.m-m-m");
  }

  private void verifyContainsDescriptor(List<? extends CodeSource> dependencies, String scope, String artifactId,
      String groupId) {

    for (CodeSource dependency : dependencies) {
      CodeSourceDescriptor descriptor = dependency.getDescriptor();
      if (groupId.equals(descriptor.getGroupId()) && artifactId.equals(descriptor.getArtifactId())) {
        verifyDescriptor(descriptor, scope, artifactId, groupId);
        return;
      }
    }
    fail("Dependency not found: " + groupId + ":" + artifactId);
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
        "Implementation of {@link io.github.mmm.code.api.CodeContext} for Java.", "",
        "@author Joerg Hohwiller (hohwille at users.sourceforge.net)", "@since 1.0.0");
    assertThat(type.getMethods().getDeclared("getRootContext").getReturns().getDoc().getLines())
        .containsExactly("the root {@link JavaContext context} responsible for the fundamental code (from JDK).");
  }

  /**
   * Test full integration of {@link JavaContext#getType(String)} from byte-code and source-code (from JAR files).
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
        "you may not use this file except in compliance with the License.", "You may obtain a copy of the License at",
        "", "     http://www.apache.org/licenses/LICENSE-2.0", "",
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
