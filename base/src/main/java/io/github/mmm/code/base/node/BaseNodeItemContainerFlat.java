/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.node;

import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.item.CodeItem;
import io.github.mmm.code.api.node.CodeNodeItemContainerFlat;

/**
 * Base implementation of {@link CodeNodeItemContainerFlat}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <I> the type of the contained {@link CodeItem}.
 * @since 1.0.0
 */
public abstract class BaseNodeItemContainerFlat<I extends CodeItem> extends BaseNodeItemContainer<I> implements CodeNodeItemContainerFlat<I> {

  /**
   * The constructor.
   */
  protected BaseNodeItemContainerFlat() {

    super();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseNodeItemContainerFlat} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseNodeItemContainerFlat(BaseNodeItemContainerFlat<I> template, CodeCopyMapper mapper) {

    super(template, mapper);
  }

}
