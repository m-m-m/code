/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.comment;

import java.util.Iterator;
import java.util.List;

import io.github.mmm.code.api.expression.CodeVariable;
import io.github.mmm.code.api.statement.CodeStatement;

/**
 * {@link CodeStatement} for a comment.
 *
 * @author hohwille
 * @since 1.0.0
 */
public abstract interface CodeComment extends CodeStatement, Iterable<String> {

  @Override
  default CodeVariable getVariable(String name) {

    return null;
  }

  @Override
  default Iterator<String> iterator() {

    return getCommentLines().iterator();
  }

  /**
   * @return the {@link List} of comment lines. Will always have the {@link List#size() size} {@code 1} in
   *         case of {@link CodeSingleLineComment}. Each contained {@link String} will be the pure comment
   *         without leading comment prefix ("// ", "/* ", etc.).
   */
  Iterable<String> getCommentLines();

}
