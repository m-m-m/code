/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.block;

import io.github.mmm.code.api.copy.CodeNodeItemCopyable;
import io.github.mmm.code.api.modifier.CodeModifiers;
import io.github.mmm.code.api.type.CodeType;

/**
 * {@link CodeBlock} for initializer code of a {@link io.github.mmm.code.api.type.CodeType}.
 *
 * @author hohwille
 * @since 1.0.0
 * @see io.github.mmm.code.api.type.CodeType#getStaticInitializer()
 * @see io.github.mmm.code.api.type.CodeType#getNonStaticInitializer()
 */
public interface CodeBlockInitializer extends CodeBlock, CodeNodeItemCopyable<CodeType, CodeBlockInitializer> {

  @Override
  CodeType getParent();

  /**
   * @return {@code true} if this {@link CodeBlockInitializer} is {@link CodeModifiers#isStatic() static}, {@code false}
   *         otherwise.
   */
  boolean isStatic();

  /**
   * @param isStatic the new value of {@link #isStatic()}.
   * @throws io.github.mmm.base.exception.ReadOnlyException if {@link #isImmutable() immutable}.
   */
  void setStatic(boolean isStatic);

  @Override
  CodeBlockInitializer copy();
}
