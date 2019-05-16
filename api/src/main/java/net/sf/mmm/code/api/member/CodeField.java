/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.member;

import java.lang.reflect.Field;

import net.sf.mmm.code.api.copy.CodeNodeItemCopyable;
import net.sf.mmm.code.api.expression.CodeExpression;
import net.sf.mmm.code.api.expression.CodeVariable;
import net.sf.mmm.code.api.item.CodeMutableItemWithType;
import net.sf.mmm.code.api.merge.CodeAdvancedMergeableItem;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * {@link CodeMember} for a field of a {@link net.sf.mmm.code.api.type.CodeType}.
 *
 * @see java.lang.reflect.Field
 * @see net.sf.mmm.code.api.type.CodeType#getFields()
 * @see CodeFields#getDeclared()
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeField extends CodeMember, CodeMutableItemWithType, CodeVariable,
    CodeAdvancedMergeableItem<CodeField>, CodeNodeItemCopyable<CodeFields, CodeField> {

  /**
   * @return the {@link CodeExpression} assigned to this field on initialization or {@code null} for none.
   */
  CodeExpression getInitializer();

  /**
   * @param initializer the new {@link #getInitializer() initializer}.
   * @throws ReadOnlyException if {@link #isImmutable() immutable}.
   */
  void setInitializer(CodeExpression initializer);

  @Override
  Field getReflectiveObject();

  /**
   * @return the {@link CodeMethod} that acts as getter to read this field or {@code null} if there is no such method.
   */
  CodeMethod getGetter();

  /**
   * @return the {@link CodeMethod} that acts as getter to read this field. If it does not yet exist, it will be
   *         created.
   */
  CodeMethod getOrCreateGetter();

  /**
   * @return the {@link CodeMethod} that acts as setter to write this field or {@code null} if there is no such method.
   */
  CodeMethod getSetter();

  /**
   * @return the {@link CodeMethod} that acts as setter to write this field. If it does not yet exist, it will be
   *         created.
   */
  CodeMethod getOrCreateSetter();

  @Override
  CodeField copy();
}
