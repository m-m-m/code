/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.github.mmm.code.api.source.CodeSourceDescriptor;
import io.github.mmm.code.api.type.CodeType;
import io.github.mmm.code.base.loader.BaseSourceCodeProviderDirectory;
import io.github.mmm.code.base.loader.BaseSourceLoader;
import io.github.mmm.code.base.loader.SourceCodeProvider;
import io.github.mmm.code.base.source.BaseSourceDescriptorType;
import io.github.mmm.code.base.source.BaseSourceImpl;
import io.github.mmm.code.base.source.BaseSourceProvider;
import io.github.mmm.code.base.type.BaseType;
import io.github.mmm.code.impl.java.loader.JavaSourceLoader;

/**
 * Test of {@link JavaExtendedContext} with only source code available.
 */
class JavaSourceCodeOnlyTest extends AbstractBaseTypeTest {

  /** Parse Demo.java where only source-code is available. */
  @Test
  void testSourceCodeOnly() {

    File sourceLocation = new File("src/test/resources/testdata/sourcecode");
    SourceCodeProvider sourceCodeProvider = new BaseSourceCodeProviderDirectory(sourceLocation);
    BaseSourceLoader loader = new JavaSourceLoader(sourceCodeProvider);
    String id = "com.example.demo";
    CodeSourceDescriptor descriptor = new BaseSourceDescriptorType(id);
    BaseSourceImpl source = new BaseSourceImpl(null, sourceLocation, id, descriptor, loader);
    BaseSourceProvider sourceProvider = null;
    JavaExtendedContext context = new JavaExtendedContext(JavaRootContext.get(), source, sourceProvider);
    BaseType type = context.getType("com.example.demo.Demo");
    assertThat(type.getQualifiedName().equals("com.example.demo.Demo"));
    assertThat(type.getDoc().getLines())
        .containsExactlyInAnyOrder("This is a simple java file to test reading of source-code only Java context.");
    assertThat(type.getConstructors().getDeclared()).hasSize(1);
    assertThat(type.getMethods().getDeclared()).hasSize(2);
    List<? extends CodeType> nestedTypes = type.getNestedTypes().getDeclared();
    assertThat(nestedTypes).hasSize(1);
    CodeType nestedType = nestedTypes.get(0);
    assertThat(nestedType.getQualifiedName()).isEqualTo("com.example.demo.Demo.Foo");
    assertThat(context.getType("com.example.demo.Demo.Foo")).isSameAs(nestedType);
    context.close();
  }

}
