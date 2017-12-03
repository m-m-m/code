/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.element;

import java.io.IOException;
import java.util.Iterator;

import net.sf.mmm.code.api.comment.CodeComment;
import net.sf.mmm.code.api.copy.CodeCopyMapper;
import net.sf.mmm.code.api.element.CodeElement;
import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.api.merge.CodeMergeStrategy;
import net.sf.mmm.code.api.node.CodeNode;
import net.sf.mmm.code.api.node.CodeNodeItemContainer;
import net.sf.mmm.code.base.annoation.BaseAnnotations;
import net.sf.mmm.code.base.comment.BaseBlockComment;
import net.sf.mmm.code.base.doc.BaseDoc;
import net.sf.mmm.code.base.node.BaseNodeItem;

/**
 * Base implementation of {@link CodeElement}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BaseElement extends BaseNodeItem implements CodeElement {

  private BaseAnnotations annotations;

  private BaseDoc doc;

  private CodeComment comment;

  /**
   * The constructor.
   */
  public BaseElement() {

    super();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseElement} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseElement(BaseElement template, CodeCopyMapper mapper) {

    super(template, mapper);
    this.doc = template.getDoc().copy(mapper);
    this.comment = template.getComment();
    this.annotations = template.getAnnotations().copy(mapper);
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
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
    }
    return this.doc;
  }

  @Override
  public BaseAnnotations getAnnotations() {

    if (this.annotations == null) {
      this.annotations = new BaseAnnotations(this);
    }
    return this.annotations;
  }

  @Override
  public CodeComment getComment() {

    if (this.comment == null) {
      CodeElement sourceElement = getSourceCodeObject();
      if (sourceElement != null) {
        this.comment = sourceElement.getComment();
      }
      if (this.comment == null) {
        return BaseBlockComment.EMPTY_COMMENT;
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
    getAnnotations().setImmutableIfNotSystemImmutable();
    getDoc().setImmutableIfNotSystemImmutable();
  }

  @Override
  public CodeElement getSourceCodeObject() {

    return null;
  }

  /**
   * @param other the {@link CodeElement} to
   *        {@link net.sf.mmm.code.api.merge.CodeMergeableItem#merge(net.sf.mmm.code.api.merge.CodeMergeableItem)
   *        merge}.
   * @param strategy the {@link CodeMergeStrategy}.
   */
  protected void doMerge(CodeElement other, CodeMergeStrategy strategy) {

    verifyMutalbe();
    if (strategy == CodeMergeStrategy.KEEP) {
      return;
    }
    boolean override = (strategy == CodeMergeStrategy.OVERRIDE);
    if (override || (this.comment == null)) {
      CodeComment otherComment = other.getComment();
      if (otherComment != BaseBlockComment.EMPTY_COMMENT) {
        this.comment = otherComment;
      } else if (override) {
        this.comment = null;
      }
    } else {
      // TODO merge without duplicating?
    }
    getAnnotations().merge(other.getAnnotations(), strategy);
    getDoc().merge(other.getDoc(), strategy);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    doWriteDoc(sink, newline, defaultIndent, currentIndent, language);
    doWriteComment(sink, newline, defaultIndent, currentIndent, language);
    doWriteAnnotations(sink, newline, defaultIndent, currentIndent, language);
  }

  /**
   * Writes the {@link #getComment() comment}.
   *
   * @param sink the {@link Appendable}.
   * @param newline the newline {@link String}.
   * @param defaultIndent the default indent.
   * @param currentIndent the current indent.
   * @param language the {@link CodeLanguage}.
   * @throws IOException if thrown by {@link Appendable}.
   */
  protected void doWriteComment(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

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
        myComment.write(sink, newline, defaultIndent, currentIndent, language);
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
   * @param language the {@link CodeLanguage}.
   */
  protected void doWriteDoc(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) {

    if (defaultIndent != null) {
      getDoc().write(sink, newline, defaultIndent, currentIndent);
    }
  }

  /**
   * Writes the {@link #getAnnotations() annotations}.
   *
   * @param sink the {@link Appendable}.
   * @param newline the newline {@link String}.
   * @param defaultIndent the default indent.
   * @param currentIndent the current indent.
   * @param language the {@link CodeLanguage}.
   */
  protected void doWriteAnnotations(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) {

    getAnnotations().write(sink, newline, defaultIndent, currentIndent, language);
  }

}
