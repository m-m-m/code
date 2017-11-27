/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.arg;

import net.sf.mmm.code.api.arg.CodeOperationArg;
import net.sf.mmm.code.api.arg.CodeOperationArgs;
import net.sf.mmm.code.api.copy.CodeCopyMapper;
import net.sf.mmm.code.base.member.BaseOperation;
import net.sf.mmm.code.base.node.BaseNodeItemContainerFlat;
import net.sf.mmm.code.base.type.BaseType;

/**
 * Base implementation of {@link CodeOperationArgs}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <A> type of the contained {@link BaseOperationArg}s.
 * @since 1.0.0
 */
public abstract class BaseOperationArgs<A extends CodeOperationArg> extends BaseNodeItemContainerFlat<A> implements CodeOperationArgs<A> {

  private final BaseOperation parent;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public BaseOperationArgs(BaseOperation parent) {

    super();
    this.parent = parent;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseOperationArgs} to copy.
   * @param parent the {@link #getParent() parent}.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseOperationArgs(BaseOperationArgs<A> template, BaseOperation parent, CodeCopyMapper mapper) {

    super(template, mapper);
    this.parent = parent;
  }

  @Override
  public BaseOperation getParent() {

    return this.parent;
  }

  @Override
  public BaseType getDeclaringType() {

    return this.parent.getDeclaringType();
  }

  @Override
  public abstract CodeOperationArgs<A> getSourceCodeObject();

  @Override
  public abstract BaseOperationArgs<A> copy();

}
