/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.comment;

import java.util.Arrays;
import java.util.List;

/**
 * {@link CodeComment} for a single comment such as {@link CodeSingleLineComment} or
 * {@link CodeInlineComment}.
 *
 * @author hohwille
 * @since 1.0.0
 */
public abstract interface CodeSingleComment extends CodeComment {

  /**
   * @return the single comment (excluding any comment markup like "//", "/*", or "*{@literal /}". Should not
   *         contain newline character(s).
   */
  String getComment();

  @Override
  default List<String> getCommentLines() {

    return Arrays.asList(getComment());
  }

}
