/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.comment;

import java.util.Objects;

import net.sf.mmm.code.api.comment.CodeSingleComment;

/**
 * Base implementation of {@link CodeSingleComment}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BaseSingleComment extends BaseComment implements CodeSingleComment {

  private final String comment;

  /**
   * The constructor.
   *
   * @param comment the single {@link #getComment() comment} line.
   */
  public BaseSingleComment(String comment) {

    super();
    Objects.requireNonNull(comment, "comment");
    this.comment = comment;
    assert (!comment.contains("\n"));
    assert (!comment.contains("\r"));
  }

  @Override
  public String getComment() {

    return this.comment;
  }

}
