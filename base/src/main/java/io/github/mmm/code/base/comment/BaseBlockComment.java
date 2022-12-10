/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.comment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.github.mmm.code.api.comment.CodeBlockComment;
import io.github.mmm.code.api.comment.CodeComment;
import io.github.mmm.code.api.language.CodeLanguage;

/**
 * Base implementation of {@link CodeBlockComment}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseBlockComment extends BaseComment implements CodeBlockComment {

  /** An empty comment instance. */
  public static final BaseBlockComment EMPTY_COMMENT = new BaseBlockComment(Collections.emptyList());

  private final List<String> commentLines;

  /**
   * The constructor.
   *
   * @param comment an existing instance of {@link CodeComment} to copy/convert to a block comment.
   */
  public BaseBlockComment(CodeComment comment) {

    super();
    List<String> list = new ArrayList<>();
    for (String line : comment) {
      list.add(line);
    }
    this.commentLines = Collections.unmodifiableList(list);
  }

  /**
   * The constructor.
   *
   * @param commentLines the {@link #getCommentLines() comment lines}.
   */
  public BaseBlockComment(List<String> commentLines) {

    super();
    this.commentLines = Collections.unmodifiableList(commentLines);
  }

  /**
   * The constructor.
   *
   * @param commentLines the {@link #getCommentLines() comment lines}.
   */
  public BaseBlockComment(String... commentLines) {

    super();
    this.commentLines = Collections.unmodifiableList(Arrays.asList(commentLines));
  }

  @Override
  public List<String> getCommentLines() {

    return this.commentLines;
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    if (this.commentLines.isEmpty()) {
      return;
    }
    sink.append(currentIndent);
    sink.append("/*");
    if (this.commentLines.size() == 1) {
      sink.append(' ');
      sink.append(this.commentLines.get(0));
      sink.append("*/");
    } else {
      String prefix = newline + currentIndent + " *";
      for (String line : this.commentLines) {
        sink.append(prefix);
        sink.append(line);
      }
      sink.append(prefix);
      sink.append('/');
    }
    sink.append(newline);
  }

}
