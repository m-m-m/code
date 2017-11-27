/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.type;

import java.util.List;

import net.sf.mmm.code.api.copy.CodeNodeItemCopyable;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * {@link CodeGenericType} {@link #getTypes() composing multiple other types (interfaces)} (e.g.
 * "{@code Comparable & Serializable}") to a virtual type that does not actually exist as raw
 * {@link net.sf.mmm.code.api.type.CodeType}.
 *
 * @see java.lang.reflect.TypeVariable
 * @see CodeType#getTypeParameters()
 * @see CodeGenericType#asTypeVariable()
 * @see CodeTypeVariables
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface CodeComposedType extends CodeGenericType, CodeNodeItemCopyable<CodeTypePlaceholder, CodeComposedType> {

  /**
   * @return the {@link List} of {@link CodeGenericType}s this virtual type is composed of. Should contain at least two
   *         elements in order to make any sense. For Java this can only be instances of {@link CodeType} or otherwise
   *         {@link CodeGenericType#isQualified() qualified} references.
   */
  List<? extends CodeGenericType> getTypes();

  /**
   * @param type the {@link CodeGenericType} to add.
   * @throws ReadOnlyException if {@link #isImmutable() immutable}.
   */
  void add(CodeGenericType type);

  /**
   * Will typically be a {@link CodeTypeWildcard} (at least for Java up to version 9).
   */
  @Override
  CodeTypePlaceholder getParent();

  @Override
  default CodeComposedType asComposedType() {

    return this;
  }

  @Override
  CodeComposedType copy();

}
