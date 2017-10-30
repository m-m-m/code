/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.block;

import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * {@link CodeBlock} for initializer code of a {@link net.sf.mmm.code.api.type.CodeType}.
 *
 * @author hohwille
 * @since 1.0.0
 * @see net.sf.mmm.code.api.type.CodeType#getStaticInitializer()
 * @see net.sf.mmm.code.api.type.CodeType#getNonStaticInitializer()
 */
public interface CodeBlockInitializer extends CodeBlock {

  @Override
  CodeType getParent();

  /**
   * @return {@code true} if this {@link CodeBlockInitializer} is {@link CodeModifiers#isStatic() static},
   *         {@code false} otherwise.
   */
  boolean isStatic();

  /**
   * @param isStatic the new value of {@link #isStatic()}.
   * @throws ReadOnlyException if {@link #isImmutable() immutable}.
   */
  void setStatic(boolean isStatic);
}
