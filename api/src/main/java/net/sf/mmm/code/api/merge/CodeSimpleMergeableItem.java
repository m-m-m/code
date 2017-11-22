/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.merge;

/**
 * {@link CodeMergeableItem} with
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 * @param <S> type of the {@link CodeSimpleMergeableItem item} that can be merged with this item. Will
 *        typically be this type itself.
 */
public interface CodeSimpleMergeableItem<S extends CodeSimpleMergeableItem<S>> extends CodeMergeableItem<S> {

  @Override
  default S merge(S other) {

    return merge(other, CodeMergeStrategy.MERGE_KEEP_BODY);
  }

  /**
   * @param other the other item to merge.
   * @param strategy {@code true} to override, {@code false} otherwise (regular merge, default).
   * @return the result of the merge. May be this item where the merge was invoked on, or the given
   *         {@code other} item.
   */
  S merge(S other, CodeMergeStrategy strategy);

}
