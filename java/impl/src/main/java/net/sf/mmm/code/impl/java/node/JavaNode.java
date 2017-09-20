/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.node;

import net.sf.mmm.code.api.node.CodeNode;
import net.sf.mmm.code.impl.java.JavaContext;
import net.sf.mmm.code.impl.java.source.JavaSource;

/**
 * Implementation of {@link CodeNode} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface JavaNode extends CodeNode {

  @Override
  JavaNode getParent();

  @Override
  default JavaContext getContext() {

    return getParent().getContext();
  }

  @Override
  default JavaSource getSource() {

    return getParent().getSource();
  }

}
