/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.element;

import io.github.mmm.code.api.annotation.CodeAnnotations;
import io.github.mmm.code.api.doc.CodeDoc;
import io.github.mmm.code.api.item.CodeMutableItemWithComment;
import io.github.mmm.code.api.item.CodeMutableItemWithType;
import io.github.mmm.code.api.node.CodeNodeItem;

/**
 * {@link CodeMutableItemWithType} that can be {@link #getAnnotations() annotated}, {@link #getDoc() documented}, or
 * {@link #getComment() commented}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeElement extends CodeNodeItem, CodeMutableItemWithComment {

  /**
   * @return the {@link CodeDoc documentation} of this element. May be {@link CodeDoc#isEmpty() empty} but will never be
   *         {@code null}.
   */
  CodeDoc getDoc();

  /**
   * @return the {@link CodeAnnotations} with the {@link io.github.mmm.code.api.annotation.CodeAnnotation}s of this
   *         element.
   */
  CodeAnnotations getAnnotations();

  /**
   * Destroys this node and disconnects it from its parent.
   *
   * @throws io.github.mmm.base.exception.ReadOnlyException if {@link #isImmutable() immutable}.
   */
  void removeFromParent();

  @Override
  CodeElement copy();

}
