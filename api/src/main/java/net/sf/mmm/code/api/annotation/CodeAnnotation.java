/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.annotation;

import java.util.Map;

import net.sf.mmm.code.api.element.CodeElement;
import net.sf.mmm.code.api.item.CodeItemWithDeclaringElement;
import net.sf.mmm.code.api.item.CodeItemWithType;
import net.sf.mmm.code.api.type.CodeType;

/**
 * {@link CodeItemWithType} that represents an {@link java.lang.annotation.Annotation} instance.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeAnnotation extends CodeItemWithType, CodeItemWithDeclaringElement {

  /**
   * @return the {@link Map} with the parameters of this annotation. May be {@link Map#isEmpty() empty} but is
   *         never <code>null</code>.
   */
  Map<String, Object> getParameters();

  @Override
  CodeType getType();

  /**
   * @param newDeclaringElement the new {@link #getDeclaringElement() declaring element}.
   * @return the new {@link #isImmutable() mutable} copy.
   */
  @Override
  CodeAnnotation copy(CodeElement newDeclaringElement);

}
