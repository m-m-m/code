/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base;

import java.util.List;
import java.util.function.Supplier;

import org.junit.Test;

import net.sf.mmm.code.api.CodeName;
import net.sf.mmm.code.base.annoation.BaseAnnotation;
import net.sf.mmm.code.base.comment.BaseSingleLineComment;
import net.sf.mmm.code.base.source.BaseSource;
import net.sf.mmm.code.base.type.BaseType;

/**
 * Test of {@link BasePackage} and {@link BasePathElements}.
 */
public class BasePackageTest extends BaseContextTest {

  private static final String TEST_TEXT1 = "This is a test";

  private static final String TEST_TEXT2 = "API-Doc comment.";

  private static final BaseSingleLineComment TEST_COMMENT = new BaseSingleLineComment(TEST_TEXT1);

  private BaseAnnotation annotation;

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
   * {@link BasePathElements#getPackage(net.sf.mmm.code.api.CodeName, boolean, java.util.function.BiFunction, boolean)}.
   */
  @SuppressWarnings("unchecked")
  @Test
  public void testGetPackage() {

    // given
    BaseContext context = createContext();
    BaseSource source = context.getSource();
    BasePackage rootPackage = source.getRootPackage();
    String pkgName = "com.foo.bar.some";
    CodeName pkgQName = context.parseName(pkgName);

    // when
    BasePackage pkg = rootPackage.getChildren().getPackage(pkgQName, false, this::createPackage, false);

    // then
    assertThat(pkg.getQualifiedName()).isEqualTo(pkgName);
    assertThat(pkg.getSimpleName()).isEqualTo("some");
    assertThat(pkg.getComment()).isSameAs(TEST_COMMENT);
    assertThat(pkg.getDoc().getLines()).containsExactly(TEST_TEXT1, TEST_TEXT2);
    assertThat((List<BaseAnnotation>) pkg.getAnnotations().getDeclared()).containsExactly(getTestAnnotation(null));
    assertThat(pkg.isImmutable()).isTrue();
    assertThat(rootPackage.getChildren().getPackage(pkgQName)).isNull();
  }

  private BasePackage createPackage(BasePackage pkg, String simpleName) {

    return createPackage(pkg, simpleName, false);
  }

  private BasePackage createPackage(BasePackage pkg, String simpleName, boolean source) {

    Supplier<BasePackage> sourceSupplier = null;
    if (!source) {
      sourceSupplier = () -> createPackage(pkg, simpleName, true);
    }
    BasePackage basePackage = new BasePackage(pkg, simpleName, null, sourceSupplier);
    if (source) {
      basePackage.getAnnotations().add(getTestAnnotation(pkg.getContext().getSource()));
      basePackage.setComment(TEST_COMMENT);
      basePackage.getDoc().getLines().add(TEST_TEXT1);
      basePackage.getDoc().getLines().add(TEST_TEXT2);
    }
    return basePackage;
  }

  private BaseAnnotation getTestAnnotation(BaseSource source) {

    if (this.annotation == null) {
      BaseType type = (BaseType) source.getContext().getType(Override.class);
      this.annotation = new BaseAnnotation(source, type);
    }
    return this.annotation;
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
    BasePackage javaUtilPackage = listType.getParentPackage();
    assertThat(javaUtilPackage.getQualifiedName()).isEqualTo("java.util");
    assertThat(javaUtilPackage.getReflectiveObject()).isSameAs(listClass.getPackage());
    assertThat(javaUtilPackage.isJavaLang()).isFalse();

    // then
    BasePathElements rootPackageChildren = source.getRootPackage().getChildren();
    BasePackage javaPackage = rootPackageChildren.getPackage("java");
    assertThat(javaPackage.getChildren().getPackage("lang")).isSameAs(javaLangPackage);
    assertThat(javaPackage.isJava()).isTrue();
    assertThat(javaLangPackage.isJava()).isFalse();
    assertThat(javaLangPackage.getChildren().getType("String")).isSameAs(stringType);
    assertThat(rootPackageChildren.getPackage(context.parseName("java.lang"))).isSameAs(javaLangPackage);
    assertThat(rootPackageChildren.getFile(context.parseName("java.lang.String")).getType()).isSameAs(stringType);
    assertThat((List<BasePathElement>) javaPackage.getChildren().getDeclared()).contains(javaLangPackage, javaUtilPackage);
    assertThat((List<BasePathElement>) javaLangPackage.getChildren().getDeclared()).contains(stringType.getFile(), integerType.getFile());
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
    assertThat(pkg.getComment()).isNull();
    if (reflect) {
      assertThat(pkg.getReflectiveObject()).isNull();
    }
    if (sourceCodeObject) {
      assertThat(pkg.getSourceCodeObject()).isNull();
    }
  }

}
