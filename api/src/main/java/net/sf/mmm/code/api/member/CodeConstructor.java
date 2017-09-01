/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.member;

import net.sf.mmm.code.api.CodeType;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * Represents a {@link java.lang.reflect.Constructor} of a {@link CodeType}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeConstructor extends CodeOperation {

  /**
   * @deprecated the {@link #getName() name} of a {@link CodeConstructor} has to be equal to the
   *             {@link CodeType#getSimpleName() simple name} of its {@link #getDeclaringType() declaring
   *             type}.
   */
  @Deprecated
  @Override
  default void setName(String name) {

    throw new ReadOnlyException(getClass().getSimpleName(), "name");
  }

}
