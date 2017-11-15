/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

import net.sf.mmm.code.api.source.CodeSourceDescriptor;
import net.sf.mmm.code.base.source.BaseSource;
import net.sf.mmm.code.base.source.BaseSourceImpl;
import net.sf.mmm.code.base.type.BaseType;
import net.sf.mmm.code.impl.java.source.maven.JavaSourceProviderUsingMaven;

/**
 * Test of {@link JavaExtendedContext} using {@link JavaSourceProviderUsingMaven}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaExtendedContextWithMavenAndSourceCodeTest extends AbstractBaseTypeTest {

  private static final Pattern VERSION_PATTERN = Pattern.compile("[0-9]+\\.[0-9]+\\.[0-9]+(-SNAPSHOT)?");

  private JavaContext getContext() {

    // TODO move code to new advanced contex class
    JavaSourceProviderUsingMaven provider = new JavaSourceProviderUsingMaven();
    JavaContext parentContext = JavaRootContext.get();
    BaseSourceImpl source = provider.createFromLocalMavenProject(parentContext);
    return new JavaExtendedContext(source, provider);
  }

  /**
   * Test the very basic methods of {@link JavaContext}.
   */
  @Test
  public void testBasics() {

    // given
    JavaContext context = getContext();
    // when
    BaseSource source = context.getSource();
    // then
    verifyDependency(source, "target/test-classes", "src/test/java", "test");
    assertThat(source.getId()).contains("/code/java/impl");
    assertThat(source.getSource()).isSameAs(source);

    List<? extends BaseSource> dependencies = source.getDependencies().getDeclared();
    assertThat(dependencies).hasSize(2);
    BaseSource compileDependency = dependencies.get(0);
    verifyDependency(compileDependency, "target/classes", "src/main/java", "compile");
    assertThat(source.getParent()).isSameAs(compileDependency);
    BaseSource testDependency = dependencies.get(1);
    verifyDescriptor(testDependency.getDescriptor(), "test", "mmm-util-test");
  }

  private void verifyDescriptor(CodeSourceDescriptor descriptor, String scope) {

    verifyDescriptor(descriptor, scope, "mmm-code-java-impl");
  }

  private void verifyDescriptor(CodeSourceDescriptor descriptor, String scope, String artifactId) {

    assertThat(descriptor.getGroupId()).isEqualTo("net.sf.m-m-m");
    assertThat(descriptor.getArtifactId()).isEqualTo(artifactId);
    String version = descriptor.getVersion();
    assertThat(version).matches(VERSION_PATTERN);
    String expectedId = "net.sf.m-m-m:" + artifactId + ":" + version;
    if (scope != null) {
      expectedId = expectedId + ":" + scope;
    }
    assertThat(descriptor.getId()).isEqualTo(expectedId);
  }

  private void verifyDependency(BaseSource source, String bytePath, String sourcePath, String scope) {

    assertThat(source.getByteCodeLocation().getAbsolutePath()).isEqualTo(new File(bytePath).getAbsolutePath());
    assertThat(source.getSourceCodeLocation().getAbsolutePath()).isEqualTo(new File(sourcePath).getAbsolutePath());
    verifyDescriptor(source.getDescriptor(), scope);
  }

  /**
   * Test full integration of {@link JavaContext#getType(String)} from byte-code and source-code.
   */
  @Test
  public void testFullType() {

    // given
    JavaContext context = getContext();
    Class<JavaContext> clazz = JavaContext.class;

    // when
    BaseType type = context.getType(clazz.getName());

    // then
    verifyHeader(type.getFile());
    verifyClass(type, clazz, context);
    assertThat(type.getDoc().getLines()).containsExactly("Implementation of {@link JavaContext} for the {@link #getParent() root} context.", "",
        "@author Joerg Hohwiller (hohwille at users.sourceforge.net)", "@since 1.0.0");
    assertThat(type.getMethods().getDeclared("getRootContext").getReturns().getDoc().getLines())
        .containsExactly("the root {@link JavaContext context} responsible for the fundamental code (from JDK).");
  }

}
