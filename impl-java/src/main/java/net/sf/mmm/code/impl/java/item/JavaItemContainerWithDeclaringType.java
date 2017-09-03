/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.item;

import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.item.CodeItemWithComment;
import net.sf.mmm.code.api.item.CodeItemWithDeclaringType;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link CodeItemWithComment} for Java reflection.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <I> the type of the contained {@link JavaItem}s.
 * @since 1.0.0
 */
public abstract class JavaItemContainerWithDeclaringType<I extends CodeItem> extends JavaItemContainer<I> implements CodeItemWithDeclaringType {

  private JavaType declaringType;

  /**
   * The constructor.
   *
   * @param declaringType the {@link #getDeclaringType() declaring type}.
   */
  public JavaItemContainerWithDeclaringType(JavaType declaringType) {

    super(declaringType.getContext());
    this.declaringType = declaringType;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaItemContainerWithDeclaringType} to copy.
   */
  public JavaItemContainerWithDeclaringType(JavaItemContainerWithDeclaringType<I> template) {

    super(template);
    this.declaringType = template.declaringType;
  }

  @Override
  public JavaType getDeclaringType() {

    return this.declaringType;
  }
}
