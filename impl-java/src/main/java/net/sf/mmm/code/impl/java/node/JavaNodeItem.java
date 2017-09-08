/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.node;

import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.base.AbstractCodeNodeItem;
import net.sf.mmm.code.impl.java.JavaContext;
import net.sf.mmm.code.impl.java.item.JavaItem;
import net.sf.mmm.code.impl.java.source.JavaSource;

/**
 * Implementation of {@link CodeItem} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class JavaNodeItem extends AbstractCodeNodeItem implements JavaNode, JavaItem {

  /**
   * The constructor.
   */
  public JavaNodeItem() {

    super();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaNodeItem} to copy.
   */
  public JavaNodeItem(JavaNodeItem template) {

    super();
  }

  @Override
  public JavaContext getContext() {

    return getParent().getContext();
  }

  @Override
  public JavaSource getSource() {

    return getParent().getSource();
  }

}
