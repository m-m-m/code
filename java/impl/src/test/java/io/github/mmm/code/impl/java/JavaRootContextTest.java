/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.impl.java;

import java.io.Serializable;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.github.mmm.code.api.language.CodeLanguage;
import io.github.mmm.code.api.member.CodeField;
import io.github.mmm.code.api.member.CodeProperties;
import io.github.mmm.code.api.member.CodeProperty;
import io.github.mmm.code.api.type.CodeGenericType;
import io.github.mmm.code.api.type.CodeGenericTypeParameters;
import io.github.mmm.code.api.type.CodeSuperTypes;
import io.github.mmm.code.api.type.CodeType;
import io.github.mmm.code.api.type.CodeTypeCategory;
import io.github.mmm.code.api.type.CodeTypeVariable;
import io.github.mmm.code.api.type.CodeTypeVariables;
import io.github.mmm.code.base.BasePackage;
import io.github.mmm.code.base.source.BaseSource;
import io.github.mmm.code.base.statement.BaseLocalVariable;
import io.github.mmm.code.base.type.BaseGenericType;
import io.github.mmm.code.base.type.BaseParameterizedType;

/**
 * Test of {@link JavaRootContext}.
 */
public class JavaRootContextTest extends AbstractBaseTypeTest {

  private JavaRootContext getContext() {

    return JavaRootContext.get();
  }

  /**
   * Test the very basic methods of {@link JavaContext}.
   */
  @Test
  public void testBasics() {

    // arrange
    JavaContext context = getContext();

    // assert
    assertThat(context.getRootContext()).isSameAs(context);
    BasePackage rootPackage = context.getSource().getRootPackage();
    assertThat(rootPackage).isNotNull();
    assertThat(rootPackage.isRoot()).isTrue();
    assertThat(rootPackage.getSimpleName()).isEmpty();
    assertThat(rootPackage.getQualifiedName()).isEmpty();
    assertThat(rootPackage.getParentPackage()).isNull();
    assertThat(context.getQualifiedNameForStandardType("byte", true)).isEqualTo("byte");
    assertThat(context.getQualifiedNameForStandardType("String", true)).isEqualTo("String");
    assertThat(context.getQualifiedNameForStandardType("String", false)).isEqualTo("java.lang.String");
    assertThat(context.getQualifiedNameForStandardType("UndefinedBanana", false)).isNull();
  }

  /**
   * Test of {@link JavaContext#getLanguage()}.
   */
  @Test
  public void testLanguage() {

    // arrange
    JavaContext context = getContext();
    BaseSource source = context.getSource();
    CodeLanguage language = context.getLanguage();

    // assert
    assertThat(language.getPackageSeparator()).isEqualTo('.');
    assertThat(language.getLanguageName()).isEqualTo("Java");
    assertThat(language.getKeywordForExtends()).isEqualTo(" extends ");
    assertThat(language.getKeywordForImplements()).isEqualTo(" implements ");
    assertThat(language.getStatementTerminator()).isEqualTo(";");
    assertThat(language.getAnnotationStart()).isEqualTo("@");
    assertThat(language.getAnnotationEndIfEmpty()).isEmpty();
    assertThat(language.getVariableNameThis()).isEqualTo("this");
    assertThat(language.getMethodKeyword()).isEmpty();
    assertThat(language.getKeywordForCategory(CodeTypeCategory.CLASS)).isEqualTo("class");
    assertThat(language.getKeywordForCategory(CodeTypeCategory.INTERFACE)).isEqualTo("interface");
    assertThat(language.getKeywordForCategory(CodeTypeCategory.ENUMERAION)).isEqualTo("enum");
    assertThat(language.getKeywordForCategory(CodeTypeCategory.ANNOTATION)).isEqualTo("@interface");
    assertThat(language.getKeywordForVariable(new BaseLocalVariable(source, "foo", context.getRootType(), null, false)))
        .isEmpty();
    assertThat(language.getKeywordForVariable(new BaseLocalVariable(source, "foo", context.getRootType(), null, true)))
        .isEqualTo("final ");
  }

  /**
   * Test of {@link JavaContext#getRootType()}
   */
  @Test
  public void testObject() {

    // arrange
    JavaContext context = getContext();

    // act
    CodeType object = context.getRootType();

    // assert
    verifyClass(object, Object.class, context);
  }

  /**
   * Test of {@link JavaContext#getRootExceptionType()}
   */
  @Test
  public void testThrowable() {

    // arrange
    JavaContext context = getContext();

    // act
    CodeType throwable = context.getRootExceptionType();

    // assert
    verifyClass(throwable, Throwable.class, context);
  }

  /**
   * Test of {@link JavaContext#getRootEnumerationType()}.
   */
  @Test
  public void testEnum() {

    // arrange
    JavaContext context = getContext();

    // act
    CodeType enumeration = context.getRootEnumerationType();

    // assert
    verifyClass(enumeration, Enum.class, context);
    CodeSuperTypes superTypes = enumeration.getSuperTypes();
    assertThat(superTypes.getSuperClass()).isSameAs(context.getRootType());
    // test "Enum implements Comparable<E>, Serializable"
    @SuppressWarnings("unchecked")
    List<CodeGenericType> superInterfaces = (List<CodeGenericType>) superTypes.getSuperInterfaces();
    BaseGenericType serializable = context.getType(Serializable.class);
    assertThat(superInterfaces).hasSize(3).contains(serializable);
    CodeGenericType comparable = superInterfaces.get(1);
    if (comparable == serializable) {
      comparable = superInterfaces.get(2);
    }
    assertThat(comparable).isNotNull().isInstanceOf(BaseParameterizedType.class);

    // test "Enum<E extends Enum<E>>" - great test with its recursive declaration
    CodeTypeVariables typeVariables = enumeration.getTypeParameters();
    List<? extends CodeTypeVariable> typeVariableList = typeVariables.getDeclared();
    assertThat(typeVariableList).hasSize(1);
    CodeTypeVariable typeVariable = typeVariableList.get(0);
    assertThat(typeVariable.getName()).isEqualTo("E");
    assertThat(typeVariable.asType()).isSameAs(enumeration);
    assertThat(typeVariables.get("E")).isSameAs(typeVariable);
    assertThat(typeVariable.isSuper()).isFalse();
    assertThat(typeVariable.isExtends()).isTrue();
    assertThat(typeVariable.isWildcard()).isFalse();
    CodeGenericType bound = typeVariable.getBound();
    assertThat(bound).isNotNull().isInstanceOf(BaseParameterizedType.class);
    CodeGenericTypeParameters<?> typeParameters = bound.getTypeParameters();
    List<? extends CodeGenericType> typeParameterList = typeParameters.getDeclared();
    assertThat(typeParameterList).hasSize(1);
    CodeGenericType boundTypeVariable = typeParameterList.get(0);
    assertThat(boundTypeVariable).isSameAs(typeVariable);
  }

  /**
   * Test of {@link CodeType#getProperties() properties}.
   */
  @Test
  public void testProperties() {

    // arrange
    JavaContext context = getContext();

    // act
    CodeType yearMonth = context.getType("java.time.YearMonth");

    // assert
    CodeProperties properties = yearMonth.getProperties();
    assertThat(properties.getDeclared().size()).isGreaterThanOrEqualTo(2);
    checkProperty(properties, "year", int.class);
    checkProperty(properties, "month", int.class);
  }

  /**
   * Test of {@link JavaContext#getClassLoader()}.
   */
  @Test
  public void testGetClassLoader() {

    // arrange
    JavaContext context = getContext();

    // act
    ClassLoader classLoader = context.getClassLoader();

    // assert
    assertThat(classLoader).isSameAs(ClassLoader.getSystemClassLoader());
  }

  private void checkProperty(CodeProperties properties, String name, Class<?> type) {

    CodeProperty property = properties.get(name);
    assertThat(property.isImmutable()).isTrue();
    assertThat(property).as(name).isNotNull();
    assertThat(property.getName()).isEqualTo(name);
    CodeField field = property.getField();
    assertThat(field).isNotNull();
    assertThat(field.getType().getQualifiedName()).isEqualTo(type.getName());
  }

}
