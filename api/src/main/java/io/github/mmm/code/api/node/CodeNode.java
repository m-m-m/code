/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.node;

import io.github.mmm.code.api.CodeContext;
import io.github.mmm.code.api.CodeWithContext;

/**
 * A node of the abstract syntax tree (AST) that is connected with its {@link #getParent() parent}. It can be
 * mutable
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeNode extends CodeWithContext {

  /**
   * @return the parent of this {@link CodeNode}. May only be {@code null} for instances of
   *         {@link CodeContext}.
   */
  CodeNode getParent();

}
