/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.io.Serializable;
import java.util.List;

import org.junit.Test;

import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeGenericTypeParameters;
import net.sf.mmm.code.api.type.CodeSuperTypes;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.api.type.CodeTypeCategory;
import net.sf.mmm.code.api.type.CodeTypeVariable;
import net.sf.mmm.code.api.type.CodeTypeVariables;
import net.sf.mmm.code.base.BasePackage;
import net.sf.mmm.code.base.source.BaseSource;
import net.sf.mmm.code.base.statement.BaseLocalVariable;
import net.sf.mmm.code.base.type.BaseGenericType;
import net.sf.mmm.code.base.type.BaseParameterizedType;

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

    // given
    JavaContext context = getContext();

    // then
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

    // given
    JavaContext context = getContext();
    BaseSource source = context.getSource();
    CodeLanguage language = context.getLanguage();

    // then
    assertThat(language.getPackageSeparator()).isEqualTo('.');
    assertThat(language.getLanguageName()).isEqualTo("Java");
    assertThat(language.getKeywordForExtends()).isEqualTo(" extends ");
    assertThat(language.getKeywordForImplements()).isEqualTo(" implements ");
    assertThat(language.getStatementTerminator()).isEqualTo(";");
    assertThat(language.getAnnotationStart()).isEqualTo("@");
    assertThat(language.getAnnotationEndIfEmpty()).isEmpty();
    assertThat(language.getVariableNameThis()).isEqualTo("this");
    assertThat(language.getMethodKeyword()).isEmpty();
    assertThat(language.getMethodReturnStart()).isEmpty();
    assertThat(language.getMethodReturnEnd()).isNull();
    assertThat(language.getKeywordForCategory(CodeTypeCategory.CLASS)).isEqualTo("class");
    assertThat(language.getKeywordForCategory(CodeTypeCategory.INTERFACE)).isEqualTo("interface");
    assertThat(language.getKeywordForCategory(CodeTypeCategory.ENUMERAION)).isEqualTo("enum");
    assertThat(language.getKeywordForCategory(CodeTypeCategory.ANNOTATION)).isEqualTo("@interface");
    assertThat(language.getKeywordForVariable(new BaseLocalVariable(source, "foo", context.getRootType(), null, false))).isEmpty();
    assertThat(language.getKeywordForVariable(new BaseLocalVariable(source, "foo", context.getRootType(), null, true))).isEqualTo("final ");
  }

  /**
   * Test of {@link JavaContext#getRootType()}
   */
  @Test
  public void testObject() {

    // given
    JavaContext context = getContext();

    // when
    CodeType object = context.getRootType();

    // then
    verifyClass(object, Object.class, context);
  }

  /**
   * Test of {@link JavaContext#getRootExceptionType()}
   */
  @Test
  public void testThrowable() {

    // given
    JavaContext context = getContext();

    // when
    CodeType throwable = context.getRootExceptionType();

    // then
    verifyClass(throwable, Throwable.class, context);
  }

  /**
   * Test of {@link JavaContext#getRootEnumerationType()}.
   */
  @Test
  public void testEnum() {

    // given
    JavaContext context = getContext();

    // when
    CodeType enumeration = context.getRootEnumerationType();

    // then
    verifyClass(enumeration, Enum.class, context);
    CodeSuperTypes superTypes = enumeration.getSuperTypes();
    assertThat(superTypes.getSuperClass()).isSameAs(context.getRootType());
    // test "Enum implements Comparable<E>, Serializable"
    @SuppressWarnings("unchecked")
    List<CodeGenericType> superInterfaces = (List<CodeGenericType>) superTypes.getSuperInterfaces();
    BaseGenericType serializable = context.getType(Serializable.class);
    assertThat(superInterfaces).hasSize(2).contains(serializable);
    CodeGenericType comparable = superInterfaces.get(0);
    if (comparable == serializable) {
      comparable = superInterfaces.get(1);
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

}
