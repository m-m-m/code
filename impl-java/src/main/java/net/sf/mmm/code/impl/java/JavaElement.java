/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.mmm.code.api.CodeAnnotation;
import net.sf.mmm.code.api.CodeElement;
import net.sf.mmm.code.api.statement.CodeComment;
import net.sf.mmm.code.impl.java.doc.JavaDoc;

/**
 * Implementation of {@link CodeElement} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class JavaElement extends JavaItem implements CodeElement {

  private List<CodeAnnotation> annotations;

  private JavaDoc doc;

  private CodeComment comment;

  /**
   * The constructor.
   *
   * @param context the {@link #getContext() context}.
   */
  public JavaElement(JavaContext context) {

    super(context);
    this.annotations = new ArrayList<>();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaElement} to copy.
   */
  public JavaElement(JavaElement template) {

    super(template);
    this.doc = template.doc;
    this.comment = template.comment;
    this.annotations = template.annotations;
  }

  @Override
  public JavaDoc getDoc() {

    if (this.doc == null) {
      this.doc = new JavaDoc(this);
    }
    return this.doc;
  }

  @Override
  public List<CodeAnnotation> getAnnotations() {

    return this.annotations;
  }

  @Override
  public CodeComment getComment() {

    return this.comment;
  }

  @Override
  public void setComment(CodeComment comment) {

    verifyMutalbe();
    this.comment = comment;
  }

  @Override
  protected void doSetImmutable() {

    super.doSetImmutable();
    this.annotations = makeImmutable(this.annotations);
    getDoc().setImmutable();
  }

  /**
   * @return {@code true} if this element only allows inline {@link #getComment() comments} (without
   *         newlines), {@code false} otherwise.
   */
  protected boolean isCommentForceInline() {

    return false;
  }

  @Override
  protected void doWrite(Appendable sink, String defaultIndent, String currentIndent) throws IOException {

    doWriteComment(sink, defaultIndent, currentIndent);
    doWriteDoc(sink, defaultIndent, currentIndent);
    doWriteAnnotations(sink, defaultIndent, currentIndent);
  }

  /**
   * Writes the {@link #getComment() comment}.
   *
   * @param sink the {@link Appendable}.
   * @param defaultIndent the default indent.
   * @param currentIndent the current indent.
   * @throws IOException if thrown by {@link Appendable}.
   */
  protected void doWriteComment(Appendable sink, String defaultIndent, String currentIndent) throws IOException {

    if (this.comment != null) {
      if (isCommentForceInline()) {
        List<String> lines = this.comment.getComments();
        if (!lines.isEmpty()) {
          String separator = "/* ";
          for (String line : lines) {
            sink.append(separator);
            line = line.trim();
            sink.append(line);
            separator = " ";
          }
          sink.append(" */");
        }
      } else {
        this.comment.write(sink, defaultIndent, currentIndent);
      }
    }
  }

  /**
   * Writes the {@link #getDoc() JavaDoc}.
   *
   * @param sink the {@link Appendable}.
   * @param defaultIndent the default indent.
   * @param currentIndent the current indent.
   */
  protected void doWriteDoc(Appendable sink, String defaultIndent, String currentIndent) {

    if (this.doc != null) {
      this.doc.write(sink, defaultIndent, currentIndent);
    }
  }

  /**
   * Writes the {@link #getAnnotations() annotations}.
   *
   * @param sink the {@link Appendable}.
   * @param defaultIndent the default indent.
   * @param currentIndent the current indent.
   */
  protected void doWriteAnnotations(Appendable sink, String defaultIndent, String currentIndent) {

    for (CodeAnnotation annotation : this.annotations) {
      annotation.write(sink, defaultIndent, currentIndent);
    }
  }

}
