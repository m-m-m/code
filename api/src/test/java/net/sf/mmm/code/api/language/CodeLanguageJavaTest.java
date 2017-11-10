/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.language;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;

import net.sf.mmm.code.api.CodePackage;
import net.sf.mmm.code.api.type.CodeTypeCategory;

/**
 * Test of {@link CodeLanguageJava}.
 */
public class CodeLanguageJavaTest extends Assertions {

  private static final String LETTERS = "ĀƀɐʰΞЀԀԱހݐܐא";

  private static final String DIGITS = "6६\u0cec";

  private static final String NON_ALPHANUMERIC_SYMBOLS = "⊈⏭";

  static final String DIGITS_NOT_SUPPORTED_BY_JAVA_8 = "௰Ⅹ\ud835\udfe8";

  /**
   * Tests the basics of {@link CodeLanguageJava}.
   */
  @Test
  public void testBasics() {

    // given
    CodeLanguage language = CodeLanguageJava.INSTANCE;

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
  }

  /**
   * Tests of
   * {@link CodeLanguageJava#verifySimpleName(net.sf.mmm.code.api.item.CodeItemWithQualifiedName, String)} for
   * {@link CodePackage}.
   */
  @Test
  public void testPackageNaming() {

    // given
    CodeLanguage language = CodeLanguageJava.INSTANCE;

    // when
    CodePackage pkgRoot = Mockito.mock(CodePackage.class);
    CodePackage pkg = Mockito.mock(CodePackage.class);
    Mockito.when(pkg.getParentPackage()).thenReturn(pkgRoot);

    // then
    // valid package names
    verifyPackageName(language, pkg, "pkg1", true);
    verifyPackageName(language, pkg, "m_m_m", true);
    verifyPackageName(language, pkg, "$_1foo2_$", true);
    verifyPackageName(language, pkg, "DiscouragedButAllowed", true);
    // never do such nonsense or you might end up with something like this:
    // Error: Could not find or load main class net.sf.mmm.code.api.item.äöüß?????.MyClass
    verifyPackageName(language, pkg, "äöüßเ", true); // never do such
    char[] name = new char[2];
    for (char letter : LETTERS.toCharArray()) {
      name[0] = letter;
      for (char digit : DIGITS.toCharArray()) {
        name[1] = digit;
        verifyPackageName(language, pkg, new String(name), true);
      }
    }
    // invalid package names
    verifyPackageName(language, pkg, "1", false);
    verifyPackageName(language, pkg, "0a", false);
    verifyPackageName(language, pkg, "a-b", false);
    verifyPackageName(language, pkg, "a.b", false);
    verifyPackageName(language, pkg, "a&b", false);
    verifyPackageName(language, pkg, "a%b", false);
    verifyPackageName(language, pkg, "a§b", false);
    verifyPackageName(language, pkg, "a௰", false);
    for (int nonAlphaNum = 0; nonAlphaNum < NON_ALPHANUMERIC_SYMBOLS.length(); nonAlphaNum++) {
      char c = NON_ALPHANUMERIC_SYMBOLS.charAt(nonAlphaNum);
      assertThat(Character.isLetterOrDigit(c)).as("isLetterOrDigit(" + name[0] + ") expected to be false").isFalse();
      verifyPackageName(language, pkg, Character.toString(c), false);
    }
  }

  private void verifyPackageName(CodeLanguage language, CodePackage pkg, String simpleName, boolean valid) {

    try {
      String name = language.verifySimpleName(pkg, simpleName);
      if (valid) {
        assertThat(name).isEqualTo(simpleName);
      } else {
        failBecauseExceptionWasNotThrown(RuntimeException.class);
      }
    } catch (RuntimeException e) {
      if (valid) {
        fail("Simple name '" + simpleName + "' was expected to be a valid for package.", e);
      }
    }
  }

}
