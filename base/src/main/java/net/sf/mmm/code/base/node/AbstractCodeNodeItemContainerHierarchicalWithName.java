/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.node;

import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.node.CodeNodeItemContainerHierarchicalWithName;

/**
 * Abstract implementation of {@link CodeNodeItemContainerHierarchicalWithName}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <I> the type of the contained {@link CodeItem}.
 * @since 1.0.0
 */
public abstract class AbstractCodeNodeItemContainerHierarchicalWithName<I extends CodeItem> extends AbstractCodeNodeItemContainerWithName<I>
    implements CodeNodeItemContainerHierarchicalWithName<I> {

  /**
   * The constructor.
   */
  protected AbstractCodeNodeItemContainerHierarchicalWithName() {

    super();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link AbstractCodeNodeItemContainerHierarchicalWithName} to copy.
   */
  public AbstractCodeNodeItemContainerHierarchicalWithName(AbstractCodeNodeItemContainerHierarchicalWithName<I> template) {

    super(template);
  }

  @Override
  protected abstract I get(String name, boolean init);

}
