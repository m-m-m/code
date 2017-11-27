/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base;

import net.sf.mmm.code.api.CodeName;
import net.sf.mmm.code.api.CodeProvider;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.base.loader.BaseLoader;
import net.sf.mmm.code.base.node.BaseNode;
import net.sf.mmm.code.base.source.BaseSource;
import net.sf.mmm.code.base.type.BaseType;
import net.sf.mmm.util.exception.api.ObjectNotFoundException;

/**
 * Base interface for {@link CodeProvider}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface BaseProvider extends CodeProvider, BaseLoader, BaseNode {

  @Override
  default BaseContext getContext() {

    return (BaseContext) getParent().getContext();
  }

  @Override
  BaseSource getSource();

  @Override
  BaseType getType(CodeName qualifiedName);

  @Override
  default BaseType getRequiredType(String qualifiedName) {

    BaseType type = getType(qualifiedName);
    if (type == null) {
      throw new ObjectNotFoundException(CodeType.class, qualifiedName);
    }
    return type;
  }

}
