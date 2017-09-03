/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.type;

import net.sf.mmm.code.api.CodeGenericType;
import net.sf.mmm.code.api.element.CodeElementWithName;
import net.sf.mmm.code.api.member.CodeOperation;

/**
 * Abstract top-level interface for any item of code as defined by this API. It reflects code structure.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeTypeVariable extends CodeGenericType, CodeElementWithName {

  /**
   * @return the {@link CodeOperation} declaring this type variable or {@code null} if
   *         {@link #getDeclaringType() declared by a type}.
   */
  CodeOperation getDeclaringOperation();

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
}
