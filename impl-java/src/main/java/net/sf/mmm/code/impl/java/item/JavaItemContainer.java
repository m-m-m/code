/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.item;

import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.item.CodeItemContainer;
import net.sf.mmm.code.api.item.CodeItemWithComment;
import net.sf.mmm.code.impl.java.JavaContext;

/**
 * Implementation of {@link CodeItemWithComment} for Java reflection.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <I> the type of the contained {@link JavaItem}s.
 * @since 1.0.0
 */
public abstract class JavaItemContainer<I extends CodeItem> extends JavaItem implements CodeItemContainer<I> {

  /**
   * The constructor.
   *
   * @param context the {@link #getContext() context}.
   */
  public JavaItemContainer(JavaContext context) {

    super(context);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaItemContainer} to copy.
   */
  public JavaItemContainer(JavaItemContainer<I> template) {

    super(template);
  }

}
