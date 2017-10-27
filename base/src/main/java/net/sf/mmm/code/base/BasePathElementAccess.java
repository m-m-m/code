/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base;

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

}
