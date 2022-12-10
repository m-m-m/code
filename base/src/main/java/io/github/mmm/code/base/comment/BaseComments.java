/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.comment;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import io.github.mmm.code.api.comment.CodeComment;
import io.github.mmm.code.api.comment.CodeComments;
import io.github.mmm.code.api.language.CodeLanguage;

/**
 * Base implementation of {@link CodeComments}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseComments extends BaseComment implements CodeComments {

  private final List<? extends CodeComment> commentBlocks;

  /**
   * The constructor.
   *
   * @param commentBlocks the {@link #getCommentBlocks() comment blocks}.
   */
  public BaseComments(List<? extends CodeComment> commentBlocks) {

    super();
    this.commentBlocks = Collections.unmodifiableList(commentBlocks);
  }

  @Override
  public Iterable<String> getCommentLines() {

    return () -> new CommentLineIterator();
  }

  @Override
  public List<? extends CodeComment> getCommentBlocks() {

    return this.commentBlocks;
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent,
      CodeLanguage language) throws IOException {

    for (CodeComment block : this.commentBlocks) {
      block.write(sink, newline, defaultIndent, currentIndent);
    }
  }

  private class CommentLineIterator implements Iterator<String> {

    /** the next item or {@code null} if done */
    private String next;

    private boolean done;

    private int blockIndex;

    private Iterator<String> lines;

    private CommentLineIterator() {

      super();
      this.next = findNext();
    }

    @Override
    public final boolean hasNext() {

      if (this.next != null) {
        return true;
      }
      if (this.done) {
        return false;
      }
      this.next = findNext();
      if (this.next == null) {
        this.done = true;
      }
      return (!this.done);
    }

    @Override
    public final String next() {

      if (this.next == null) {
        throw new NoSuchElementException();
      } else {
        String result = this.next;
        this.next = null;
        return result;
      }
    }

    private String findNext() {

      while ((this.lines == null) || !this.lines.hasNext()) {
        if (this.blockIndex >= BaseComments.this.commentBlocks.size()) {
          return null;
        }
        this.lines = BaseComments.this.commentBlocks.get(this.blockIndex++).iterator();
      }
      return this.lines.next();
    }
  }

}
