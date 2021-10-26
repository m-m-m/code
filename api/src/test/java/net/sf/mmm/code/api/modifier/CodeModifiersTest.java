package net.sf.mmm.code.api.modifier;

import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * Test of {@link CodeModifiers}.
 */
public class CodeModifiersTest extends Assertions {

  /**
   * Test of {@link CodeModifiers#MODIFIERS_PRIVATE_STATIC_FINAL}.
   */
  @Test
  public void testPrivateStaticFinal() {

    // given
    CodeModifiers modifiers = CodeModifiers.MODIFIERS_PRIVATE_STATIC_FINAL;

    // when + then
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
  public void testPublicStaticFinal() {

    // given
    CodeModifiers modifiers = CodeModifiers.MODIFIERS_PUBLIC_STATIC_FINAL;

    // when + then
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
  public void testProtectedStaticFinal() {

    // given
    CodeModifiers modifiers = CodeModifiers.MODIFIERS_PROTECTED_STATIC_FINAL;

    // when + then
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
  public void testPublicDefault() {

    // given
    CodeModifiers modifiers = CodeModifiers.MODIFIERS_PUBLIC_DEFAULT;

    // when + then
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
  public void testPublicAbstract() {

    // given
    CodeModifiers modifiers = CodeModifiers.MODIFIERS_PUBLIC_ABSTRACT;

    // when + then
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
  public void testPrivateAbstract() {

    // given
    CodeModifiers modifiers = CodeModifiers.MODIFIERS_PRIVATE_ABSTRACT;

    // when + then
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
  public void testProtectedAbstract() {

    // given
    CodeModifiers modifiers = CodeModifiers.MODIFIERS_PROTECTED_ABSTRACT;

    // when + then
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
