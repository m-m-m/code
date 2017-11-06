/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.io.Serializable;
import java.util.List;

import org.junit.Test;

import net.sf.mmm.code.api.syntax.CodeSyntax;
import net.sf.mmm.code.api.type.CodeTypeCategory;
import net.sf.mmm.code.base.BasePackage;
import net.sf.mmm.code.base.statement.BaseLocalVariable;
import net.sf.mmm.code.base.type.BaseGenericType;
import net.sf.mmm.code.base.type.BaseGenericTypeParameters;
import net.sf.mmm.code.base.type.BaseParameterizedType;
import net.sf.mmm.code.base.type.BaseSuperTypes;
import net.sf.mmm.code.base.type.BaseType;
import net.sf.mmm.code.base.type.BaseTypeVariable;
import net.sf.mmm.code.base.type.BaseTypeVariables;

/**
 * Test of {@link JavaRootContext}.
 */
public class JavaRootContextTest extends JavaTypeTest {

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
   * Test of {@link JavaContext#getSyntax()}.
   */
  @Test
  public void testSyntax() {

    // given
    JavaContext context = getContext();
    CodeSyntax syntax = context.getSyntax();

    // then
    assertThat(syntax.getPackageSeparator()).isEqualTo('.');
    assertThat(syntax.getLanguageName()).isEqualTo("Java");
    assertThat(syntax.getKeywordForExtends()).isEqualTo(" extends ");
    assertThat(syntax.getKeywordForImplements()).isEqualTo(" implements ");
    assertThat(syntax.getStatementTerminator()).isEqualTo(";");
    assertThat(syntax.getAnnotationStart()).isEqualTo("@");
    assertThat(syntax.getAnnotationEndIfEmpty()).isEmpty();
    assertThat(syntax.getVariableNameThis()).isEqualTo("this");
    assertThat(syntax.getMethodKeyword()).isEmpty();
    assertThat(syntax.getMethodReturnStart()).isEmpty();
    assertThat(syntax.getMethodReturnEnd()).isNull();
    assertThat(syntax.getKeywordForCategory(CodeTypeCategory.CLASS)).isEqualTo("class");
    assertThat(syntax.getKeywordForCategory(CodeTypeCategory.INTERFACE)).isEqualTo("interface");
    assertThat(syntax.getKeywordForCategory(CodeTypeCategory.ENUMERAION)).isEqualTo("enum");
    assertThat(syntax.getKeywordForCategory(CodeTypeCategory.ANNOTATION)).isEqualTo("@interface");
    assertThat(syntax.getKeywordForVariable(new BaseLocalVariable("foo", context.getRootType(), null, false))).isEmpty();
    assertThat(syntax.getKeywordForVariable(new BaseLocalVariable("foo", context.getRootType(), null, true))).isEqualTo("final ");
  }

  /**
   * Test of {@link JavaContext#getRootType()}
   */
  @Test
  public void testObject() {

    // given
    JavaContext context = getContext();

    // when
    BaseType object = context.getRootType();

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
    BaseType throwable = context.getRootExceptionType();

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
    BaseType enumeration = context.getRootEnumerationType();

    // then
    verifyClass(enumeration, Enum.class, context);
    BaseSuperTypes superTypes = enumeration.getSuperTypes();
    assertThat(superTypes.getSuperClass()).isSameAs(context.getRootType());
    // test "Enum implements Comparable<E>, Serializable"
    @SuppressWarnings("unchecked")
    List<BaseGenericType> superInterfaces = (List<BaseGenericType>) superTypes.getSuperInterfaces();
    BaseGenericType serializable = context.getType(Serializable.class);
    assertThat(superInterfaces).hasSize(2).contains(serializable);
    BaseGenericType comparable = superInterfaces.get(0);
    if (comparable == serializable) {
      comparable = superInterfaces.get(1);
    }
    assertThat(comparable).isNotNull().isInstanceOf(BaseParameterizedType.class);

    // test "Enum<E extends Enum<E>>" - great test with its recursive declaration
    BaseTypeVariables typeVariables = enumeration.getTypeParameters();
    List<? extends BaseTypeVariable> typeVariableList = typeVariables.getDeclared();
    assertThat(typeVariableList).hasSize(1);
    BaseTypeVariable typeVariable = typeVariableList.get(0);
    assertThat(typeVariable.getName()).isEqualTo("E");
    assertThat(typeVariable.asType()).isSameAs(enumeration);
    assertThat(typeVariables.get("E")).isSameAs(typeVariable);
    assertThat(typeVariable.isSuper()).isFalse();
    assertThat(typeVariable.isExtends()).isTrue();
    assertThat(typeVariable.isWildcard()).isFalse();
    BaseGenericType bound = typeVariable.getBound();
    assertThat(bound).isNotNull().isInstanceOf(BaseParameterizedType.class);
    BaseGenericTypeParameters<?> typeParameters = bound.getTypeParameters();
    List<? extends BaseGenericType> typeParameterList = typeParameters.getDeclared();
    assertThat(typeParameterList).hasSize(1);
    BaseGenericType boundTypeVariable = typeParameterList.get(0);
    assertThat(boundTypeVariable).isSameAs(typeVariable);
  }

}
