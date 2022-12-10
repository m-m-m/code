/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.comment;

import java.util.List;

/**
 * {@link CodeComment} representing a multi-line comment. Example<br>
 * <pre>
 * /*
 *  * This is the first line of the comment.
 *  * There can also be more lines.
 *  *&#47;
 * </pre>
 *
 * @author hohwille
 * @since 1.0.0
 */
public interface CodeBlockComment extends CodeComment {

  @Override
  List<String> getCommentLines();
}
