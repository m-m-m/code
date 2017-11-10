/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.language;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import net.sf.mmm.code.api.expression.CodeVariable;
import net.sf.mmm.code.api.expression.CodeVariableThis;
import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.item.CodeItemWithName;
import net.sf.mmm.code.api.statement.CodeLocalVariable;
import net.sf.mmm.code.api.type.CodeTypeCategory;

/**
 * The default implementation of {@link CodeLanguage} (for Java).
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class CodeLanguageJava extends AbstractCodeLanguage {

  /** The {@link #getLanguageName() language name}. */
  public static final String LANGUAGE_NAME_JAVA = "Java";

  static final Pattern NAME_PATTERN = Pattern.compile("[\\$_\\w]+");

  static final Pattern NAME_PATTERN_PACKAGE = Pattern.compile("(\\pL|[$_])(\\pL|\\p{Nd}|[$_])*");

  static final Pattern NAME_PATTERN_TYPE = NAME_PATTERN;

  private static final Set<String> REVERVED_NAMES = new HashSet<>(Arrays.asList("abstract", "continue", "for", "new", "switch", "assert", "default", "goto",
      "package", "synchronized", "boolean", "do", "if", "private", "this", "break", "double", "implements", "protected", "throw", "byte", "else", "import",
      "public", "throws", "case", "enum", "instanceof", "return", "transient", "catch", "extends", "int", "short", "try", "char", "final", "interface",
      "static", "void", "class", "finally", "long", "strictfp", "volatile", "const", "float", "native", "super", "while"));

  /** The singleton instance. */
  public static final CodeLanguageJava INSTANCE = new CodeLanguageJava();

  @Override
  public String getLanguageName() {

    return LANGUAGE_NAME_JAVA;
  }

  @Override
  public String getVariableNameThis() {

    return CodeVariableThis.NAME_THIS;
  }

  @Override
  public String getKeywordForVariable(CodeLocalVariable variable) {

    if (variable.isFinal()) {
      return "final ";
    }
    return "";
  }

  @Override
  public void writeDeclaration(CodeVariable variable, Appendable sink) throws IOException {

    variable.getType().writeReference(sink, false);
    sink.append(' ');
    sink.append(variable.getName());
  }

  @Override
  public String getKeywordForCategory(CodeTypeCategory category) {

    if (category.isAnnotation()) {
      return "@interface";
    } else if (category.isEnumeration()) {
      return "enum";
    } else {
      return category.toString();
    }
  }

  @Override
  public String getStatementTerminator() {

    return ";";
  }

  @Override
  protected Pattern getNamePattern(CodeItemWithName item) {

    return NAME_PATTERN;
  }

  @Override
  protected Pattern getSimpleNamePatternForPackage() {

    return NAME_PATTERN_PACKAGE;
  }

  @Override
  protected Pattern getSimpleNamePatternForType() {

    return NAME_PATTERN_TYPE;
  }

  @Override
  protected boolean isRevervedKeyword(String name, CodeItem item) {

    return REVERVED_NAMES.contains(name);
  }

}
