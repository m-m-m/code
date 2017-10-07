/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.item;

import net.sf.mmm.code.api.item.CodeChildItem;
import net.sf.mmm.code.base.item.AbstractCodeMutableItem;
import net.sf.mmm.code.impl.java.JavaContext;

/**
 * Implementation of {@link CodeChildItem} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class JavaChildItem extends AbstractCodeMutableItem implements CodeChildItem, JavaItem {

  private final JavaContext context;

  /**
   * The constructor.
   *
   * @param context the {@link #getContext() context}.
   */
  public JavaChildItem(JavaContext context) {

    super();
    this.context = context;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaChildItem} to copy.
   */
  public JavaChildItem(JavaChildItem template) {

    super(template);
    this.context = template.context;
  }

  @Override
  public JavaContext getContext() {

    return this.context;
  }

}
