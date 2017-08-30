/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api;

/**
 * Abstract top-level interface for any item of code as defined by this API. It reflects code structure.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract interface CodeTypeVariable extends CodeElementWithName {

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

}
