/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.arg;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;

import net.sf.mmm.code.api.arg.CodeReturn;
import net.sf.mmm.code.api.copy.CodeCopyMapper;
import net.sf.mmm.code.api.copy.CodeCopyType;
import net.sf.mmm.code.api.member.CodeMethod;
import net.sf.mmm.code.api.merge.CodeMergeStrategy;
import net.sf.mmm.code.base.member.BaseMethod;
import net.sf.mmm.code.base.member.BaseOperation;
import net.sf.mmm.code.base.type.BaseType;

/**
 * Base implementation of {@link CodeReturn}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseReturn extends BaseOperationArg implements CodeReturn {

  private final BaseMethod parent;

  private final AnnotatedType reflectiveObject;

  private CodeReturn sourceCodeObject;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May be {@code null}.
   */
  public BaseReturn(BaseMethod parent, AnnotatedType reflectiveObject) {

    super();
    this.parent = parent;
    this.reflectiveObject = reflectiveObject;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseReturn} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseReturn(BaseReturn template, CodeCopyMapper mapper) {

    super(template, mapper);
    this.parent = mapper.map(template.parent, CodeCopyType.PARENT);
    this.reflectiveObject = null;
  }

  @Override
  protected BaseType getDefaultType() {

    return getContext().getVoidType();
  }

  @Override
  public BaseMethod getParent() {

    return this.parent;
  }

  @Override
  public BaseOperation getDeclaringOperation() {

    return this.parent;
  }

  @Override
  public AnnotatedType getReflectiveObject() {

    return this.reflectiveObject;
  }

  @Override
  protected Type getReflectiveObjectType() {

    if (this.reflectiveObject != null) {
      return this.reflectiveObject.getType();
    }
    return null;
  }

  @Override
  public BaseType getDeclaringType() {

    return getParent().getDeclaringType();
  }

  @Override
  public CodeReturn getSourceCodeObject() {

    if (this.sourceCodeObject == null) {
      CodeMethod sourceMethod = this.parent.getSourceCodeObject();
      if (sourceMethod != null) {
        this.sourceCodeObject = sourceMethod.getReturns();
      }
    }
    return this.sourceCodeObject;
  }

  @Override
  public CodeReturn merge(CodeReturn other, CodeMergeStrategy strategy) {

    doMerge(other, strategy);
    return this;
  }

  @Override
  public BaseReturn copy() {

    return copy(getDefaultCopyMapper());
  }

  @Override
  public BaseReturn copy(CodeCopyMapper mapper) {

    return new BaseReturn(this, mapper);
  }

}
