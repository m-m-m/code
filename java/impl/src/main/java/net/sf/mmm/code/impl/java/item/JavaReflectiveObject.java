/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.item;

/**
 * Interface for any {@link JavaItem} or {@link net.sf.mmm.code.impl.java.node.JavaNode} that may have a
 * {@link #getReflectiveObject()}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <R> type of {@link #getReflectiveObject()}
 * @since 1.0.0
 */
public interface JavaReflectiveObject<R> {

  /**
   * @return the optional reflective object of this item such as {@link Class}. May be {@code null} (e.g. if
   *         this object was created from source-code only or has been created dynamically). However, if
   *         available it can be helpful for analysis especially in case of type-safe
   *         {@link java.lang.annotation.Annotation} processing. In most cases the generic type will be
   *         derived from {@link java.lang.reflect.AnnotatedElement} but in specific cases it can also be
   *         {@link java.security.ProtectionDomain} or other types that have no common parent-type. Therefore
   *         this generic type is unbounded here.
   */
  R getReflectiveObject();

}
