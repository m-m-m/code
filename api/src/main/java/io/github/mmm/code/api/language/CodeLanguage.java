/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.language;

import java.io.IOException;

import io.github.mmm.code.api.CodeFile;
import io.github.mmm.code.api.CodePackage;
import io.github.mmm.code.api.expression.CodeVariable;
import io.github.mmm.code.api.item.CodeItemWithName;
import io.github.mmm.code.api.item.CodeItemWithQualifiedName;
import io.github.mmm.code.api.member.CodeConstructor;
import io.github.mmm.code.api.member.CodeField;
import io.github.mmm.code.api.member.CodeMethod;
import io.github.mmm.code.api.statement.CodeLocalVariable;
import io.github.mmm.code.api.type.CodeType;
import io.github.mmm.code.api.type.CodeTypeCategory;

/**
 * Interface to abstract from a concrete language.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeLanguage {

  /**
   * @return the name of the programming language. E.g. "Java".
   */
  String getLanguageName();

  /**
   * @return the {@link io.github.mmm.code.api.expression.CodeVariableThis#getName() name} for
   *         {@link io.github.mmm.code.api.expression.CodeVariableThis}. E.g. "this" or "self".
   */
  String getVariableNameThis();

  /**
   * @param variable the {@link CodeLocalVariable}.
   * @return the keyword for the declaration (e.g. "let ", "final ", "var ", or "val ").
   */
  String getKeywordForVariable(CodeLocalVariable variable);

  /**
   * Writes a variable declaration. E.g.
   * <code>«{@link CodeVariable#getType() type}» «{@link CodeVariable#getName() name}»</code> or
   * <code>«{@link CodeVariable#getName() name}»: «{@link CodeVariable#getType() type}»</code>
   *
   * @param variable the {@link CodeVariable} to write.
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the code to.
   * @throws IOException if thrown by {@link Appendable}.
   */
  void writeDeclaration(CodeVariable variable, Appendable sink) throws IOException;

  /**
   * Writes a type declaration (e.g. "public class MyClass").
   *
   * @param type the {@link CodeType} to write.
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the code to.
   * @param newline the newline {@link String}.
   * @param defaultIndent the {@link String} used for indentation (e.g. a number of spaces to insert per indent level).
   * @param currentIndent the current indent (number of spaces). Initially the empty string ({@code ""}). Before a
   *        recursion the {@code defaultIndent} will be appended.
   * @throws IOException if thrown by {@link Appendable}.
   */
  void writeDeclaration(CodeType type, Appendable sink, String newline, String defaultIndent, String currentIndent)
      throws IOException;

  /**
   * Writes the type body (e.g. "{\n private String field;\n }\n").
   *
   * @param type the {@link CodeType} to write.
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the code to.
   * @param newline the newline {@link String}.
   * @param defaultIndent the {@link String} used for indentation (e.g. a number of spaces to insert per indent level).
   * @param currentIndent the current indent (number of spaces). Initially the empty string ({@code ""}). Before a
   *        recursion the {@code defaultIndent} will be appended.
   * @throws IOException if thrown by {@link Appendable}.
   */
  void writeBody(CodeType type, Appendable sink, String newline, String defaultIndent, String currentIndent)
      throws IOException;

  /**
   * Writes the given field (e.g. "private String field;\n").
   *
   * @param field the {@link CodeField} to write.
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the code to.
   * @param newline the newline {@link String}.
   * @param defaultIndent the {@link String} used for indentation (e.g. a number of spaces to insert per indent level).
   * @param currentIndent the current indent (number of spaces). Initially the empty string ({@code ""}). Before a
   *        recursion the {@code defaultIndent} will be appended.
   * @throws IOException if thrown by {@link Appendable}.
   */
  void writeField(CodeField field, Appendable sink, String newline, String defaultIndent, String currentIndent)
      throws IOException;

  /**
   * Writes the given method (e.g. "String getName();\n").
   *
   * @param method the {@link CodeField} to write.
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the code to.
   * @param newline the newline {@link String}.
   * @param defaultIndent the {@link String} used for indentation (e.g. a number of spaces to insert per indent level).
   * @param currentIndent the current indent (number of spaces). Initially the empty string ({@code ""}). Before a
   *        recursion the {@code defaultIndent} will be appended.
   * @throws IOException if thrown by {@link Appendable}.
   */
  void writeMethod(CodeMethod method, Appendable sink, String newline, String defaultIndent, String currentIndent)
      throws IOException;

  /**
   * Writes the given {@link CodeConstructor}.
   *
   * @param constructor the {@link CodeConstructor} to write.
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the code to.
   * @param newline the newline {@link String}.
   * @param defaultIndent the {@link String} used for indentation (e.g. a number of spaces to insert per indent level).
   * @param currentIndent the current indent (number of spaces). Initially the empty string ({@code ""}). Before a
   *        recursion the {@code defaultIndent} will be appended.
   * @throws IOException if thrown by {@link Appendable}.
   */
  void writeConstructor(CodeConstructor constructor, Appendable sink, String newline, String defaultIndent,
      String currentIndent) throws IOException;

  /**
   * @return the " extends " keyword (May also be " : " for Kotlin).
   */
  default String getKeywordForExtends() {

    return " extends ";
  }

  /**
   * @return the " implements " keyword (May also be " : " for Kotlin).
   */
  default String getKeywordForImplements() {

    return " implements ";
  }

  /**
   * @param category the {@link CodeTypeCategory}.
   * @return the keyword for the given {@code category} to use in the source-code.
   */
  String getKeywordForCategory(CodeTypeCategory category);

  /**
   * @return the keyword/prefix to initiate a method (e.g. "fun " for Kotlin).
   */
  default String getMethodKeyword() {

    return "";
  }

  /**
   * @return the {@link String} used as suffix to terminate a
   *         {@link io.github.mmm.code.api.statement.CodeAtomicStatement}. E.g. ";".
   */
  String getStatementTerminator();

  /**
   * @return the {@link String} to signal the start of an {@link io.github.mmm.code.api.annotation.CodeAnnotation}.
   */
  default String getAnnotationStart() {

    return "@";
  }

  /**
   * @return the {@link String} to signal the end of an empty {@link io.github.mmm.code.api.annotation.CodeAnnotation}.
   *         Here empty means that the {@link io.github.mmm.code.api.annotation.CodeAnnotation#getParameters()
   *         parameters} are {@link java.util.Map#isEmpty() empty}. E.g. for TypeScript even an empty annotation has to
   *         be terminated with "()".
   */
  default String getAnnotationEndIfEmpty() {

    return "";
  }

  /**
   * @return the package separator character.
   */
  default char getPackageSeparator() {

    return '.';
  }

  /**
   * @param item the {@link CodeItemWithName} to verify.
   * @param name the new {@link CodeItemWithName#getName() name} to verify.
   * @return the given {@code name} (or potentially a "normalized" name that is valid).
   * @throws RuntimeException if the name is invalid.
   */
  String verifyName(CodeItemWithName item, String name);

  /**
   * @param item the {@link CodeItemWithQualifiedName} to verify.
   * @param simpleName the new {@link CodeItemWithQualifiedName#getSimpleName() simple name} to verify.
   * @return the given {@code simpleName} (or potentially a "normalized" name that is valid).
   * @throws RuntimeException if the name is invalid.
   */
  String verifySimpleName(CodeItemWithQualifiedName item, String simpleName);

  /**
   * @param pkg the {@link CodePackage} to be read or write.
   * @return the filename for the given {@link CodePackage}.
   */
  String getPackageFilename(CodePackage pkg);

  /**
   * @param file the {@link CodeType} to be read or write.
   * @return the filename for the given {@link CodeType}.
   */
  String getFileFilename(CodeFile file);

  /**
   * @return {@code true} if this language natively supports {@link io.github.mmm.code.api.member.CodeProperty
   *         properties} (in such case
   *         {@link io.github.mmm.code.api.CodeFactory#createField(io.github.mmm.code.api.member.CodeFields, String, java.lang.reflect.Field)}
   *         needs to provide an implementation that also implements
   *         {@link io.github.mmm.code.api.member.CodeProperty}), {@code false} otherwise.
   */
  default boolean isSupportingNativeProperties() {

    return false;
  }

}
