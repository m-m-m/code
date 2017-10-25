/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.node;

import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.node.CodeNodeItemContainerFlat;

/**
 * Abstract implementation of {@link CodeNodeItemContainerFlat}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <I> the type of the contained {@link CodeItem}.
 * @since 1.0.0
 */
public abstract class AbstractCodeNodeItemContainerFlat<I extends CodeItem> extends AbstractCodeNodeItemContainer<I> implements CodeNodeItemContainerFlat<I> {

  /**
   * The constructor.
   */
  protected AbstractCodeNodeItemContainerFlat() {

    super();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link AbstractCodeNodeItemContainerFlat} to copy.
   */
  public AbstractCodeNodeItemContainerFlat(AbstractCodeNodeItemContainerFlat<I> template) {

    super(template);
  }

}
