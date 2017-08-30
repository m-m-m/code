/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.io.IOException;

import net.sf.mmm.code.api.CodeItemWithComment;
import net.sf.mmm.code.api.statement.CodeComment;

/**
 * Implementation of {@link CodeItemWithComment} for Java reflection.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class JavaItemWithComment extends JavaItem implements CodeItemWithComment {

  private CodeComment comment;

  /**
   * The constructor.
   */
  public JavaItemWithComment() {

    super();
  }

  @Override
  public CodeComment getComment() {

    return this.comment;
  }

  /**
   * @param comment the new {@link #getComment() comment}.
   */
  public void setComment(CodeComment comment) {

    verifyMutalbe();
    this.comment = comment;
  }

  @Override
  protected void doWrite(Appendable sink, String defaultIndent, String currentIndent) throws IOException {

    if (this.comment != null) {
      this.comment.write(sink, defaultIndent, currentIndent);
    }
  }

}
