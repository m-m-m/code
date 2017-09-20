/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.element;

import net.sf.mmm.code.api.element.CodeElement;
import net.sf.mmm.code.impl.java.annotation.JavaAnnotations;
import net.sf.mmm.code.impl.java.node.JavaNode;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Interface for {@link CodeElement} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface JavaElementNode extends CodeElement, JavaNode {

  @Override
  JavaAnnotations getAnnotations();

  @Override
  JavaType getDeclaringType();

  @Override
  JavaElementNode copy();

  /**
   * @return the optional reflective object of this item such as {@link Class}. May be {@code null} (e.g. if
   *         this object was created from source-code only or has been created dynamically). However, if
   *         available it can be helpful for analysis especially in case of type-safe
   *         {@link java.lang.annotation.Annotation} processing. In most cases the generic type will be
   *         derived from {@link java.lang.reflect.AnnotatedElement} but in specific cases it can also be
   *         {@link java.security.ProtectionDomain} or other types that have no common parent-type. Therefore
   *         this generic type is unbounded here.
   */
  Object getReflectiveObject();

}
