/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.type;

import net.sf.mmm.code.api.element.CodeElementWithName;
import net.sf.mmm.code.api.node.CodeNodeItemWithDeclaringOperation;

/**
 * {@link CodeGenericType} representing a type variable. It is a variable as a placeholder for a
 * {@link CodeGenericType generic} {@link CodeType type}.
 *
 * @see java.lang.reflect.TypeVariable
 * @see CodeType#getTypeVariables()
 * @see CodeGenericType#asTypeVariable()
 * @see CodeTypeVariables
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeTypeVariable extends CodeGenericType, CodeElementWithName, CodeNodeItemWithDeclaringOperation {

  @Override
  CodeTypeVariables getParent();

  /**
   * @return {@code true} if this type variable extends a given type (e.g. "{@code T extends String}"),
   *         {@code false} otherwise.
   */
  boolean isExtends();

  /**
   * @return {@code true} if this type variable represents the given type or is a supertype of it (e.g.
   *         "{@code T super String}"), {@code false} otherwise.
   */
  boolean isSuper();

  /**
   * @return {@code true} if this is a wildcard variable (and the {@link #getName() name} is "{@code ?}"),
   *         {@code false} otherwise.
   */
  boolean isWildcard();

  /**
   * @return the {@link CodeGenericType} representing the {@link java.lang.reflect.TypeVariable#getBounds()
   *         bound(s)} of this type variable.
   */
  CodeGenericType getBound();

  @Override
  default boolean isQualified() {

    return false;
  }

  /**
   * @deprecated a {@link CodeTypeVariable} can not have {@link CodeTypeVariables}. The result will always be
   *             empty and {@link #isImmutable() immutable}.
   */
  @Deprecated
  @Override
  CodeTypeVariables getTypeVariables();

  @Override
  CodeTypeVariable copy();

}
