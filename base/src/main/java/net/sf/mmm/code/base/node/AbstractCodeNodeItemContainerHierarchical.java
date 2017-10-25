/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.node;

import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.node.CodeNodeItemContainerHierarchical;

/**
 * Abstract implementation of {@link CodeNodeItemContainerHierarchical}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <I> the type of the contained {@link CodeItem}.
 * @since 1.0.0
 */
public abstract class AbstractCodeNodeItemContainerHierarchical<I extends CodeItem> extends AbstractCodeNodeItemContainer<I>
    implements CodeNodeItemContainerHierarchical<I> {

  /**
   * The constructor.
   */
  protected AbstractCodeNodeItemContainerHierarchical() {

    super();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link AbstractCodeNodeItemContainerHierarchical} to copy.
   */
  public AbstractCodeNodeItemContainerHierarchical(AbstractCodeNodeItemContainerHierarchical<I> template) {

    super(template);
  }

}
