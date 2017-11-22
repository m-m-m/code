/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.merge;

/**
 * {@link CodeMergeableItem} with
 * {@link #merge(CodeAdvancedMergeableItem, CodeMergeStrategyDecider, CodeMergeStrategy) advanced merge}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 * @param <S> type of the {@link CodeAdvancedMergeableItem item} that can be merged with this item. Will
 *        typically be this type itself.
 */
public abstract interface CodeAdvancedMergeableItem<S extends CodeAdvancedMergeableItem<S>> extends CodeMergeableItem<S> {

  @Override
  default S merge(S other) {

    return merge(other, CodeMergeStrategyDeciderDefault.INSTANCE);
  }

  /**
   * @param other the other item to merge.
   * @param decider the {@link CodeMergeStrategyDecider} that can choose the {@link CodeMergeStrategy}
   *        dynamically.
   * @return the result of the merge. May be this item where the merge was invoked on, or the given
   *         {@code other} item.
   */
  default S merge(S other, CodeMergeStrategyDecider decider) {

    return merge(other, decider, null);
  }

  /**
   * @param other the other item to merge.
   * @param decider the {@link CodeMergeStrategyDecider} that can choose the {@link CodeMergeStrategy}
   *        dynamically.
   * @param parentStrategy the {@link CodeMergeStrategy} that has been
   *        {@link CodeMergeStrategyDecider#decide(CodeAdvancedMergeableItem, CodeAdvancedMergeableItem, CodeMergeStrategy)
   *        decided} for the {@link net.sf.mmm.code.api.node.CodeNodeItem#getParent() parent} in case of a
   *        recursive merge invocation.
   * @return the result of the merge. May be this item where the merge was invoked on, or the given
   *         {@code other} item.
   */
  S merge(S other, CodeMergeStrategyDecider decider, CodeMergeStrategy parentStrategy);

}
