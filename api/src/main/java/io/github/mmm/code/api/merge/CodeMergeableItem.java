/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.merge;

import io.github.mmm.code.api.item.CodeItem;

/**
 * {@link CodeItem} with {@link #merge(CodeMergeableItem) merge} support.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 * @param <S> type of the {@link CodeMergeableItem item} that can be merged with this item. Will typically be
 *        this type itself.
 */
public abstract interface CodeMergeableItem<S extends CodeMergeableItem<S>> extends CodeItem {

  /**
   * @param other the other item to merge.
   * @return the result of the merge. May be this item where the merge was invoked on, or the given
   *         {@code other} item.
   */
  S merge(S other);

}
