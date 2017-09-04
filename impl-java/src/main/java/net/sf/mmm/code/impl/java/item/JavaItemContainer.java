/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.item;

import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.item.CodeItemContainer;
import net.sf.mmm.code.api.item.CodeItemWithComment;
import net.sf.mmm.code.impl.java.JavaContext;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link CodeItemWithComment} for Java reflection.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <I> the type of the contained {@link JavaItem}s.
 * @since 1.0.0
 */
public abstract class JavaItemContainer<I extends CodeItem> extends JavaItem implements CodeItemContainer<I> {

  private final JavaType declaringType;

  /**
   * The constructor.
   *
   * @param context the {@link #getContext() context}.
   */
  protected JavaItemContainer(JavaContext context) {

    super(context);
    this.declaringType = null;
  }

  /**
   * The constructor.
   *
   * @param declaringType the {@link #getDeclaringType() declaring type}.
   */
  public JavaItemContainer(JavaType declaringType) {

    super(declaringType.getContext());
    this.declaringType = declaringType;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaItemContainer} to copy.
   */
  public JavaItemContainer(JavaItemContainer<I> template) {

    this(template, template.declaringType);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaItemContainer} to copy.
   * @param declaringType the {@link #getDeclaringType() declaring type}.
   */
  public JavaItemContainer(JavaItemContainer<I> template, JavaType declaringType) {

    super(template);
    this.declaringType = declaringType;
  }

  @Override
  public JavaType getDeclaringType() {

    return this.declaringType;
  }
}
