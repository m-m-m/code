package io.github.mmm.code.api.modifier;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link CodeModifiers}.
 */
class CodeModifiersTest extends Assertions {

  /**
   * Test of {@link CodeModifiers#MODIFIERS_PRIVATE_STATIC_FINAL}.
   */
  @Test
  void testPrivateStaticFinal() {

    // arrange
    CodeModifiers modifiers = CodeModifiers.MODIFIERS_PRIVATE_STATIC_FINAL;

    // act + assert
    assertThat(modifiers.isAbstract()).isFalse();
    assertThat(modifiers.isDefaultModifier()).isFalse();
    assertThat(modifiers.isFinal()).isTrue();
    assertThat(modifiers.isStatic()).isTrue();

    assertThat(modifiers.getVisibility()).isSameAs(CodeVisibility.PRIVATE);
    assertThat(modifiers.isPrivate()).isTrue();
    assertThat(modifiers.isPublic()).isFalse();
    assertThat(modifiers.isProtected()).isFalse();
    assertThat(modifiers.isDefaultVisibility()).isFalse();

    assertThat(modifiers.toString()).isEqualTo("private static final ");
  }

  /**
   * Test of {@link CodeModifiers#MODIFIERS_PUBLIC_STATIC_FINAL}.
   */
  @Test
  void testPublicStaticFinal() {

    // arrange
    CodeModifiers modifiers = CodeModifiers.MODIFIERS_PUBLIC_STATIC_FINAL;

    // act + assert
    assertThat(modifiers.isAbstract()).isFalse();
    assertThat(modifiers.isDefaultModifier()).isFalse();
    assertThat(modifiers.isFinal()).isTrue();
    assertThat(modifiers.isStatic()).isTrue();

    assertThat(modifiers.getVisibility()).isSameAs(CodeVisibility.PUBLIC);
    assertThat(modifiers.isPrivate()).isFalse();
    assertThat(modifiers.isPublic()).isTrue();
    assertThat(modifiers.isProtected()).isFalse();
    assertThat(modifiers.isDefaultVisibility()).isFalse();
    assertThat(modifiers.toString()).isEqualTo("public static final ");
    assertThat(modifiers.changeVisibility(CodeVisibility.PRIVATE))
        .isEqualTo(CodeModifiers.MODIFIERS_PUBLIC_STATIC_FINAL);
    assertThat(modifiers.changeVisibility(CodeVisibility.DEFAULT).toString()).isEqualTo("static final ");
  }

  /**
   * Test of {@link CodeModifiers#MODIFIERS_PROTECTED_STATIC_FINAL}.
   */
  @Test
  void testProtectedStaticFinal() {

    // arrange
    CodeModifiers modifiers = CodeModifiers.MODIFIERS_PROTECTED_STATIC_FINAL;

    // act + assert
    assertThat(modifiers.isAbstract()).isFalse();
    assertThat(modifiers.isDefaultModifier()).isFalse();
    assertThat(modifiers.isFinal()).isTrue();
    assertThat(modifiers.isStatic()).isTrue();

    assertThat(modifiers.getVisibility()).isSameAs(CodeVisibility.PROTECTED);
    assertThat(modifiers.isPrivate()).isFalse();
    assertThat(modifiers.isPublic()).isFalse();
    assertThat(modifiers.isProtected()).isTrue();
    assertThat(modifiers.isDefaultVisibility()).isFalse();
    assertThat(modifiers.toString()).isEqualTo("protected static final ");
  }

  /**
   * Test of {@link CodeModifiers#MODIFIERS_PUBLIC_DEFAULT}.
   */
  @Test
  void testPublicDefault() {

    // arrange
    CodeModifiers modifiers = CodeModifiers.MODIFIERS_PUBLIC_DEFAULT;

    // act + assert
    assertThat(modifiers.isAbstract()).isFalse();
    assertThat(modifiers.isDefaultModifier()).isTrue();
    assertThat(modifiers.isFinal()).isFalse();
    assertThat(modifiers.isStatic()).isFalse();

    assertThat(modifiers.getVisibility()).isSameAs(CodeVisibility.PUBLIC);
    assertThat(modifiers.isPrivate()).isFalse();
    assertThat(modifiers.isPublic()).isTrue();
    assertThat(modifiers.isProtected()).isFalse();
    assertThat(modifiers.isDefaultVisibility()).isFalse();
    assertThat(modifiers.toString()).isEqualTo("public default ");
  }

  /**
   * Test of {@link CodeModifiers#MODIFIERS_PUBLIC_ABSTRACT}.
   */
  @Test
  void testPublicAbstract() {

    // arrange
    CodeModifiers modifiers = CodeModifiers.MODIFIERS_PUBLIC_ABSTRACT;

    // act + assert
    assertThat(modifiers.isAbstract()).isTrue();
    assertThat(modifiers.isDefaultModifier()).isFalse();
    assertThat(modifiers.isFinal()).isFalse();
    assertThat(modifiers.isStatic()).isFalse();

    assertThat(modifiers.getVisibility()).isSameAs(CodeVisibility.PUBLIC);
    assertThat(modifiers.isPrivate()).isFalse();
    assertThat(modifiers.isPublic()).isTrue();
    assertThat(modifiers.isProtected()).isFalse();
    assertThat(modifiers.isDefaultVisibility()).isFalse();
    assertThat(modifiers.toString()).isEqualTo("public abstract ");
  }

  /**
   * Test of {@link CodeModifiers#MODIFIERS_PRIVATE_ABSTRACT}.
   */
  @Test
  void testPrivateAbstract() {

    // arrange
    CodeModifiers modifiers = CodeModifiers.MODIFIERS_PRIVATE_ABSTRACT;

    // act + assert
    assertThat(modifiers.isAbstract()).isTrue();
    assertThat(modifiers.isDefaultModifier()).isFalse();
    assertThat(modifiers.isFinal()).isFalse();
    assertThat(modifiers.isStatic()).isFalse();

    assertThat(modifiers.getVisibility()).isSameAs(CodeVisibility.PRIVATE);
    assertThat(modifiers.isPrivate()).isTrue();
    assertThat(modifiers.isPublic()).isFalse();
    assertThat(modifiers.isProtected()).isFalse();
    assertThat(modifiers.isDefaultVisibility()).isFalse();
    assertThat(modifiers.toString()).isEqualTo("private abstract ");
  }

  /**
   * Test of {@link CodeModifiers#MODIFIERS_PROTECTED_ABSTRACT}.
   */
  @Test
  void testProtectedAbstract() {

    // arrange
    CodeModifiers modifiers = CodeModifiers.MODIFIERS_PROTECTED_ABSTRACT;

    // act + assert
    assertThat(modifiers.isAbstract()).isTrue();
    assertThat(modifiers.isDefaultModifier()).isFalse();
    assertThat(modifiers.isFinal()).isFalse();
    assertThat(modifiers.isStatic()).isFalse();

    assertThat(modifiers.getVisibility()).isSameAs(CodeVisibility.PROTECTED);
    assertThat(modifiers.isPrivate()).isFalse();
    assertThat(modifiers.isPublic()).isFalse();
    assertThat(modifiers.isProtected()).isTrue();
    assertThat(modifiers.isDefaultVisibility()).isFalse();
    assertThat(modifiers.toString()).isEqualTo("protected abstract ");
  }

}
