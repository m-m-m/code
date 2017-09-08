/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.node;

import net.sf.mmm.code.api.CodeContext;
import net.sf.mmm.code.api.source.CodeSource;

/**
 * A node of the abstract syntax tree (AST) that is connected with its {@link #getParent() parent}. It can be
 * mutable
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeNode {

  /**
   * @return the parent of this {@link CodeNode}. May only be {@code null} for instances of
   *         {@link CodeContext}.
   */
  CodeNode getParent();

  /**
   * @return the owning {@link CodeContext}. In case of a {@link CodeContext} this method will return the
   *         object itself ({@code this}).
   */
  default CodeContext getContext() {

    return getSource().getContext();
  }

  /**
   * @return the owning {@link CodeSource}. Shall never be {@code null}.
   */
  CodeSource getSource();

}
