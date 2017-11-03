/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.item;

import net.sf.mmm.code.api.syntax.CodeSyntax;
import net.sf.mmm.code.api.syntax.CodeSyntaxJava;

/**
 * Abstract top-level interface for any item of code as defined by this API. It reflects code structure.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeItem {

  /** The default newline (line feed). */
  String DEFAULT_NEWLINE = "\n";

  /** The default indent (2 spaces). */
  String DEFAULT_INDENT = "  ";

  /**
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the
   *        {@link #getSourceCode() source code} from this {@link CodeItem}.
   */
  default void write(Appendable sink) {

    write(sink, DEFAULT_NEWLINE);
  }

  /**
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the
   *        {@link #getSourceCode() source code} from this {@link CodeItem}.
   * @param newline the newline {@link String}.
   */
  default void write(Appendable sink, String newline) {

    write(sink, newline, DEFAULT_INDENT);
  }

  /**
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the
   *        {@link #getSourceCode() source code} from this {@link CodeItem}.
   * @param newline the newline {@link String}.
   * @param defaultIndent the {@link String} used for indentation (e.g. a number of spaces to insert per
   *        indent level).
   */
  default void write(Appendable sink, String newline, String defaultIndent) {

    write(sink, newline, defaultIndent, "");
  }

  /**
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the
   *        {@link #getSourceCode() source code} from this {@link CodeItem}.
   * @param defaultIndent the {@link String} used for indentation (e.g. a number of spaces to insert per
   *        indent level).
   * @param newline the newline {@link String}.
   * @param currentIndent the current indent (number of spaces). Initially the empty string ({@code ""}).
   *        Before a recursion the {@code indent} will be appended.
   */
  default void write(Appendable sink, String newline, String defaultIndent, String currentIndent) {

    write(sink, newline, defaultIndent, currentIndent, getSyntax());
  }

  /**
   * @return the {@link CodeSyntax} to use.
   */
  default CodeSyntax getSyntax() {

    return CodeSyntaxJava.INSTANCE;
  }

  /**
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the
   *        {@link #getSourceCode() source code} from this {@link CodeItem}.
   * @param defaultIndent the {@link String} used for indentation (e.g. a number of spaces to insert per
   *        indent level).
   * @param newline the newline {@link String}.
   * @param currentIndent the current indent (number of spaces). Initially the empty string ({@code ""}).
   *        Before a recursion the {@code indent} will be appended.
   * @param syntax the {@link CodeSyntax} to use.
   */
  void write(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeSyntax syntax);

  /**
   * @return the source code of this item.
   * @see #write(Appendable)
   */
  String getSourceCode();

}
