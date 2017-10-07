/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.comment;

import java.util.List;

/**
 * {@link CodeComment} representing a sequence of {@link CodeComment}s. Example:<br>
 * <pre>
 * // This is a single-line comment
 * // Yet another single-line comment
 * /* This is a multi-line comment with only a single line *&#47;
 * // Again a single-line comment
 * /*
 *  * And again multi-line
 *  * comment follows.
 *  *&#47;
 * </pre>
 *
 * @author hohwille
 * @since 1.0.0
 */
public interface CodeComments extends CodeComment {

  /**
   * @return the {@link List} with the {@link CodeComment}s.
   */
  List<? extends CodeComment> getCommentBlocks();

}
