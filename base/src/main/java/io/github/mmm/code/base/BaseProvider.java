/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base;

import io.github.mmm.base.exception.ObjectNotFoundException;
import io.github.mmm.code.api.CodeName;
import io.github.mmm.code.api.CodeProvider;
import io.github.mmm.code.api.type.CodeType;
import io.github.mmm.code.base.loader.BaseLoader;
import io.github.mmm.code.base.source.BaseSource;
import io.github.mmm.code.base.type.BaseType;

/**
 * Base interface for {@link CodeProvider}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface BaseProvider extends CodeProvider, BaseLoader {

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
