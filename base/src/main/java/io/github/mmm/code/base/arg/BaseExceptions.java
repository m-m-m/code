/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.arg;

import java.io.IOException;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Executable;
import java.util.List;

import io.github.mmm.code.api.arg.CodeException;
import io.github.mmm.code.api.arg.CodeExceptions;
import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.language.CodeLanguage;
import io.github.mmm.code.api.member.CodeOperation;
import io.github.mmm.code.api.merge.CodeMergeStrategy;
import io.github.mmm.code.api.type.CodeGenericType;
import io.github.mmm.code.base.member.BaseOperation;

/**
 * Base implementation of {@link CodeExceptions}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseExceptions extends BaseOperationArgs<CodeException> implements CodeExceptions {

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public BaseExceptions(BaseOperation parent) {

    super(parent);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseExceptions} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseExceptions(BaseExceptions template, CodeCopyMapper mapper) {

    super(template, mapper);
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
    Executable reflectiveObject = getParent().getReflectiveObject();
    if (reflectiveObject != null) {
      List<CodeException> list = getList();
      for (AnnotatedType exceptionType : reflectiveObject.getAnnotatedExceptionTypes()) {
        BaseException exception = new BaseException(this, exceptionType);
        list.add(exception);
      }
    }
  }

  @Override
  public CodeException get(CodeGenericType type) {

    for (CodeException exception : getDeclared()) {
      if (exception.getType().equals(type)) {
        return exception;
      }
    }
    return null;
  }

  @Override
  public CodeException add(CodeGenericType type) {

    verifyMutalbe();
    BaseException exception = new BaseException(this, null);
    exception.setType(type);
    add(exception);
    return exception;
  }

  @Override
  public CodeExceptions getSourceCodeObject() {

    CodeOperation sourceOperation = getParent().getSourceCodeObject();
    if (sourceOperation != null) {
      return sourceOperation.getExceptions();
    }
    return null;
  }

  @Override
  public CodeExceptions merge(CodeExceptions o, CodeMergeStrategy strategy) {

    if (strategy == CodeMergeStrategy.KEEP) {
      return this;
    }
    BaseExceptions other = (BaseExceptions) o;
    if (strategy == CodeMergeStrategy.OVERRIDE) {
      clear();
      for (CodeException otherException : other.getDeclared()) {
        CodeException copy = doCopyNode(otherException, this);
        add(copy);
      }
    } else {
      for (CodeException otherException : other.getDeclared()) {
        CodeException myException = get(otherException.getType());
        if (myException == null) {
          CodeException copy = doCopyNode(otherException, this);
          add(copy);
        } else {
          myException.merge(otherException, strategy);
        }
      }
    }
    return this;
  }

  @Override
  public BaseExceptions copy() {

    return copy(getDefaultCopyMapper());
  }

  @Override
  public BaseExceptions copy(CodeCopyMapper mapper) {

    return new BaseExceptions(this, mapper);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    List<CodeException> list = getList();
    if (list.isEmpty()) {
      return;
    }
    String prefix = " throws ";
    for (CodeException exception : list) {
      sink.append(prefix);
      exception.write(sink, newline, null, "", language);
      prefix = ", ";
    }
  }

}
