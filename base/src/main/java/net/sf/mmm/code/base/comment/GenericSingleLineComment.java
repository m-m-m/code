/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.comment;

import java.io.IOException;

import net.sf.mmm.code.api.comment.CodeSingleLineComment;
import net.sf.mmm.code.api.syntax.CodeSyntax;

/**
 * Generic implementation of {@link CodeSingleLineComment}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class GenericSingleLineComment extends GenericSingleComment implements CodeSingleLineComment {

  /**
   * The constructor.
   *
   * @param comment the single {@link #getComment() comment} line.
   */
  public GenericSingleLineComment(String comment) {

    super(comment);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeSyntax syntax) throws IOException {

    sink.append(currentIndent);
    sink.append("// ");
    sink.append(getComment());
    sink.append(newline);
  }

}
