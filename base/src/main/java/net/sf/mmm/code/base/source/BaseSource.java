/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.source;

import net.sf.mmm.code.api.source.CodeSource;
import net.sf.mmm.code.base.BaseContext;
import net.sf.mmm.code.base.BaseProvider;
import net.sf.mmm.code.base.node.BaseContainer;

/**
 * Base implementation of {@link CodeSource}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface BaseSource extends CodeSource, BaseProvider, BaseContainer {

  @Override
  BaseSource getParent();

  @Override
  default BaseContext getContext() {

    return getParent().getContext();
  }

}
