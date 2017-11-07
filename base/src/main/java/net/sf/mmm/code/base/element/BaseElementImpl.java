/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.element;

import java.io.IOException;
import java.util.Iterator;

import net.sf.mmm.code.api.annotation.CodeAnnotations;
import net.sf.mmm.code.api.comment.CodeComment;
import net.sf.mmm.code.api.node.CodeNode;
import net.sf.mmm.code.api.node.CodeNodeItemContainer;
import net.sf.mmm.code.api.syntax.CodeSyntax;
import net.sf.mmm.code.base.BaseContext;
import net.sf.mmm.code.base.annoation.BaseAnnotations;
import net.sf.mmm.code.base.doc.BaseDoc;
import net.sf.mmm.code.base.node.BaseNodeItemImpl;

/**
 * Base implementation of {@link BaseElement}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BaseElementImpl extends BaseNodeItemImpl implements BaseElement {

  private BaseAnnotations annotations;

  private BaseDoc doc;

  private CodeComment comment;

  /**
   * The constructor.
   */
  public BaseElementImpl() {

    super();
    this.annotations = new BaseAnnotations(this);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseElementImpl} to copy.
   */
  public BaseElementImpl(BaseElementImpl template) {

    super(template);
    this.doc = template.getDoc().copy(this);
    this.comment = template.getComment();
    this.annotations = doCopy(template.getAnnotations(), this);
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
    getDoc();
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public void removeFromParent() {

    verifyMutalbe();
    CodeNode parent = getParent();
    if (parent instanceof CodeNodeItemContainer) {
      ((CodeNodeItemContainer) parent).remove(this);
    } else {
      throw new UnsupportedOperationException();
    }
  }

  @Override
  public BaseDoc getDoc() {

    if (this.doc == null) {
      this.doc = new BaseDoc(this);
      BaseElementImpl sourceElement = getSourceCodeObject();
      if (sourceElement != null) {
        this.doc.getLines().addAll(sourceElement.getDoc().getLines());
      }
    }
    return this.doc;
  }

  @Override
  public BaseAnnotations getAnnotations() {

    return this.annotations;
  }

  @Override
  public CodeComment getComment() {

    if (this.comment == null) {
      BaseElementImpl sourceElement = getSourceCodeObject();
      if (sourceElement != null) {
        this.comment = sourceElement.getComment();
      }
    }
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
    this.annotations.setImmutableIfNotSystemImmutable();
    this.doc.setImmutableIfNotSystemImmutable();
  }

  @Override
  public BaseElementImpl getSourceCodeObject() {

    return null;
  }

  @Override
  public BaseContext getContext() {

    return getParent().getContext();
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeSyntax syntax) throws IOException {

    doWriteDoc(sink, newline, defaultIndent, currentIndent, syntax);
    doWriteComment(sink, newline, defaultIndent, currentIndent, syntax);
    doWriteAnnotations(sink, newline, defaultIndent, currentIndent, syntax);
  }

  /**
   * Writes the {@link #getComment() comment}.
   *
   * @param sink the {@link Appendable}.
   * @param newline the newline {@link String}.
   * @param defaultIndent the default indent.
   * @param currentIndent the current indent.
   * @param syntax the {@link CodeSyntax}.
   * @throws IOException if thrown by {@link Appendable}.
   */
  protected void doWriteComment(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeSyntax syntax) throws IOException {

    CodeComment myComment = getComment();
    if (myComment != null) {
      if (defaultIndent == null) {
        if ("".equals(newline)) {
          return;
        }
        Iterator<? extends String> lines = myComment.iterator();
        if (lines.hasNext()) {
          String separator = "/* ";
          while (lines.hasNext()) {
            String line = lines.next();
            sink.append(separator);
            line = line.trim();
            sink.append(line);
            separator = " ";
          }
          sink.append(" */");
        }
      } else {
        myComment.write(sink, newline, defaultIndent, currentIndent, syntax);
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
   * @param syntax the {@link CodeSyntax}.
   */
  protected void doWriteDoc(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeSyntax syntax) {

    if ((getDoc() != null) && (currentIndent != null)) {
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
   * @param syntax the {@link CodeSyntax}.
   */
  protected void doWriteAnnotations(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeSyntax syntax) {

    CodeAnnotations myAnnotations = getAnnotations();
    if (myAnnotations != null) {
      myAnnotations.write(sink, newline, defaultIndent, currentIndent, syntax);
    }
  }

}
