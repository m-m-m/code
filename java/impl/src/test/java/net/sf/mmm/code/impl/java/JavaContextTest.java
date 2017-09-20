/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Test of {@link JavaContext}.
 */
public class JavaContextTest extends Assertions {

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
    assertThat(context.getPackageSeparator()).isEqualTo('.');
    assertThat(context.getRootContext()).isSameAs(context);
    JavaPackage rootPackage = context.getRootPackage();
    assertThat(rootPackage).isNotNull();
    assertThat(rootPackage.getSimpleName()).isEmpty();
    assertThat(rootPackage.getQualifiedName()).isEmpty();
    assertThat(rootPackage.getParentPackage()).isNull();
    assertThat(context.getPackage("")).isSameAs(rootPackage);
    assertThat(context.getQualifiedNameForStandardType("byte", true)).isEqualTo("byte");
    assertThat(context.getQualifiedNameForStandardType("String", true)).isEqualTo("String");
    assertThat(context.getQualifiedNameForStandardType("String", false)).isEqualTo("java.lang.String");
    assertThat(context.getQualifiedNameForStandardType("UndefinedBanana", false)).isNull();
  }

  /**
   * Test of {@link JavaContext#getRootType()}
   */
  @Test
  public void testObject() {

    // given
    JavaContext context = getContext();

    // when
    JavaType object = context.getRootType();

    // then
    assertThat(object).isNotNull();
    assertThat(object.getSimpleName()).isEqualTo(Object.class.getSimpleName());
    assertThat(object.getQualifiedName()).isEqualTo(Object.class.getName());
  }

}
