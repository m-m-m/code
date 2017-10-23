/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.node;

import net.sf.mmm.code.api.node.CodeNodeItem;
import net.sf.mmm.code.impl.java.JavaContext;
import net.sf.mmm.code.impl.java.item.JavaItem;
import net.sf.mmm.code.impl.java.source.JavaSource;

/**
 * {@link CodeNodeItem} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface JavaNodeItem extends CodeNodeItem, JavaNode, JavaItem {

  @Override
  default JavaContext getContext() {

    return getParent().getContext();
  }

  @Override
  default JavaSource getSource() {

    return getParent().getSource();
  }
}
