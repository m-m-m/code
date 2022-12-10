/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.member;

import io.github.mmm.base.exception.ReadOnlyException;
import io.github.mmm.code.api.copy.CodeNodeItemCopyable;
import io.github.mmm.code.api.node.CodeNodeItemContainerHierarchicalWithName;
import io.github.mmm.code.api.type.CodeType;

/**
 * {@link CodeMembers} as a container for the {@link CodeProperty}s. <br>
 * <b>Attention:</b><br>
 * The {@link CodeProperty properties} are calculated on the fly and the operations may be expensive. Avoid subsequent
 * calls to the same method if possible.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeProperties extends CodeMembers<CodeProperty>,
    CodeNodeItemContainerHierarchicalWithName<CodeProperty>, CodeNodeItemCopyable<CodeType, CodeProperties> {

  /**
   * @deprecated instances of {@link CodeProperty} are dynamically created on the fly.
   */
  @Deprecated
  @Override
  default CodeProperty add(String name) {

    throw new ReadOnlyException(getDeclaringType().getSimpleName(), "properties");
  }

  @Override
  CodeProperties copy();
}
