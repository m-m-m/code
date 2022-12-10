/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.item;

import io.github.mmm.code.api.comment.CodeComment;

/**
 * {@link CodeItem} that has an optional {@link #getComment() comment}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeMutableItemWithComment extends CodeMutableItem, CodeItemWithComment {

  /**
   * @param comment the new {@link #getComment() comment}.
   * @throws io.github.mmm.base.exception.ReadOnlyException if {@link #isImmutable() immutable}.
   */
  void setComment(CodeComment comment);

}
