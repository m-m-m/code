/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.node;

import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.item.CodeItem;
import io.github.mmm.code.api.node.CodeNodeItemContainerHierarchical;

/**
 * Base implementation of {@link CodeNodeItemContainerHierarchical}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <I> the type of the contained {@link CodeItem}.
 * @since 1.0.0
 */
public abstract class BaseNodeItemContainerHierarchical<I extends CodeItem> extends BaseNodeItemContainer<I> implements CodeNodeItemContainerHierarchical<I> {

  /**
   * The constructor.
   */
  protected BaseNodeItemContainerHierarchical() {

    super();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseNodeItemContainerHierarchical} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseNodeItemContainerHierarchical(BaseNodeItemContainerHierarchical<I> template, CodeCopyMapper mapper) {

    super(template, mapper);
  }

}
