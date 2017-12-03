/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.arg;

import java.io.IOException;
import java.lang.reflect.Type;

import net.sf.mmm.code.api.arg.CodeOperationArg;
import net.sf.mmm.code.api.copy.CodeCopyMapper;
import net.sf.mmm.code.api.copy.CodeCopyType;
import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.api.merge.CodeMergeStrategy;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.base.element.BaseElementWithDeclaringType;
import net.sf.mmm.code.base.member.BaseOperation;
import net.sf.mmm.code.base.type.BaseGenericType;
import net.sf.mmm.code.base.type.BaseType;

/**
 * Base implementation of {@link CodeOperationArg}.
 *
 * @author hohwille
 * @since 1.0.0
 */
public abstract class BaseOperationArg extends BaseElementWithDeclaringType implements CodeOperationArg {

  private BaseGenericType type;

  /**
   * The constructor.
   */
  public BaseOperationArg() {

    super();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseOperationArg} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseOperationArg(BaseOperationArg template, CodeCopyMapper mapper) {

    super(template, mapper);
    this.type = mapper.map(template.type, CodeCopyType.REFERENCE);
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
    getType();
  }

  @Override
  public abstract BaseOperation getDeclaringOperation();

  @Override
  public BaseGenericType getType() {

    if (this.type == null) {
      Type reflectiveObjectType = getReflectiveObjectType();
      if (reflectiveObjectType != null) {
        this.type = getContext().getType(reflectiveObjectType, getDeclaringOperation());
      } else {
        this.type = getDefaultType();
      }
    }
    return this.type;
  }

  /**
   * @return the default type to use if {@link #getType() type} was not {@link #setType(CodeGenericType) set}.
   */
  protected BaseType getDefaultType() {

    return getContext().getRootType();
  }

  @Override
  public void setType(CodeGenericType type) {

    verifyMutalbe();
    this.type = (BaseGenericType) type;
  }

  /**
   * @return the argument {@link Type} of the {@link #getReflectiveObject() reflective object}.
   * @see java.lang.reflect.Parameter#getParameterizedType()
   * @see java.lang.reflect.AnnotatedType#getType()
   */
  protected abstract Type getReflectiveObjectType();

  /**
   * @see #doMerge(net.sf.mmm.code.api.element.CodeElement, CodeMergeStrategy)
   * @param other the {@link CodeOperationArg} to merge.
   * @param strategy the {@link CodeMergeStrategy}.
   */
  protected void doMerge(CodeOperationArg other, CodeMergeStrategy strategy) {

    super.doMerge(other, strategy);
    if (strategy == CodeMergeStrategy.OVERRIDE) {
      this.type = (BaseGenericType) other.getType();
    }
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    doWriteAnnotations(sink, newline, null, "", language);
    doWriteDeclaration(sink, newline, defaultIndent, currentIndent, language);
  }

  /**
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the code from this
   *        {@link CodeItem}.
   * @param newline the newline {@link String}.
   * @param defaultIndent the {@link String} used for indentation (e.g. a number of spaces to insert per indent level).
   * @param currentIndent the current indent (number of spaces). Initially the empty string ({@code ""}). Before a
   *        recursion the {@code defaultIndent} will be appended.
   * @param language the {@link CodeLanguage} to use.
   * @throws IOException if thrown by {@link Appendable}.
   */
  protected void doWriteDeclaration(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    getType().writeReference(sink, false);
  }

}
