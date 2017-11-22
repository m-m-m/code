/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.type;

import net.sf.mmm.code.api.element.CodeElementWithTypeVariables;
import net.sf.mmm.code.api.member.CodeOperation;
import net.sf.mmm.code.api.merge.CodeSimpleMergeableItem;
import net.sf.mmm.code.api.node.CodeNodeItemContainerFlatWithName;

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
 * @param <V> the type of the contained {@link CodeTypeVariable}s.
 * @since 1.0.0
 */
public interface CodeTypeVariables<V extends CodeTypeVariable>
    extends CodeGenericTypeParameters<V>, CodeNodeItemContainerFlatWithName<V>, CodeSimpleMergeableItem<CodeTypeVariables<?>> {

  @Override
  CodeElementWithTypeVariables getParent();

  /**
   * @param name the {@link CodeTypeVariable#getName() name} of the requested {@link CodeTypeVariable}.
   * @param includeParents - {@code true} if the search should recurse into {@link #getParent() parents}
   *        ({@link CodeOperation#getDeclaringType() declaring type} of {@link CodeOperation} or
   *        {@link CodeType#getDeclaringType() declaring types} of {@link CodeType#isNested() nested}
   *        {@link CodeType}s) if non-{@link net.sf.mmm.code.api.modifier.CodeModifiers#isStatic() static},
   *        {@code false} otherwise
   * @return the requested {@link CodeTypeVariable} or {@code null} if not found.
   * @see #get(String)
   */
  V get(String name, boolean includeParents);

  @Override
  CodeTypeVariables<V> copy();

}
