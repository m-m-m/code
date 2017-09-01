/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * Test of {@link JavaContext}.
 */
public class JavaContextTest extends Assertions {

  /**
   * Test the very basic methods of {@link JavaContext}.
   */
  @Test
  public void testBasics() {

    // given
    JavaContext context = new JavaContext();

    // then
    assertThat(context.getNewline()).isEqualTo("\n");
    assertThat(context.getPackageSeparator()).isEqualTo('.');
    assertThat(context.getDocDescriptors()).isEmpty();
    JavaPackage rootPackage = context.getRootPackage();
    assertThat(rootPackage).isNotNull();
    assertThat(rootPackage.getSimpleName()).isEmpty();
    assertThat(rootPackage.getQualifiedName()).isEmpty();
    assertThat(rootPackage.getParentPackage()).isNull();
    assertThat(context.getPackage("")).isSameAs(rootPackage);
  }

}
