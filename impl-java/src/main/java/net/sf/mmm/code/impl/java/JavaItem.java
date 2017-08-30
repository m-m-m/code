/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import net.sf.mmm.code.api.CodeItem;
import net.sf.mmm.code.base.AbstractCodeItem;

/**
 * Implementation of {@link CodeItem} for Java reflection.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class JavaItem extends AbstractCodeItem<JavaContext> {

  /**
   * The constructor.
   *
   * @param context the {@link #getContext() context}.
   */
  public JavaItem(JavaContext context) {

    super(context);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaItem} to copy.
   */
  public JavaItem(JavaItem template) {

    super(template);
  }

}
