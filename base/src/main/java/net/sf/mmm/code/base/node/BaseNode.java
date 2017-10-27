/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.node;

import net.sf.mmm.code.api.node.CodeNode;
import net.sf.mmm.code.base.BaseContext;
import net.sf.mmm.code.base.source.BaseSource;

/**
 * Base implementation of {@link CodeNode}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface BaseNode extends CodeNode {

  @Override
  BaseNode getParent();

  @Override
  default BaseContext getContext() {

    return getParent().getContext();
  }

  @Override
  default BaseSource getSource() {

    return getParent().getSource();
  }

}
