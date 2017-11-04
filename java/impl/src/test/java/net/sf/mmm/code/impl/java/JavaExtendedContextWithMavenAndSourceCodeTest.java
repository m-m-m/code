/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

import net.sf.mmm.code.api.source.CodeSourceDescriptor;
import net.sf.mmm.code.base.loader.BaseSourceCodeProviderDirectory;
import net.sf.mmm.code.base.loader.SourceCodeProvider;
import net.sf.mmm.code.base.source.BaseSource;
import net.sf.mmm.code.base.source.BaseSourceImpl;
import net.sf.mmm.code.base.type.BaseType;
import net.sf.mmm.code.impl.java.loader.AbstractJavaCodeLoader;
import net.sf.mmm.code.impl.java.loader.JavaLoader;
import net.sf.mmm.code.impl.java.source.maven.JavaSourceProviderUsingMaven;
import net.sf.mmm.code.java.maven.api.MavenConstants;

/**
 * Test of {@link JavaExtendedContext} using {@link JavaSourceProviderUsingMaven}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaExtendedContextWithMavenAndSourceCodeTest extends JavaTypeTest {

  private static final Pattern VERSION_PATTERN = Pattern.compile("[0-9]+\\.[0-9]+\\.[0-9]+(-SNAPSHOT)?");

  private JavaContext getContext() {

    // TODO move code to new advanced contex class
    JavaSourceProviderUsingMaven provider = new JavaSourceProviderUsingMaven();
    JavaContext parentContext = JavaRootContext.get();
    BaseSourceImpl source = provider.createFromLocalMavenProject(parentContext);
    SourceCodeProvider sourceCodeProvider = new BaseSourceCodeProviderDirectory(new File(MavenConstants.DEFAULT_SOURCE_DIRECTORY));
    AbstractJavaCodeLoader loader = new JavaLoader(sourceCodeProvider);
    return new JavaExtendedContext(provider, loader, source);
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
    assertThat(source.getByteCodeLocation()).isNull();
    assertThat(source.getSourceCodeLocation()).isNull();
    CodeSourceDescriptor descriptor = source.getDescriptor();
    verifyDescriptor(descriptor, null);
    assertThat(source.getId()).contains("/code/java/impl");
    assertThat(source.getSource()).isSameAs(source);

    List<? extends BaseSource> dependencies = source.getDependencies().getDeclared();
    assertThat(dependencies).hasSize(2);
    BaseSource compileDependency = dependencies.get(0);
    verifyDependency(compileDependency, "target/classes", "src/main/java", "compile");
    BaseSource testDependency = dependencies.get(1);
    verifyDependency(testDependency, "target/test-classes", "src/test/java", "test");
    assertThat(testDependency.getParent()).isSameAs(compileDependency);
  }

  private void verifyDescriptor(CodeSourceDescriptor descriptor, String scope) {

    assertThat(descriptor.getGroupId()).isEqualTo("net.sf.m-m-m");
    assertThat(descriptor.getArtifactId()).isEqualTo("mmm-code-java-impl");
    String version = descriptor.getVersion();
    assertThat(version).matches(VERSION_PATTERN);
    String expectedId = "net.sf.m-m-m:mmm-code-java-impl:" + version;
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
    verifyClass(type, clazz, context);
  }

}
