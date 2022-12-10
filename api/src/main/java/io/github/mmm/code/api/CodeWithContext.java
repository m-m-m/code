/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api;

import io.github.mmm.code.api.node.CodeNode;
import io.github.mmm.code.api.source.CodeSource;

/**
 * Any code object that has an owning {@link #getContext() context}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 * @see CodeNode
 */
public abstract interface CodeWithContext {

  /**
   * @return the owning {@link CodeContext}. In case of a {@link CodeContext} this method will return the object itself
   *         ({@code this}).
   */
  CodeContext getContext();

  /**
   * @return the owning {@link CodeSource}. Shall never be {@code null}.
   */
  CodeSource getSource();

}
