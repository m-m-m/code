/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.item;

import java.io.IOException;

import net.sf.mmm.code.api.item.CodeItemWithComment;
import net.sf.mmm.code.api.statement.CodeComment;
import net.sf.mmm.code.impl.java.JavaContext;

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
   *
   * @param context the {@link #getContext() context}.
   */
  public JavaItemWithComment(JavaContext context) {

    super(context);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaItemWithComment} to copy.
   */
  public JavaItemWithComment(JavaItemWithComment template) {

    super(template);
    this.comment = template.comment; // TODO copy?
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
