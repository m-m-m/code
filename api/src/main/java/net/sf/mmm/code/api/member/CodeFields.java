/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.member;

import net.sf.mmm.code.api.CodeGenericType;
import net.sf.mmm.util.exception.api.DuplicateObjectException;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * {@link CodeMembers} as a container for the {@link CodeField}s.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeFields extends CodeMembers<CodeField> {

  /**
   * @param name the {@link CodeField#getName() name} of the requested {@link CodeField}.
   * @return the {@link CodeField} from the {@link #getDeclared() declared fields} with the given {@code name}
   *         or {@code null} if no such field exists.
   */
  CodeField getDeclared(String name);

  /**
   * @param name the {@link CodeField#getName() name} of the requested {@link CodeField}.
   * @return the {@link CodeField} from {@link #getAll() all fields} with the given {@code name} or
   *         {@code null} if no such field exists.
   */
  CodeField get(String name);

  /**
   * @param name the {@link CodeField#getName() field name}.
   * @param type the {@link CodeField#getType() field type}.
   * @return a new {@link CodeField} that has been added to {@link #getDeclared()}.
   * @throws ReadOnlyException if {@link #isImmutable() immutable}.
   * @throws DuplicateObjectException if a {@link CodeField} with the same {@link CodeField#getName() field
   *         name} is already {@link #getDeclared() declared}.
   */
  CodeField add(String name, CodeGenericType type);
}
