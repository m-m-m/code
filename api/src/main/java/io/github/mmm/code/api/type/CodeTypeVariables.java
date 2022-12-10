/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.type;

import io.github.mmm.code.api.copy.CodeNodeItemCopyable;
import io.github.mmm.code.api.element.CodeElementWithTypeVariables;
import io.github.mmm.code.api.member.CodeOperation;
import io.github.mmm.code.api.merge.CodeSimpleMergeableItem;
import io.github.mmm.code.api.node.CodeNodeItemContainerFlatWithName;

/**
 * {@link CodeGenericTypeParameters} containing the {@link CodeTypeVariable}s of a {@link CodeType} or
 * {@link CodeOperation}.
 *
 * @see Class#getTypeParameters()
 * @see java.lang.reflect.Executable#getTypeParameters()
 * @see CodeGenericType#getTypeParameters()
 * @see CodeOperation#getTypeParameters()
 * @see CodeTypeParameters
 * @see CodeTypeVariable
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeTypeVariables extends CodeGenericTypeParameters<CodeTypeVariable>, CodeNodeItemContainerFlatWithName<CodeTypeVariable>,
    CodeSimpleMergeableItem<CodeTypeVariables>, CodeNodeItemCopyable<CodeElementWithTypeVariables, CodeTypeVariables> {

  @Override
  CodeElementWithTypeVariables getParent();

  /**
   * @param name the {@link CodeTypeVariable#getName() name} of the requested {@link CodeTypeVariable}.
   * @param includeParents - {@code true} if the search should recurse into {@link #getParent() parents}
   *        ({@link CodeOperation#getDeclaringType() declaring type} of {@link CodeOperation} or
   *        {@link CodeType#getDeclaringType() declaring types} of {@link CodeType#isNested() nested} {@link CodeType}s)
   *        if non-{@link io.github.mmm.code.api.modifier.CodeModifiers#isStatic() static}, {@code false} otherwise
   * @return the requested {@link CodeTypeVariable} or {@code null} if not found.
   * @see #get(String)
   */
  CodeTypeVariable get(String name, boolean includeParents);

  @Override
  CodeTypeVariables copy();

}
