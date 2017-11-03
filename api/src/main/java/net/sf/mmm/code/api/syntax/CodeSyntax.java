/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.syntax;

import java.io.IOException;

import net.sf.mmm.code.api.expression.CodeVariable;
import net.sf.mmm.code.api.statement.CodeLocalVariable;
import net.sf.mmm.code.api.type.CodeTypeCategory;

/**
 * Interface to abstract from a concrete syntax.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeSyntax {

  /**
   * @return the name of the programming language. E.g. "Java".
   */
  String getLanguageName();

  /**
   * @return the {@link net.sf.mmm.code.api.expression.CodeVariableThis#getName() name} for
   *         {@link net.sf.mmm.code.api.expression.CodeVariableThis}. E.g. "this" or "self".
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
   * @return the {@link String} to add prefixed by the {@link net.sf.mmm.code.api.arg.CodeReturn} before the
   *         {@link net.sf.mmm.code.api.member.CodeMethod#getName() method name} or {@code null} if the
   *         {@link net.sf.mmm.code.api.arg.CodeReturn} shall be written with {@link #getMethodReturnEnd()}.
   */
  default String getMethodReturnStart() {

    return "";
  }

  /**
   * @return the {@link String} to add followed by the {@link net.sf.mmm.code.api.arg.CodeReturn} after the
   *         {@link net.sf.mmm.code.api.member.CodeMethod#getParameters() method parameters} or {@code null}
   *         if the {@link net.sf.mmm.code.api.arg.CodeReturn} shall be written with
   *         {@link #getMethodReturnStart()}. E.g. ": " for TypeScript or Kotlin.
   */
  default String getMethodReturnEnd() {

    return null;
  }

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
   *         {@link net.sf.mmm.code.api.statement.CodeAtomicStatement}. E.g. ";".
   */
  String getStatementTerminator();

  /**
   * @return the {@link String} to signal the start of an
   *         {@link net.sf.mmm.code.api.annotation.CodeAnnotation}.
   */
  default String getAnnotationStart() {

    return "@";
  }

  /**
   * @return the {@link String} to signal the end of an empty
   *         {@link net.sf.mmm.code.api.annotation.CodeAnnotation}. Here empty means that the
   *         {@link net.sf.mmm.code.api.annotation.CodeAnnotation#getParameters() parameters} are
   *         {@link java.util.Map#isEmpty() empty}. E.g. for TypeScript even an empty annotation has to be
   *         terminated with "()".
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
}
