/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import net.sf.mmm.code.api.CodeTypeCategory;
import net.sf.mmm.code.api.modifier.CodeModifiers;

/**
 * Test of {@link JavaType}.
 */
public class JavaTypeTest extends Assertions {

  @Test
  public void test() {

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
    assertThat(type.getCategory()).isEqualTo(CodeTypeCategory.CLASS);
    assertThat(type.getModifiers()).isEqualTo(CodeModifiers.MODIFIERS_PUBLIC);
    assertThat(type.getDoc()).isNotNull();
    assertThat(type.getDoc().isEmpty()).isTrue();
    assertThat(type.getAnnotations()).isEmpty();
    assertThat(type.getFields()).isEmpty();
    assertThat(type.getMethods()).isEmpty();
    assertThat(type.getConstructors()).isEmpty();
    assertThat(type.getFile().toString()).isEqualTo("package mydomain;\n" + //
        "public class MyClass {\n" + //
        "}\n");
  }

}
