/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.element;

import java.io.IOException;
import java.util.List;

import net.sf.mmm.code.api.element.CodeElement;
import net.sf.mmm.code.api.node.CodeNodeItemContainer;
import net.sf.mmm.code.api.statement.CodeComment;
import net.sf.mmm.code.impl.java.annotation.JavaAnnotations;
import net.sf.mmm.code.impl.java.doc.JavaDoc;
import net.sf.mmm.code.impl.java.node.JavaNode;
import net.sf.mmm.code.impl.java.node.JavaNodeItem;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link CodeElement} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class JavaElement extends JavaNodeItem implements JavaElementNode {

  private final JavaAnnotations annotations;

  private final JavaDoc doc;

  private CodeComment comment;

  /**
   * The constructor.
   */
  public JavaElement() {

    super();
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
    this.doc = template.getDoc().copy();
    this.comment = template.getComment();
    this.annotations = template.getAnnotations().copy(this);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public void removeFromParent() {

    verifyMutalbe();
    JavaNode parent = getParent();
    if (parent instanceof CodeNodeItemContainer) {
      ((CodeNodeItemContainer) parent).remove(this);
    } else {
      throw new UnsupportedOperationException();
    }
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
  protected boolean isSystemImmutable() {

    if (super.isSystemImmutable()) {
      return true;
    }
    Object reflectiveObject = getReflectiveObject();
    if (reflectiveObject != null) {
      return true;
    }
    return false;
  }

  @Override
  public abstract JavaType getDeclaringType();

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent) throws IOException {

    doWriteComment(sink, newline, defaultIndent, currentIndent);
    doWriteDoc(sink, newline, defaultIndent, currentIndent);
    doWriteAnnotations(sink, newline, defaultIndent, currentIndent);
  }

  /**
   * Writes the {@link #getComment() comment}.
   *
   * @param sink the {@link Appendable}.
   * @param newline the newline {@link String}.
   * @param defaultIndent the default indent.
   * @param currentIndent the current indent.
   * @throws IOException if thrown by {@link Appendable}.
   */
  protected void doWriteComment(Appendable sink, String newline, String defaultIndent, String currentIndent) throws IOException {

    if (this.comment != null) {
      if (currentIndent == null) {
        if (defaultIndent == null) {
          return;
        }
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
        this.comment.write(sink, newline, defaultIndent, currentIndent);
      }
    }
  }

  /**
   * Writes the {@link #getDoc() JavaDoc}.
   *
   * @param sink the {@link Appendable}.
   * @param newline the newline {@link String}.
   * @param defaultIndent the default indent.
   * @param currentIndent the current indent.
   */
  protected void doWriteDoc(Appendable sink, String newline, String defaultIndent, String currentIndent) {

    if ((this.doc != null) && (currentIndent != null)) {
      this.doc.write(sink, newline, defaultIndent, currentIndent);
    }
  }

  /**
   * Writes the {@link #getAnnotations() annotations}.
   *
   * @param sink the {@link Appendable}.
   * @param newline the newline {@link String}.
   * @param defaultIndent the default indent.
   * @param currentIndent the current indent.
   */
  protected void doWriteAnnotations(Appendable sink, String newline, String defaultIndent, String currentIndent) {

    if (this.annotations != null) {
      this.annotations.write(sink, newline, defaultIndent, currentIndent);
    }
  }

}
