/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.element;

import java.io.IOException;
import java.util.List;

import net.sf.mmm.code.api.element.CodeElement;
import net.sf.mmm.code.api.statement.CodeComment;
import net.sf.mmm.code.impl.java.JavaContext;
import net.sf.mmm.code.impl.java.annotation.JavaAnnotations;
import net.sf.mmm.code.impl.java.doc.JavaDoc;
import net.sf.mmm.code.impl.java.item.JavaItem;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link CodeElement} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class JavaElement extends JavaItem implements CodeElement {

  private final JavaAnnotations annotations;

  private final JavaDoc doc;

  private CodeComment comment;

  /**
   * The constructor.
   *
   * @param context the {@link #getContext() context}.
   */
  public JavaElement(JavaContext context) {

    super(context);
    this.doc = new JavaDoc(this);
    this.annotations = new JavaAnnotations(this);
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
    this.annotations = template.annotations.copy(this);
  }

  @Override
  public JavaDoc getDoc() {

    return this.doc;
  }

  @Override
  public JavaAnnotations getAnnotations() {

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
    this.annotations.setImmutable();
    this.doc.setImmutable();
  }

  @Override
  public abstract JavaType getDeclaringType();

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

    if ((this.doc != null) && !isCommentForceInline()) {
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

    if (this.annotations != null) {
      if (isCommentForceInline()) {
        this.annotations.write(sink, null, null);
      } else {
        this.annotations.write(sink, defaultIndent, currentIndent);
      }
    }
  }

}
