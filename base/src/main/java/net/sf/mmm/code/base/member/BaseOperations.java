/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.member;

import net.sf.mmm.code.api.member.CodeMembers;
import net.sf.mmm.code.api.member.CodeOperation;
import net.sf.mmm.code.api.member.CodeOperations;
import net.sf.mmm.code.base.type.BaseType;

/**
 * Implementation of {@link CodeMembers} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <O> type of the contained {@link CodeOperation}s.
 * @since 1.0.0
 */
public abstract class BaseOperations<O extends BaseOperation> extends BaseMembers<O> implements CodeOperations<O> {

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public BaseOperations(BaseType parent) {

    super(parent);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseOperations} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public BaseOperations(BaseOperations<O> template, BaseType parent) {

    super(template, parent);
  }

  @Override
  public abstract BaseOperations<O> getSourceCodeObject();

  @Override
  public abstract BaseOperations<O> copy();

}
