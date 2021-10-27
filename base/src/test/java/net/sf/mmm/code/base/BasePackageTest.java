/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base;

import java.util.List;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import net.sf.mmm.code.api.CodeName;
import net.sf.mmm.code.base.annoation.BaseAnnotation;
import net.sf.mmm.code.base.comment.BaseBlockComment;
import net.sf.mmm.code.base.source.BaseSource;
import net.sf.mmm.code.base.type.BaseType;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * Test of {@link BasePackage} and {@link BasePathElements}.
 */
public class BasePackageTest extends BaseContextTest {

  /**
   * Test of {@link BasePackage#BasePackage(BasePackage, String)} with invalid and valid simple names.
   */
  @Test
  public void testSimpleNames() {

    // given
    BaseContext context = createContext();
    BaseSource source = context.getSource();
    BasePackage rootPackage = source.getRootPackage();

    // when + then
    verifyPackageSimpleName(rootPackage, null, false);
    verifyPackageSimpleName(rootPackage, "", false);
    verifyPackageSimpleName(rootPackage, "package", false);
    verifyPackageSimpleName(rootPackage, "private", false);
    verifyPackageSimpleName(rootPackage, "package1", true);
  }

  private void verifyPackageSimpleName(BasePackage parentPackage, String simpleName, boolean valid) {

    try {
      new BasePackage(parentPackage, simpleName);
      if (!valid) {
        failBecauseExceptionWasNotThrown(RuntimeException.class);
      }
    } catch (RuntimeException e) {
      if (valid) {
        fail("Simple name '" + simpleName + "' was expected to be a valid for package.", e);
      }
      assertThat(e).hasMessageContaining("" + simpleName);
    }
  }

  /**
   * Test of {@link BasePathElements#createPackage(String)}.
   */
  @Test
  public void testRootPackage() {

    // given
    BaseContext context = createContext();
    BaseSource source = context.getSource();

    // when
    BasePackage rootPackage = source.getRootPackage();

    // then
    assertThat(rootPackage.getSimpleName()).isEmpty();
    assertThat(rootPackage.getQualifiedName()).isEmpty();
    assertThat(rootPackage.getParentPackage()).isNull();
    assertThat(rootPackage.getParent()).isSameAs(source);
    assertThat(rootPackage.getSource()).isSameAs(source);
    assertThat(rootPackage.getContext()).isSameAs(context);
    verifyPackageEmpty(rootPackage, false);
    assertThat(rootPackage.toString()).isEmpty();
    assertThat(rootPackage.getSourceCode()).isEmpty();
  }

  /**
   * Test of {@link BasePathElements#createPackage(String)}.
   */
  @Test
  public void testCreatePackage() {

    // given
    BaseContext context = createContext();
    BaseSource source = context.getSource();
    BasePackage rootPackage = source.getRootPackage();
    String pkgName = "mydomain";

    // when
    BasePackage pkg = rootPackage.getChildren().createPackage(pkgName);

    // then
    assertThat(pkg.getSimpleName()).isEqualTo(pkgName);
    assertThat(pkg.getQualifiedName()).isEqualTo(pkgName);
    assertThat(pkg.getParentPackage()).isSameAs(rootPackage);
    assertThat(pkg.getParent()).isSameAs(rootPackage);
    assertThat(pkg.getSource()).isSameAs(source);
    assertThat(pkg.getContext()).isSameAs(context);
    assertThat(pkg.isImmutable()).isFalse();
    verifyPackageEmpty(pkg, true);
    assertThat(pkg.getSourceCode()).isEqualTo("package mydomain;\n");
    assertThat(pkg.toString()).isEqualTo("package mydomain;");
  }

  /**
   * Test of
   * {@link BasePathElements#getPackage(net.sf.mmm.code.api.CodeName, boolean, java.util.function.BiFunction, boolean)}
   * with factory to create packages but without adding.
   */
  @Test
  public void testGetPackage() {

    testGetPackageParameterized(false, false);
    testGetPackageParameterized(false, true); // sick signature but protected so only internal and no API
  }

  /**
   * Test of
   * {@link BasePathElements#getPackage(net.sf.mmm.code.api.CodeName, boolean, java.util.function.BiFunction, boolean)}
   * with factory to create packages and with force adding.
   */
  @Test
  public void testGetPackageForceAdd() {

    testGetPackageParameterized(true, true);
  }

  /**
   * Test of
   * {@link BasePathElements#getPackage(net.sf.mmm.code.api.CodeName, boolean, java.util.function.BiFunction, boolean)}
   * with factory to create packages and with adding failing because immutable.
   */
  @Test
  public void testGetPackageAddFailImmutable() {

    try {
      testGetPackageParameterized(true, false);
      failBecauseExceptionWasNotThrown(ReadOnlyException.class);
    } catch (ReadOnlyException e) {
      assertThat(e.getNlsMessage().getMessage())
          .isEqualTo("Failed to modify \"BasePathElements:«root».getChildren()\" as it is read-only!");
    }
  }

  @SuppressWarnings("unchecked")
  private void testGetPackageParameterized(boolean add, boolean forceAdd) {

    // given
    BaseContext context = createContext();
    BaseSource source = context.getSource();
    BasePackage rootPackage = source.getRootPackage();
    String pkgName = "com.foo.bar.some";
    CodeName pkgQName = context.parseName(pkgName);

    // when
    BasePackage pkg = rootPackage.getChildren().getPackage(pkgQName, false, this::createPackage, add, forceAdd);

    // then
    assertThat(pkg.getQualifiedName()).isEqualTo(pkgName);
    assertThat(pkg.getSimpleName()).isEqualTo("some");
    assertThat(pkg.getComment()).isSameAs(TEST_COMMENT);
    assertThat(pkg.getDoc().getLines()).containsExactly(TEST_TEXT1, TEST_TEXT2);
    assertThat((List<BaseAnnotation>) pkg.getAnnotations().getDeclared()).hasSize(1)
        .allMatch(x -> x.getType() == getTestAnnotationType(null));
    assertThat(pkg.isImmutable()).isTrue();
    BasePackage pkgFromRoot = rootPackage.getChildren().getPackage(pkgQName);
    if (add) {
      assertThat(pkgFromRoot).isSameAs(pkg);
    } else {
      assertThat(pkgFromRoot).isNull();
    }
  }

  private BasePackage createPackage(BasePackage pkg, String simpleName) {

    return createPackage(pkg, simpleName, false);
  }

  private BasePackage createPackage(BasePackage pkg, String simpleName, boolean source) {

    Supplier<BasePackage> sourceSupplier = null;
    if (!source) {
      sourceSupplier = () -> createPackage(pkg, simpleName, true);
    }
    BasePackage basePackage = new BasePackage(pkg, simpleName, null, sourceSupplier, !source);
    if (source) {
      getTestAnnotation(basePackage.getAnnotations());
      basePackage.setComment(TEST_COMMENT);
      basePackage.getDoc().getLines().add(TEST_TEXT1);
      basePackage.getDoc().getLines().add(TEST_TEXT2);
    }
    return basePackage;
  }

  /**
   * Test of {@link BasePathElements} with standard packages from JDK such as "java.lang".
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Test
  public void testJavaLang() {

    // given
    BaseContext context = createContext();
    BaseSource source = context.getSource();
    Class<String> stringClass = String.class;
    Class<List> listClass = List.class;

    // when
    BaseType stringType = getClassType(context, stringClass);
    BaseType integerType = getClassType(context, Integer.class);
    BaseType listType = getClassType(context, listClass);
    BasePackage javaLangPackage = stringType.getParentPackage();
    assertThat(javaLangPackage).isSameAs(integerType.getParentPackage());
    assertThat(javaLangPackage.getQualifiedName()).isEqualTo("java.lang");
    assertThat(javaLangPackage.getReflectiveObject()).isSameAs(stringClass.getPackage());
    assertThat(javaLangPackage.isJavaLang()).isTrue();
    assertThat(javaLangPackage.isImmutable()).isTrue();
    BasePackage javaUtilPackage = listType.getParentPackage();
    assertThat(javaUtilPackage.getQualifiedName()).isEqualTo("java.util");
    assertThat(javaUtilPackage.getReflectiveObject()).isSameAs(listClass.getPackage());
    assertThat(javaUtilPackage.isJavaLang()).isFalse();
    assertThat(javaUtilPackage.isImmutable()).isTrue();

    // then
    BasePathElements rootPackageChildren = source.getRootPackage().getChildren();
    BasePackage javaPackage = rootPackageChildren.getPackage("java");
    assertThat(javaPackage.getChildren().getPackage("lang")).isSameAs(javaLangPackage);
    assertThat(javaPackage.isJava()).isTrue();
    assertThat(javaPackage.isImmutable()).isTrue();
    assertThat(javaLangPackage.isJava()).isFalse();
    assertThat(javaLangPackage.getChildren().getType("String")).isSameAs(stringType);
    assertThat(javaLangPackage.getChildren().getFile("String")).isSameAs(stringType.getFile());
    assertThat(rootPackageChildren.getPackage(context.parseName("java.lang"))).isSameAs(javaLangPackage);
    assertThat(rootPackageChildren.getFile(context.parseName("java.lang.String")).getType()).isSameAs(stringType);
    assertThat((List<BasePathElement>) javaPackage.getChildren().getDeclared()).contains(javaLangPackage,
        javaUtilPackage);
    assertThat((List<BasePathElement>) javaLangPackage.getChildren().getDeclared()).contains(stringType.getFile(),
        integerType.getFile());
  }

  private BaseType getClassType(BaseContext context, Class<?> clazz) {

    BaseType type = context.getType(clazz).asType();
    assertThat(type.getQualifiedName()).isEqualTo(clazz.getName());
    assertThat(type.getSimpleName()).isEqualTo(clazz.getSimpleName());
    assertThat(type.getReflectiveObject()).isSameAs(clazz);
    Package pkg = clazz.getPackage();
    if (pkg != null) {
      BasePackage parentPackage = type.getParentPackage();
      assertThat(parentPackage.getReflectiveObject()).isSameAs(pkg);
      assertThat(parentPackage.getQualifiedName()).isEqualTo(pkg.getName());
    }
    return type;
  }

  private void verifyPackageEmpty(BasePackage pkg, boolean children) {

    verifyPackageEmpty(pkg, children, true, true);
  }

  private void verifyPackageEmpty(BasePackage pkg, boolean children, boolean reflect, boolean sourceCodeObject) {

    assertThat(pkg.getAnnotations().getAll()).isEmpty();
    if (children) {
      assertThat(pkg.getChildren().getDeclared()).isEmpty();
    }
    assertThat(pkg.getDoc().isEmpty()).isTrue();
    assertThat(pkg.getComment()).isSameAs(BaseBlockComment.EMPTY_COMMENT);
    if (reflect) {
      assertThat(pkg.getReflectiveObject()).isNull();
    }
    if (sourceCodeObject) {
      assertThat(pkg.getSourceCodeObject()).isNull();
    }
  }

}
