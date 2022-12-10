/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base;

import java.util.function.BiFunction;

import io.github.mmm.code.api.CodeName;

/**
 * Class to extend by internal implementation classes to get internal access to {@link BasePathElements}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BasePathElementAccess {

  /**
   * @param pathElements the {@link BasePathElements} where to add to.
   * @param item the {@link BasePathElement} to add.
   */
  protected static void addPathElementInternal(BasePathElements pathElements, BasePathElement item) {

    pathElements.addInternal(item);
  }

  /**
   * <b>Attention:</b> This is an internal API that should not be used from outside.
   *
   * @param start the {@link BasePathElement} where to start.
   * @param path the {@link CodeName} to traverse.
   * @param init - {@code true} to initialize, {@code false} otherwise.
   * @param factory the {@link BiFunction} used as factory to create missing packages.
   * @param add - {@code true} to add new packages created by the given {@code factory}, {@code false}
   *        otherwise.
   * @param forceAdd - {@code true} to force adding (if {@code add} is {@code true} but is immutable),
   *        {@code false} otherwise.
   * @return the traversed {@link BasePackage}. Has been created if it did not already exist and was produced
   *         by the given {@code factory}.
   * @see BasePathElements#getPackage(CodeName, boolean, BiFunction, boolean, boolean)
   */
  protected static BasePackage getPackage(BasePathElements start, CodeName path, boolean init, BiFunction<BasePackage, String, BasePackage> factory,
      boolean add, boolean forceAdd) {

    return start.getPackage(path, init, factory, add, forceAdd);
  }

}
