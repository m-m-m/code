/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.item;

import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.object.CodeMutable;

/**
 * {@link CodeItem} that can be {@link #isMutable() mutable}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeMutableItem extends CodeMutable, CodeItem {

  /**
   * Makes this item {@link #isImmutable() immutable}. Can not be undone. Multiple calls will have no further effect.
   */
  void setImmutable();

  /**
   * @return the optional reflective object of this item such as {@link Class}. May be {@code null} (e.g. if this object
   *         was created from source-code only or has been created dynamically). However, if available it can be helpful
   *         for analysis especially in case of type-safe {@link java.lang.annotation.Annotation} processing. In most
   *         cases the generic type will be derived from {@link java.lang.reflect.AnnotatedElement} but in specific
   *         cases it can also be {@link java.security.ProtectionDomain} or other types that have no common parent-type.
   *         Therefore this generic type is unbounded here.
   */
  Object getReflectiveObject();

  /**
   * @return a new {@link #isMutable() mutable} copy of this {@link CodeMutableItem}. Will be a deep-copy with copies of
   *         all child {@link CodeMutableItem}s.
   */
  CodeMutableItem copy();

  /**
   * @param mapper the {@link CodeCopyMapper} used to map involved nodes during copy.
   * @return a {@link #copy()} with the related objects mapped using the given {@link CodeCopyMapper}.
   */
  CodeMutableItem copy(CodeCopyMapper mapper);

}
