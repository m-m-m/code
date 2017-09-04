/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.statement;

import java.util.List;

/**
 * {@link CodeStatement} for a comment.
 *
 * @author hohwille
 * @since 1.0.0
 */
public interface CodeComment extends CodeStatement {

  /**
   * @return the {@link List} of comment lines. Will always have the {@link List#size() size} {@code 1} in
   *         case of {@link CodeSingleComment}. Each contained {@link String} will be the pure comment without
   *         leading comment prefix ("// ", "/* ", etc.).
   */
  List<String> getComments();

}
