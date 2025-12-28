/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.language;

import java.nio.charset.Charset;
import java.nio.file.Path;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.mmm.code.api.CodeContext;
import io.github.mmm.code.api.CodePackage;
import io.github.mmm.code.api.CodePathElements;
import io.github.mmm.code.api.annotation.CodeAnnotations;
import io.github.mmm.code.api.comment.CodeComment;
import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.doc.CodeDoc;
import io.github.mmm.code.api.node.CodeContainer;
import io.github.mmm.code.api.source.CodeSource;
import io.github.mmm.code.api.type.CodeTypeCategory;

/**
 * Test of {@link JavaLanguage}.
 */
public class JavaLanguageTest extends Assertions implements TestValues {

  static final String DIGITS_NOT_SUPPORTED_BY_JAVA_8 = "௰Ⅹ\ud835\udfe8";

  /**
   * Tests the basics of {@link JavaLanguage}.
   */
  @Test
  public void testBasics() {

    // arrange
    CodeLanguage language = JavaLanguage.get();

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
    assertThat(language.getKeywordForCategory(CodeTypeCategory.RECORD)).isEqualTo("record");
  }

  /**
   * Tests of {@link JavaLanguage#verifySimpleName(io.github.mmm.code.api.item.CodeItemWithQualifiedName, String)} for
   * {@link CodePackage}.
   */
  @Test
  public void testPackageNaming() {

    // arrange
    CodeLanguage language = JavaLanguage.get();

    // act
    CodePackage pkgRoot = new CodePackageMock("com");
    CodePackage pkg = new CodePackageMock(pkgRoot, "foo");
    // Mockito.when(pkg.getParentPackage()).thenReturn(pkgRoot);

    // assert
    // valid package names
    verifyPackageName(language, pkg, "pkg1", true);
    verifyPackageName(language, pkg, "m_m_m", true);
    verifyPackageName(language, pkg, "$_1foo2_$", true);
    verifyPackageName(language, pkg, "DiscouragedButAllowed", true);
    // never do such nonsense or you might end up with something like this:
    // Error: Could not find or load main class net.sf.mmm.code.api.item.äöüß?????.MyClass
    verifyPackageName(language, pkg, "äöüßเ", true); // never do such
    char[] name = new char[2];
    for (char letter : UNICODE_LETTERS.toCharArray()) {
      name[0] = letter;
      for (char digit : UNICODE_DIGITS.toCharArray()) {
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
    for (int nonAlphaNum = 0; nonAlphaNum < UNICODE_NON_ALPHANUMERIC_SYMBOLS.length(); nonAlphaNum++) {
      if (nonAlphaNum == 2) {
        continue;
      }
      char c = UNICODE_NON_ALPHANUMERIC_SYMBOLS.charAt(nonAlphaNum);
      assertThat(Character.isLetterOrDigit(c))
          .as("isLetterOrDigit(" + c + ") [index " + nonAlphaNum + "] expected to be false").isFalse();
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

  private class CodePackageMock implements CodePackage {

    private CodePackage parent;

    private String simpleName;

    CodePackageMock(String simpleName) {

      this(null, simpleName);
    }

    CodePackageMock(CodePackage parent, String simpleName) {

      super();
      this.parent = parent;
      this.simpleName = simpleName;
    }

    @Override
    public boolean isFile() {

      return false;
    }

    @Override
    public CodeDoc getDoc() {

      return null;
    }

    @Override
    public CodeAnnotations getAnnotations() {

      return null;
    }

    @Override
    public void removeFromParent() {

    }

    @Override
    public void setComment(CodeComment comment) {

    }

    @Override
    public CodeComment getComment() {

      return null;
    }

    @Override
    public CodePackageMock copy(CodeCopyMapper mapper) {

      return null;
    }

    @Override
    public void setImmutable() {

    }

    @Override
    public void write(Appendable sink, String newline, String defaultIndent, String currentIndent,
        CodeLanguage language) {

    }

    @Override
    public String getSourceCode() {

      return null;
    }

    @Override
    public void write(Path targetFolder) {

    }

    @Override
    public void write(Path targetFolder, Charset encoding) {

    }

    @Override
    public CodePackage getParentPackage() {

      return this.parent;
    }

    @Override
    public String getSimpleName() {

      return this.simpleName;
    }

    @Override
    public CodeContext getContext() {

      return null;
    }

    @Override
    public CodeSource getSource() {

      return null;
    }

    @Override
    public boolean isImmutable() {

      return false;
    }

    @Override
    public CodeContainer getParent() {

      return this.parent;
    }

    @Override
    public CodePathElements getChildren() {

      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public boolean isRequireImport() {

      return false;
    }

    @Override
    public boolean isRoot() {

      return this.parent == null;
    }

    @Override
    public Package getReflectiveObject() {

      return null;
    }

    @Override
    public CodePackage copy() {

      return null;
    }

  }

}
