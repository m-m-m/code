/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.annotation;

import net.sf.mmm.code.api.element.CodeElement;
import net.sf.mmm.code.api.item.CodeItemContainerWithInheritance;
import net.sf.mmm.code.api.item.CodeItemWithDeclaringElement;
import net.sf.mmm.code.api.type.CodeType;

/**
 * {@link CodeItemContainerWithInheritance} containing {@link CodeAnnotation}s.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeAnnotations extends CodeItemContainerWithInheritance<CodeAnnotation>, CodeItemWithDeclaringElement {

  @Override
  CodeAnnotations copy(CodeType newDeclaringType);

  /**
   * @param newDeclaringElement the new {@link #getDeclaringElement() declaring element}.
   * @return the new {@link #isImmutable() mutable} copy.
   */
  @Override
  CodeAnnotations copy(CodeElement newDeclaringElement);

}
