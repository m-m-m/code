/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.arg;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;

import net.sf.mmm.code.api.arg.CodeException;
import net.sf.mmm.code.api.arg.CodeExceptions;
import net.sf.mmm.code.api.copy.CodeCopyMapper;
import net.sf.mmm.code.api.copy.CodeCopyMapperNone;
import net.sf.mmm.code.api.merge.CodeMergeStrategy;
import net.sf.mmm.code.base.member.BaseOperation;
import net.sf.mmm.code.base.type.BaseType;

/**
 * Base implementation of {@link CodeException}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseException extends BaseOperationArg implements CodeException {

  private final BaseExceptions parent;

  private final AnnotatedType reflectiveObject;

  private CodeException sourceCodeObject;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May be {@code null}.
   */
  public BaseException(BaseExceptions parent, AnnotatedType reflectiveObject) {

    super();
    this.parent = parent;
    this.reflectiveObject = reflectiveObject;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseException} to copy.
   * @param parent the {@link #getParent() parent}.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseException(BaseException template, BaseExceptions parent, CodeCopyMapper mapper) {

    super(template, mapper);
    this.parent = parent;
    this.reflectiveObject = null;
  }

  @Override
  public BaseExceptions getParent() {

    return this.parent;
  }

  @Override
  public BaseOperation getDeclaringOperation() {

    return this.parent.getParent();
  }

  @Override
  public BaseType getDeclaringType() {

    return getParent().getDeclaringType();
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
  public CodeException getSourceCodeObject() {

    if ((this.sourceCodeObject == null) && !isInitialized()) {
      CodeExceptions sourceExceptions = this.parent.getSourceCodeObject();
      if (sourceExceptions != null) {
        String exceptionTypeName = getType().getQualifiedName();
        for (CodeException sourceException : sourceExceptions.getDeclared()) {
          if (exceptionTypeName.equals(sourceException.getType().getQualifiedName())) {
            this.sourceCodeObject = sourceException;
            break;
          }
        }
      }
    }
    return this.sourceCodeObject;
  }

  @Override
  public CodeException merge(CodeException other, CodeMergeStrategy strategy) {

    doMerge(other, strategy);
    return this;
  }

  @Override
  public BaseException copy() {

    return copy(this.parent);
  }

  @Override
  public BaseException copy(CodeExceptions newParent) {

    return copy(newParent, CodeCopyMapperNone.INSTANCE);
  }

  @Override
  public BaseException copy(CodeExceptions newParent, CodeCopyMapper mapper) {

    return new BaseException(this, (BaseExceptions) newParent, mapper);
  }

}
