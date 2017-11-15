/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.arg;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;

import net.sf.mmm.code.api.arg.CodeReturn;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.base.member.BaseMethod;
import net.sf.mmm.code.base.member.BaseOperation;
import net.sf.mmm.code.base.type.BaseType;

/**
 * Base implementation of {@link CodeReturn}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseReturn extends BaseOperationArg implements CodeReturn, CodeNodeItemWithGenericParent<BaseMethod, BaseReturn> {

  private final BaseMethod parent;

  private final AnnotatedType reflectiveObject;

  private BaseReturn sourceCodeObject;

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
   * @param parent the {@link #getParent() parent}.
   */
  public BaseReturn(BaseReturn template, BaseMethod parent) {

    super(template);
    this.parent = parent;
    this.reflectiveObject = null;
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
  public BaseReturn getSourceCodeObject() {

    if (this.sourceCodeObject == null) {
      BaseMethod sourceMethod = this.parent.getSourceCodeObject();
      if (sourceMethod != null) {
        this.sourceCodeObject = sourceMethod.getReturns();
      }
    }
    return this.sourceCodeObject;
  }

  @Override
  public BaseReturn copy() {

    return copy(this.parent);
  }

  @Override
  public BaseReturn copy(BaseMethod newParent) {

    return new BaseReturn(this, newParent);
  }

}
