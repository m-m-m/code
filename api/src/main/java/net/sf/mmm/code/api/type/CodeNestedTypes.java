/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.type;

import net.sf.mmm.code.api.node.CodeNodeItemContainerHierarchical;
import net.sf.mmm.code.api.node.CodeNodeItemContainerHierarchicalWithName;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * {@link CodeNodeItemContainerHierarchical} containing the nested {@link CodeType}s.
 *
 * @see Class#getDeclaringClass()
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeNestedTypes extends CodeNodeItemContainerHierarchicalWithName<CodeType> {

  @Override
  CodeType getParent();

  /**
   * @param name the {@link CodeType#getSimpleName() simple name} of the requested {@link CodeType}.
   * @return the nested {@link CodeType} from {@link #getDeclared()} or {@code null} if not found.
   */
  @Override
  CodeType getDeclared(String name);

  /**
   * @param name the {@link CodeType#getSimpleName() simple name} of the requested {@link CodeType}.
   * @return the nested {@link CodeType} from {@link #getAll()} or {@code null} if not found.
   */
  @Override
  CodeType get(String name);

  /**
   * @param name the {@link CodeType#getSimpleName() simple name} of the {@link CodeType} to create.
   * @return a new {@link CodeType} that has been added.
   * @throws ReadOnlyException if {@link #isImmutable() immutable}.
   */
  @Override
  CodeType add(String name);

  @Override
  CodeNestedTypes copy();

}
