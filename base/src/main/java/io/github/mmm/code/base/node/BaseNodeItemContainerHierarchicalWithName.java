/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.node;

import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.item.CodeItem;
import io.github.mmm.code.api.node.CodeNodeItemContainerHierarchicalWithName;

/**
 * Base implementation of {@link CodeNodeItemContainerHierarchicalWithName}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <I> the type of the contained {@link CodeItem}.
 * @since 1.0.0
 */
public abstract class BaseNodeItemContainerHierarchicalWithName<I extends CodeItem> extends BaseNodeItemContainerWithName<I>
    implements CodeNodeItemContainerHierarchicalWithName<I> {

  /**
   * The constructor.
   */
  protected BaseNodeItemContainerHierarchicalWithName() {

    super();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseNodeItemContainerHierarchicalWithName} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseNodeItemContainerHierarchicalWithName(BaseNodeItemContainerHierarchicalWithName<I> template, CodeCopyMapper mapper) {

    super(template, mapper);
  }

  @Override
  protected abstract I get(String name, boolean init);

}
