/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.item;

import net.sf.mmm.code.api.CodeFile;
import net.sf.mmm.code.api.comment.CodeComment;

/**
 * {@link CodeItem} that has an optional {@link #getComment() comment}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeItemWithComment extends CodeItem {

  /**
   * @return the {@link CodeComment} comment above this item (e.g. header of {@link CodeFile}) or {@code null}
   *         for none.
   */
  CodeComment getComment();

}
