/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.arg;

import java.io.IOException;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Executable;
import java.util.List;

import net.sf.mmm.code.api.arg.CodeExceptions;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.syntax.CodeSyntax;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.base.member.BaseOperation;

/**
 * Base implementation of {@link CodeExceptions}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseExceptions extends BaseOperationArgs<BaseException>
    implements CodeExceptions<BaseException>, CodeNodeItemWithGenericParent<BaseOperation, BaseExceptions> {

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
   * @param parent the {@link #getParent() parent}.
   */
  public BaseExceptions(BaseExceptions template, BaseOperation parent) {

    super(template, parent);
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
    Executable reflectiveObject = getParent().getReflectiveObject();
    if (reflectiveObject != null) {
      List<BaseException> list = getList();
      for (AnnotatedType exceptionType : reflectiveObject.getAnnotatedExceptionTypes()) {
        BaseException exception = new BaseException(this, exceptionType);
        list.add(exception);
      }
    }
  }

  @Override
  public BaseException get(CodeGenericType type) {

    for (BaseException exception : getDeclared()) {
      if (exception.getType().equals(type)) {
        return exception;
      }
    }
    return null;
  }

  @Override
  public BaseException add(CodeGenericType type) {

    verifyMutalbe();
    BaseException exception = new BaseException(this, null);
    exception.setType(type);
    add(exception);
    return exception;
  }

  @Override
  public BaseExceptions getSourceCodeObject() {

    BaseOperation sourceOperation = getParent().getSourceCodeObject();
    if (sourceOperation != null) {
      return sourceOperation.getExceptions();
    }
    return null;
  }

  @Override
  public BaseExceptions copy() {

    return copy(getParent());
  }

  @Override
  public BaseExceptions copy(BaseOperation newParent) {

    return new BaseExceptions(this, newParent);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeSyntax syntax) throws IOException {

    List<BaseException> list = getList();
    if (list.isEmpty()) {
      return;
    }
    String prefix = " throws ";
    for (BaseException exception : list) {
      sink.append(prefix);
      exception.write(sink, newline, null, "", syntax);
      prefix = ", ";
    }
  }

}