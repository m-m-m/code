/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.element;

import net.sf.mmm.code.api.annotation.CodeAnnotations;
import net.sf.mmm.code.api.doc.CodeDoc;
import net.sf.mmm.code.api.node.CodeNode;
import net.sf.mmm.code.api.node.CodeNodeItemWithComment;
import net.sf.mmm.code.api.node.CodeNodeItemWithDeclaringType;
import net.sf.mmm.code.api.node.CodeNodeItemWithType;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * {@link CodeNodeItemWithType} that might be {@link #getAnnotations() annotated} or {@link #getDoc()
 * documented}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeElement extends CodeNodeItemWithComment, CodeNodeItemWithDeclaringType {

  /**
   * @return the {@link CodeDoc documentation} of this element. May be {@link CodeDoc#isEmpty() empty} but
   *         will never be {@code null}.
   */
  CodeDoc getDoc();

  /**
   * @return the {@link CodeAnnotations} with the {@link net.sf.mmm.code.api.annotation.CodeAnnotation}s of
   *         this element.
   */
  CodeAnnotations<?> getAnnotations();

  /**
   * Destroys this node and disconnects it from its parent.
   *
   * @throws ReadOnlyException if this {@link CodeNode} or its {@link #getParent() parent} is
   *         {@link #isImmutable() immutable}.
   */
  void removeFromParent();

  @Override
  CodeElement copy();

}
