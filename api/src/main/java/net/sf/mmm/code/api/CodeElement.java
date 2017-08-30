/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api;

import java.util.List;

import net.sf.mmm.code.api.doc.CodeDoc;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * {@link CodeItemWithType} that might be {@link #getAnnotations() annotated} or {@link #getDoc() documented}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeElement extends CodeItemWithComment {

  /**
   * @return the {@link CodeDoc documentation} of this element. May be empty but will never be {@code null}.
   */
  CodeDoc getDoc();

  /**
   * @param doc the new {@link #getDoc() documentation}.
   * @throws ReadOnlyException if {@link #isImmutable() immutable}.
   */
  void setDoc(CodeDoc doc);

  /**
   * @return a {@link List} with the {@link CodeAnnotation}s of this element.
   */
  List<CodeAnnotation> getAnnotations();

}
