/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.node;

import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.node.CodeNodeItemContainerFlatWithName;

/**
 * Abstract implementation of {@link CodeNodeItemContainerFlatWithName}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <I> the type of the contained {@link CodeItem}.
 * @since 1.0.0
 */
public abstract class AbstractCodeNodeItemContainerFlatWithName<I extends CodeItem> extends AbstractCodeNodeItemContainerWithName<I>
    implements CodeNodeItemContainerFlatWithName<I> {

  /**
   * The constructor.
   */
  protected AbstractCodeNodeItemContainerFlatWithName() {

    super();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link AbstractCodeNodeItemContainerFlatWithName} to copy.
   */
  public AbstractCodeNodeItemContainerFlatWithName(AbstractCodeNodeItemContainerFlatWithName<I> template) {

    super(template);
  }

}
