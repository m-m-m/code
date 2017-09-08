/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.member;

import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * {@link CodeOperation} representing a constructor of a {@link CodeType}.
 *
 * @see CodeType#getConstructors()
 * @see CodeConstructors#getAll()
 * @see java.lang.reflect.Constructor
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeConstructor extends CodeOperation {

  @Override
  CodeConstructors getParent();

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

  @Override
  CodeConstructor copy();

}
