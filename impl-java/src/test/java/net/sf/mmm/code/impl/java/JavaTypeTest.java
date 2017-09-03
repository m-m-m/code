/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.api.type.CodeTypeCategory;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Test of {@link JavaType}.
 */
public class JavaTypeTest extends Assertions {

  /**
   * Test of {@link JavaType} for empty class.
   */
  @Test
  public void testEmptyClass() {

    // given
    JavaContext context = new JavaContext();
    JavaPackage rootPackage = context.getRootPackage();
    String pkgName = "mydomain";
    JavaPackage pkg = context.createPackage(rootPackage, pkgName);
    String simpleName = "MyClass";

    // when
    JavaType type = context.createType(pkg, simpleName);

    // then
    assertThat(type.getSimpleName()).isEqualTo(simpleName);
    assertThat(type.getQualifiedName()).isEqualTo(pkgName + "." + simpleName);
    assertThat(type.getParentPackage()).isSameAs(pkg);
    assertThat(type.getCategory()).isEqualTo(CodeTypeCategory.CLASS);
    assertThat(type.getModifiers()).isEqualTo(CodeModifiers.MODIFIERS_PUBLIC);
    assertThat(type.getDoc()).isNotNull();
    assertThat(type.getDoc().isEmpty()).isTrue();
    assertThat(type.getAnnotations()).isEmpty();
    assertThat(type.getFields().getDeclared()).isEmpty();
    assertThat(type.getMethods().getDeclared()).isEmpty();
    assertThat(type.getConstructors().getDeclared()).isEmpty();
    assertThat(type.getFile().toString()).isEqualTo("package mydomain;\n" + //
        "\n" + //
        "public class MyClass {\n" + //
        "}\n");
  }

  /**
   * Test of {@link JavaType} with {@link JavaType#getNestedTypes() nested types} of all
   * {@link JavaType#getCategory() categories}.
   */
  @Test
  public void testNestedTypesWithDoc() {

    // given
    JavaContext context = new JavaContext();
    JavaPackage rootPackage = context.getRootPackage();
    String pkgName = "mydomain";
    JavaPackage pkg = context.createPackage(rootPackage, pkgName);
    String simpleNameTop = "ClassToplevel";
    String simpleNameNested1 = "StaticClassNested1";
    String simpleNameNested2 = "InterfaceClassNested2";
    String simpleNameNested21 = "ClassNested2_1";
    String simpleNameNested211 = "AnnotationNested2_1_1";
    String simpleNameNested212 = "EnumNested2_1_2";

    // when
    JavaType classTop = context.createType(pkg, simpleNameTop);
    JavaType classStaticNested1 = context.createType(classTop, simpleNameNested1);
    classStaticNested1.setModifiers(CodeModifiers.MODIFIERS_PUBLIC_STATIC);
    JavaType interfacetypeNested2 = context.createType(classTop, simpleNameNested2);
    interfacetypeNested2.setCategory(CodeTypeCategory.INTERFACE);
    JavaType classNested21 = context.createType(interfacetypeNested2, simpleNameNested21);
    JavaType annotationNested211 = context.createType(classNested21, simpleNameNested211);
    annotationNested211.setCategory(CodeTypeCategory.ANNOTATION);
    JavaType enumNested212 = context.createType(classNested21, simpleNameNested212);
    enumNested212.setCategory(CodeTypeCategory.ENUMERAION);
    addDummyDoc(classTop);

    // then
    assertThat(classTop.getSimpleName()).isEqualTo(simpleNameTop);
    assertThat(classStaticNested1.getSimpleName()).isEqualTo(simpleNameNested1);
    assertThat(interfacetypeNested2.getSimpleName()).isEqualTo(simpleNameNested2);
    assertThat(classNested21.getSimpleName()).isEqualTo(simpleNameNested21);
    assertThat(annotationNested211.getSimpleName()).isEqualTo(simpleNameNested211);
    assertThat(enumNested212.getSimpleName()).isEqualTo(simpleNameNested212);
    assertThat(classTop.getNestedTypes()).containsExactly(classStaticNested1, interfacetypeNested2);
    assertThat(interfacetypeNested2.getNestedTypes()).containsExactly(classNested21);
    assertThat(classNested21.getNestedTypes()).containsExactly(annotationNested211, enumNested212);
    assertThat(classTop.getFile().toString()).isEqualTo("package mydomain;\n" + //
        "\n" + //
        "/** Doc for {@link ClassToplevel}. */\n" + //
        "public class ClassToplevel {\n" + //
        "\n" + //
        "  /** Doc for {@link StaticClassNested1}. */\n" + //
        "  public static class StaticClassNested1 {\n" + //
        "  }\n" + //
        "\n" + //
        "  /** Doc for {@link InterfaceClassNested2}. */\n" + //
        "  public interface InterfaceClassNested2 {\n" + //
        "\n" + //
        "    /** Doc for {@link ClassNested2_1}. */\n" + //
        "    public class ClassNested2_1 {\n" + //
        "\n" + //
        "      /** Doc for {@link AnnotationNested2_1_1}. */\n" + //
        "      public @interface AnnotationNested2_1_1 {\n" + //
        "      }\n" + //
        "\n" + //
        "      /** Doc for {@link EnumNested2_1_2}. */\n" + //
        "      public enum EnumNested2_1_2 {\n" + //
        "      }\n" + //
        "    }\n" + //
        "  }\n" + //
        "}\n");
  }

  private void addDummyDoc(CodeType type) {

    type.getDoc().getLines().add("Doc for {@link " + type.getSimpleName() + "}.");
    for (CodeType child : type.getNestedTypes()) {
      addDummyDoc(child);
    }
  }

}
