/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.arg;

import net.sf.mmm.code.api.arg.CodeOperationArgs;
import net.sf.mmm.code.impl.java.member.JavaOperation;
import net.sf.mmm.code.impl.java.node.JavaNodeItemContainerFlat;

/**
 * Implementation of {@link CodeOperationArgs} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <A> type of the contained {@link JavaOperationArg}s.
 * @since 1.0.0
 */
public abstract class JavaOperationArgs<A extends JavaOperationArg> extends JavaNodeItemContainerFlat<A> implements CodeOperationArgs<A> {

  private final JavaOperation parent;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public JavaOperationArgs(JavaOperation parent) {

    super();
    this.parent = parent;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaOperationArgs} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaOperationArgs(JavaOperationArgs<A> template, JavaOperation parent) {

    super(template);
    this.parent = parent;
  }

  @Override
  public JavaOperation getParent() {

    return this.parent;
  }

  @Override
  public abstract JavaOperationArgs<A> copy();

}
