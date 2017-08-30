/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api;

import java.util.List;

import net.sf.mmm.code.api.doc.CodeDoc;

/**
 * {@link CodeItemWithType} that might be {@link #getAnnotations() annotated} or {@link #getDoc() documented}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeElement extends CodeItemWithComment {

  /**
   * @return the {@link CodeDoc documentation} of this element. May be {@link CodeDoc#isEmpty() empty} but
   *         will never be {@code null}.
   */
  CodeDoc getDoc();

  /**
   * @return a {@link List} with the {@link CodeAnnotation}s of this element.
   */
  List<CodeAnnotation> getAnnotations();

  /**
   * @return the {@link CodeType} declaring this {@link CodeElement}. Here declaring means owning and
   *         containing. May only be {@code null} for {@link CodeType} and for {@link CodePackage} (where it
   *         will always be {@code null}).
   * @see CodeType#getDeclaringType()
   */
  CodeType getDeclaringType();

}
