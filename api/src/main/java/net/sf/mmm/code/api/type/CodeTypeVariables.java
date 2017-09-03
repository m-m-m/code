/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.type;

import java.util.List;

import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.item.CodeItemContainer;
import net.sf.mmm.code.api.item.CodeItemWithDeclaringType;
import net.sf.mmm.code.api.member.CodeConstructor;
import net.sf.mmm.code.api.member.CodeMethod;
import net.sf.mmm.code.api.member.CodeOperation;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * {@link CodeItem} Abstract interface for an invokable operation such as a {@link CodeMethod} or
 * {@link CodeConstructor}.
 *
 * @see Class#getTypeParameters()
 * @see java.lang.reflect.Executable#getTypeParameters()
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeTypeVariables extends CodeItemContainer<CodeTypeVariable>, CodeItemWithDeclaringType {

  /**
   * @return the {@link CodeOperation} declaring this type variables or {@code null} if
   *         {@link #getDeclaringType() declared by a type}.
   */
  CodeOperation getDeclaringOperation();

  @Override
  List<? extends CodeTypeVariable> getAll();

  /**
   * @param name the {@link CodeTypeVariable#getName() name} of the requested {@link CodeTypeVariable}.
   * @return the requested {@link CodeTypeVariable} or {@code null} if not found.
   */
  CodeTypeVariable get(String name);

  /**
   * @param name the {@link CodeTypeVariable#getName() name} of the {@link CodeTypeVariable} to create.
   * @return a new {@link CodeTypeVariable} that has been added.
   * @throws ReadOnlyException if {@link #isImmutable() immutable}.
   */
  CodeTypeVariable add(String name);

}
