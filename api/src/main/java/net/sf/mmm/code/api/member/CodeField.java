/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.member;

import net.sf.mmm.code.api.CodeType;
import net.sf.mmm.code.api.expression.CodeExpression;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * {@link CodeProperty} for a field of a {@link CodeType}.
 *
 * @see java.lang.reflect.Field
 * @see CodeType#getFields(CodeMemberSelector)
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeField extends CodeProperty {

  @Override
  CodeField inherit(CodeType declaring);

  /**
   * @return the {@link CodeExpression} assigned to this field on initialization or {@code null} for none.
   */
  CodeExpression getInitializer();

  /**
   * @param initializer the new {@link #getInitializer() initializer}.
   * @throws ReadOnlyException if {@link #isImmutable() immutable}.
   */
  void setInitializer(CodeExpression initializer);

}