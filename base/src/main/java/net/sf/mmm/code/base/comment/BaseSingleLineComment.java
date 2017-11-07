/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.comment;

import java.io.IOException;

import net.sf.mmm.code.api.comment.CodeSingleLineComment;
import net.sf.mmm.code.api.language.CodeLanguage;

/**
 * Base implementation of {@link CodeSingleLineComment}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseSingleLineComment extends BaseSingleComment implements CodeSingleLineComment {

  /**
   * The constructor.
   *
   * @param comment the single {@link #getComment() comment} line.
   */
  public BaseSingleLineComment(String comment) {

    super(comment);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    sink.append(currentIndent);
    sink.append("// ");
    sink.append(getComment());
    sink.append(newline);
  }

}
