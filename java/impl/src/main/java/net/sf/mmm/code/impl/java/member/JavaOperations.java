/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.member;

import net.sf.mmm.code.api.member.CodeMembers;
import net.sf.mmm.code.api.member.CodeOperation;
import net.sf.mmm.code.api.member.CodeOperations;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link CodeMembers} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <O> type of the contained {@link CodeOperation}s.
 * @since 1.0.0
 */
public abstract class JavaOperations<O extends JavaOperation> extends JavaMembers<O> implements CodeOperations<O> {

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public JavaOperations(JavaType parent) {

    super(parent);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaOperations} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaOperations(JavaOperations<O> template, JavaType parent) {

    super(template, parent);
  }

  @Override
  public abstract JavaOperations<O> copy();

}
