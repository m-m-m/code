/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base;

import org.junit.jupiter.api.Test;

import io.github.mmm.code.base.source.BaseSource;
import io.github.mmm.code.base.type.BaseType;

/**
 * Test of {@link BaseFile}.
 */
class BaseFileTest extends BaseContextTest {

  /**
   * Test of {@link BasePackage#BasePackage(BasePackage, String)} with invalid names.
   */
  @Test
  void testInvalidNames() {

    // arrange
    BaseContext context = createContext();
    BaseSource source = context.getSource();
    BasePackage rootPackage = source.getRootPackage();
    BasePackage pkg = new BasePackage(rootPackage, "pkg");

    // act + assert
    verifyFileSimpleName(rootPackage, null, false);
    verifyFileSimpleName(rootPackage, "", false);
    verifyFileSimpleName(pkg, "class", false);
    verifyFileSimpleName(pkg, "interface", false);
    verifyFileSimpleName(pkg, "enum", false);
    verifyFileSimpleName(pkg, "private", false);
    verifyFileSimpleName(rootPackage, "java", true);
    verifyFileSimpleName(pkg, "Class", true);
  }

  private void verifyFileSimpleName(BasePackage parentPackage, String simpleName, boolean valid) {

    try {
      new BaseFile(parentPackage, simpleName);
      if (!valid) {
        failBecauseExceptionWasNotThrown(RuntimeException.class);
      }
    } catch (RuntimeException e) {
      if (valid) {
        fail("Simple name '" + simpleName + "' was expected to be a valid for file.", e);
      }
      assertThat(e).hasMessageContaining("" + simpleName);
    }
  }

  /**
   * Test of {@link BaseFile} for {@link BaseContext#getRootType()}.
   */
  @Test
  void testObjectFile() {

    // arrange
    BaseContext context = createContext();
    BaseSource source = context.getSource();

    // act
    BaseType type = context.getRootType();
    BaseFile file = type.getFile();

    // assert
    assertThat(type.getSimpleName()).isEqualTo("Object");
    assertThat(type.getQualifiedName()).isEqualTo("java.lang.Object");
    assertThat(type.isImmutable()).isTrue();
    assertThat(file.isImmutable()).isTrue();
    assertThat(file.getParentPackage().getQualifiedName()).isEqualTo("java.lang");
    assertThat(file.getParent()).isSameAs(file.getParentPackage());
    assertThat(file.getSource()).isSameAs(source);
    assertThat(file.getContext()).isSameAs(context);
    assertThat(file.toString()).isEqualTo("Object");
    assertThat(file.getImports()).isEmpty();
  }

  /**
   * Test of {@link BasePathElements#createFile(String)}.
   */
  @Test
  void testCreatePackage() {

    // arrange
    BaseContext context = createContext();
    BaseSource source = context.getSource();
    BasePackage rootPackage = source.getRootPackage();
    String fileName = "MyFile";

    // act
    BaseFile file = rootPackage.getChildren().createFile(fileName);

    // assert
    assertThat(file.getSimpleName()).isEqualTo(fileName);
    assertThat(file.getQualifiedName()).isEqualTo(fileName);
    assertThat(file.getParentPackage()).isSameAs(rootPackage);
    assertThat(file.getParent()).isSameAs(rootPackage);
    assertThat(file.getSource()).isSameAs(source);
    assertThat(file.getContext()).isSameAs(context);
    assertThat(file.isImmutable()).isFalse();
    assertThat(file.getImports()).isEmpty();
    assertThat(file.getSourceCode()).isEqualTo("public class MyFile {\n" + //
        "}\n");
    assertThat(file.getType().getSimpleName()).isSameAs(fileName);
  }

}
