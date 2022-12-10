/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api.merge;

/**
 * Call-back Interface to
 * {@link #decide(CodeAdvancedMergeableItem, CodeAdvancedMergeableItem, CodeMergeStrategy) decide} which merge
 * strategy to apply for a
 * {@link CodeAdvancedMergeableItem#merge(CodeAdvancedMergeableItem, CodeMergeStrategyDecider) merge}. Will be
 * called recursively during the merge except for {@link io.github.mmm.code.api.node.CodeNodeItemContainer}s.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
@FunctionalInterface
public interface CodeMergeStrategyDecider {

  /**
   * @param original the {@link CodeAdvancedMergeableItem} on which the
   *        {@link CodeAdvancedMergeableItem#merge(CodeAdvancedMergeableItem, CodeMergeStrategyDecider) merge}
   *        is invoked on.
   * @param other the {@link CodeAdvancedMergeableItem} given as argument to
   *        {@link CodeAdvancedMergeableItem#merge(CodeAdvancedMergeableItem, CodeMergeStrategyDecider)
   *        merge}.
   * @param parentStrategy the {@link CodeMergeStrategy} that has been
   *        {@link #decide(CodeAdvancedMergeableItem, CodeAdvancedMergeableItem, CodeMergeStrategy) decided}
   *        for the {@link io.github.mmm.code.api.node.CodeNodeItem#getParent() parent} in case of a recursive
   *        merge invocation. Will typically be ignored but might also be returned in specific cases.
   * @return the {@link CodeMergeStrategy} to apply for the
   *         {@link CodeAdvancedMergeableItem#merge(CodeAdvancedMergeableItem, CodeMergeStrategyDecider)
   *         merge}. Shall never be {@code null}.
   */
  CodeMergeStrategy decide(CodeAdvancedMergeableItem<?> original, CodeAdvancedMergeableItem<?> other, CodeMergeStrategy parentStrategy);

}
