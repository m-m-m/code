/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.type;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import net.sf.mmm.code.api.arg.CodeParameter;
import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.api.type.CodeTypeCategory;
import net.sf.mmm.code.base.BaseContext;
import net.sf.mmm.code.base.BaseContextTest;
import net.sf.mmm.code.base.BasePackage;
import net.sf.mmm.code.base.BasePathElements;
import net.sf.mmm.code.base.member.BaseMethod;

/**
 * Test of {@link BaseType}.
 */
public class BaseTypeTest extends BaseContextTest {

  /**
   * Test of {@link BaseType} for empty class.
   */
  @Test
  public void testEmptyClass() {

    // given
    BaseContext context = createContext();
    BasePackage rootPackage = context.getSource().getRootPackage();
    String pkgName = "mydomain";
    BasePackage pkg = rootPackage.getChildren().createPackage(pkgName);
    String simpleName = "MyClass";

    // when
    BaseType type = pkg.getChildren().createType(simpleName);

    // then
    assertThat(type.getSimpleName()).isEqualTo(simpleName);
    assertThat(type.getQualifiedName()).isEqualTo(pkgName + "." + simpleName);
    assertThat(type.getParentPackage()).isSameAs(pkg);
    assertThat(type.getCategory()).isEqualTo(CodeTypeCategory.CLASS);
    assertThat(type.getModifiers()).isEqualTo(CodeModifiers.MODIFIERS_PUBLIC);
    assertThat(type.getDoc()).isNotNull();
    assertThat(type.getDoc().isEmpty()).isTrue();
    assertThat(type.getAnnotations().getDeclared()).isEmpty();
    assertThat(type.getFields().getDeclared()).isEmpty();
    assertThat(type.getMethods().getDeclared()).isEmpty();
    assertThat(type.getConstructors().getDeclared()).isEmpty();
    assertThat(type.getFile().toString()).isEqualTo("MyClass");
    assertThat(type.getFile().getSourceCode()).isEqualTo("package mydomain;\n" + //
        "\n" + //
        "public class MyClass {\n" + //
        "}\n");
  }

  /**
   * Test of {@link BaseType} with {@link BaseType#getNestedTypes() nested types} of all {@link BaseType#getCategory()
   * categories}.
   */
  @SuppressWarnings("unchecked")
  @Test
  public void testNestedTypesWithDoc() {

    // given
    BaseContext context = createContext();
    BasePackage rootPackage = context.getSource().getRootPackage();
    String pkgName = "mydomain";
    BasePackage pkg = rootPackage.getChildren().createPackage(pkgName);
    String simpleNameTop = "ClassToplevel";
    String simpleNameNested1 = "StaticClassNested1";
    String simpleNameNested2 = "InterfaceClassNested2";
    String simpleNameNested21 = "ClassNested2_1";
    String simpleNameNested211 = "AnnotationNested2_1_1";
    String simpleNameNested212 = "EnumNested2_1_2";

    // when
    BaseType classTop = pkg.getChildren().createType(simpleNameTop);
    classTop.getTypeParameters().add("T").getDoc().getLines().add("generic type");
    BaseType classStaticNested1 = classTop.getNestedTypes().add(simpleNameNested1);
    classStaticNested1.setModifiers(CodeModifiers.MODIFIERS_PUBLIC_STATIC);
    BaseType interfacetypeNested2 = classTop.getNestedTypes().add(simpleNameNested2);
    interfacetypeNested2.setCategory(CodeTypeCategory.INTERFACE);
    BaseMethod method = interfacetypeNested2.getMethods().add("myMethod");
    method.getDoc().getLines().add("This method rocks.");
    BaseTypeVariable typeVariable = method.getTypeParameters().add("V");
    typeVariable.getDoc().getLines().add("generic value");
    method.getReturns().setType(typeVariable);
    method.getReturns().getDoc().getLines().add("the converted value that rocks.");
    CodeParameter parameter = method.getParameters().add("value");
    parameter.setType(typeVariable);
    parameter.getDoc().getLines().add("the value to convert.");
    BaseType classNested21 = interfacetypeNested2.getNestedTypes().add(simpleNameNested21);
    BaseType annotationNested211 = classNested21.getNestedTypes().add(simpleNameNested211);
    annotationNested211.setCategory(CodeTypeCategory.ANNOTATION);
    BaseType enumNested212 = classNested21.getNestedTypes().add(simpleNameNested212);
    enumNested212.setCategory(CodeTypeCategory.ENUMERAION);
    addDummyDoc(classTop);

    // then
    assertThat(classTop.getSimpleName()).isEqualTo(simpleNameTop);
    assertThat(classStaticNested1.getSimpleName()).isEqualTo(simpleNameNested1);
    assertThat(interfacetypeNested2.getSimpleName()).isEqualTo(simpleNameNested2);
    assertThat(classNested21.getSimpleName()).isEqualTo(simpleNameNested21);
    assertThat(annotationNested211.getSimpleName()).isEqualTo(simpleNameNested211);
    assertThat(enumNested212.getSimpleName()).isEqualTo(simpleNameNested212);
    assertThat((List<BaseType>) classTop.getNestedTypes().getDeclared()).containsExactly(classStaticNested1, interfacetypeNested2);
    assertThat((List<BaseType>) interfacetypeNested2.getNestedTypes().getDeclared()).containsExactly(classNested21);
    assertThat((List<BaseType>) classNested21.getNestedTypes().getDeclared()).containsExactly(annotationNested211, enumNested212);
    assertThat(classTop.getFile().toString()).isEqualTo("ClassToplevel<T>");
    assertThat(classTop.getFile().getSourceCode()).isEqualTo("package mydomain;\n" + //
        "\n" + //
        "/**\n" + //
        " * Doc for {@link ClassToplevel}.\n" + //
        " * @param <T> generic type\n" + //
        " */\n" + //
        "public class ClassToplevel<T> {\n" + //
        "\n" + //
        "  /** Doc for {@link StaticClassNested1}. */\n" + //
        "  public static class StaticClassNested1 {\n" + //
        "  }\n" + //
        "\n" + //
        "  /** Doc for {@link InterfaceClassNested2}. */\n" + //
        "  public interface InterfaceClassNested2 {\n" + //
        "\n" + //
        "    /**\n" + //
        "     * This method rocks.\n" + //
        "     * @param <V> generic value\n" + //
        "     * @param value the value to convert.\n" + //
        "     * @return the converted value that rocks.\n" + //
        "     */\n" + //
        "    <V> V myMethod(V value);\n" + //
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
    for (CodeType child : type.getNestedTypes().getDeclared()) {
      addDummyDoc(child);
    }
  }

  /**
   * Test of {@link BaseType#getSuperTypes()}.
   */
  @Test
  public void testSuperTypes() {

    // given
    BaseContext context = createContext();
    BasePackage rootPackage = context.getSource().getRootPackage();
    String pkgName1 = "pkg1";
    BasePackage pkg1 = rootPackage.getChildren().createPackage(pkgName1);
    String pkgName2 = "pkg2";
    BasePackage pkg2 = rootPackage.getChildren().createPackage(pkgName2);
    BasePathElements children1 = pkg1.getChildren();
    BasePathElements children2 = pkg2.getChildren();

    // when
    BaseType interface1Other = children1.createType("Other");
    BaseType interface2Bar = children1.createType("Bar");
    BaseType interface3Some = children2.createType("Some");
    BaseType interface4Foo = children2.createType("Foo");
    BaseType class1Other = children1.createType("OtherClass");
    BaseType class2Foo = children2.createType("FooClass");
    interface3Some.getSuperTypes().add(interface2Bar);
    interface4Foo.getSuperTypes().add(interface3Some);
    interface4Foo.getSuperTypes().add(interface1Other);
    class1Other.getSuperTypes().add(interface1Other);
    class1Other.getSuperTypes().add(interface2Bar);
    class2Foo.getSuperTypes().add(class1Other);
    class2Foo.getSuperTypes().add(interface4Foo);

    // then
    assertThat(getAllSuperTypesAsList(class2Foo)).containsExactly(class1Other, interface1Other, interface2Bar, interface4Foo, interface3Some);
  }

  private List<CodeGenericType> getAllSuperTypesAsList(BaseType class2Foo) {

    List<CodeGenericType> superTypeList = new ArrayList<>();
    for (CodeGenericType superType : class2Foo.getSuperTypes().getAll()) {
      superTypeList.add(superType);
    }
    return superTypeList;
  }

}
