/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.comment;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sf.mmm.code.api.comment.CodeComment;
import net.sf.mmm.code.api.comment.CodeComments;
import net.sf.mmm.code.api.syntax.CodeSyntax;
import net.sf.mmm.util.collection.base.AbstractIterator;

/**
 * Generic implementation of {@link CodeComments}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class GenericComments extends GenericComment implements CodeComments {

  private final List<? extends CodeComment> commentBlocks;

  /**
   * The constructor.
   *
   * @param commentBlocks the {@link #getCommentBlocks() comment blocks}.
   */
  public GenericComments(List<? extends CodeComment> commentBlocks) {

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
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeSyntax syntax) throws IOException {

    for (CodeComment block : this.commentBlocks) {
      block.write(sink, newline, defaultIndent, currentIndent);
    }
  }

  private class CommentLineIterator extends AbstractIterator<String> {

    private int blockIndex;

    private Iterator<String> lines;

    private CommentLineIterator() {

      super();
      findFirst();
    }

    @Override
    protected String findNext() {

      while ((this.lines == null) || !this.lines.hasNext()) {
        if (this.blockIndex >= GenericComments.this.commentBlocks.size()) {
          return null;
        }
        this.lines = GenericComments.this.commentBlocks.get(this.blockIndex++).iterator();
      }
      return this.lines.next();
    }
  }

}
