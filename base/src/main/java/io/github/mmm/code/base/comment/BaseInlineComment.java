/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.comment;

import java.io.IOException;

import io.github.mmm.code.api.comment.CodeInlineComment;
import io.github.mmm.code.api.language.CodeLanguage;

/**
 * Base implementation of {@link CodeInlineComment}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseInlineComment extends BaseSingleComment implements CodeInlineComment {

  /**
   * The constructor.
   *
   * @param comment the {@link #getComment() comment}.
   */
  public BaseInlineComment(String comment) {

    super(comment);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    sink.append(currentIndent);
    sink.append("/* ");
    sink.append(getComment());
    sink.append(" */");
  }

}
