/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.item;

import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.item.CodeItemContainerWithInheritance;
import net.sf.mmm.code.api.item.CodeItemWithComment;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link CodeItemWithComment} for Java reflection.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <I> the type of the contained {@link JavaItem}s.
 * @since 1.0.0
 */
public abstract class JavaItemContainerWithInheritance<I extends CodeItem> extends JavaItemContainerWithDeclaringType<I>
    implements CodeItemContainerWithInheritance<I> {

  /**
   * The constructor.
   *
   * @param declaringType the {@link #getDeclaringType() declaring type}.
   */
  public JavaItemContainerWithInheritance(JavaType declaringType) {

    super(declaringType);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaItemContainerWithInheritance} to copy.
   */
  public JavaItemContainerWithInheritance(JavaItemContainerWithInheritance<I> template) {

    super(template);
  }

}
