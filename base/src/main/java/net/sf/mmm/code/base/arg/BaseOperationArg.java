/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.arg;

import java.io.IOException;
import java.lang.reflect.Type;

import net.sf.mmm.code.api.arg.CodeOperationArg;
import net.sf.mmm.code.api.syntax.CodeSyntax;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.base.element.BaseElementImpl;
import net.sf.mmm.code.base.member.BaseOperation;
import net.sf.mmm.code.base.node.BaseNode;
import net.sf.mmm.code.base.type.BaseGenericType;

/**
 * Base implementation of {@link CodeOperationArg}.
 *
 * @author hohwille
 * @since 1.0.0
 */
public abstract class BaseOperationArg extends BaseElementImpl implements CodeOperationArg {

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
   */
  public BaseOperationArg(BaseOperationArg template) {

    super(template);
    this.type = template.type;
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
    getType();
  }

  @Override
  public abstract BaseNode getParent();

  @Override
  public abstract BaseOperation getDeclaringOperation();

  @Override
  public BaseGenericType getType() {

    if (this.type == null) {
      Type reflectiveObjectType = getReflectiveObjectType();
      if (reflectiveObjectType != null) {
        this.type = getContext().getType(reflectiveObjectType, getDeclaringOperation());
      } else {
        this.type = getContext().getRootType();
      }
    }
    return this.type;
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

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeSyntax syntax) throws IOException {

    doWriteAnnotations(sink, newline, null, "", syntax);
    doWriteType(sink);
  }

  /**
   * @param sink the {@link Appendable}.
   * @throws IOException if thrown by {@link Appendable}.
   */
  protected void doWriteType(Appendable sink) throws IOException {

    getType().writeReference(sink, false);
  }

}
