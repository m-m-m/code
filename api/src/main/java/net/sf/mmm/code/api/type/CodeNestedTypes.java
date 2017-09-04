/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.type;

import net.sf.mmm.code.api.item.CodeItemContainerWithInheritance;
import net.sf.mmm.code.api.item.CodeItemContainerWithInheritanceAndName;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * {@link CodeItemContainerWithInheritance} containing the nested {@link CodeType}s.
 *
 * @see Class#getDeclaringClass()
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeNestedTypes extends CodeItemContainerWithInheritanceAndName<CodeType> {

  /**
   * @param name the {@link CodeType#getSimpleName() simple name} of the requested {@link CodeType}.
   * @return the nested {@link CodeType} from {@link #getDeclared()} or {@code null} if not found.
   */
  CodeType getDeclared(String name);

  /**
   * @param name the {@link CodeType#getSimpleName() simple name} of the requested {@link CodeType}.
   * @return the nested {@link CodeType} from {@link #getAll()} or {@code null} if not found.
   */
  CodeType get(String name);

  /**
   * @param name the {@link CodeType#getSimpleName() simple name} of the {@link CodeType} to create.
   * @return a new {@link CodeType} that has been added.
   * @throws ReadOnlyException if {@link #isImmutable() immutable}.
   */
  CodeType add(String name);

  @Override
  CodeNestedTypes copy(CodeType newDeclaringType);

}
