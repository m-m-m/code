/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.item;

import net.sf.mmm.code.api.CodeWithContext;

/**
 * {@link CodeMutableItem} with an owning {@link #getContext() context}. It is similar to a
 * {@link net.sf.mmm.code.api.node.CodeNodeItem} but is not a {@link net.sf.mmm.code.api.node.CodeNode}.
 * Therefore, as single instance can be added to the same AST in multiple places.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 * @see net.sf.mmm.code.api.annotation.CodeAnnotation
 */
public interface CodeChildItem extends CodeMutableItem, CodeWithContext {

  @Override
  CodeChildItem copy();

}
