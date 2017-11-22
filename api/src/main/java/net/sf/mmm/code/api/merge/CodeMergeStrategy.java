/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.merge;

/**
 * Enum with the available strategies for a
 * {@link CodeAdvancedMergeableItem#merge(CodeAdvancedMergeableItem, CodeMergeStrategyDecider) merge}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public enum CodeMergeStrategy {

  /**
   * Overrides the original {@link CodeMergeableItem item} (on which
   * {@link CodeAdvancedMergeableItem#merge(CodeAdvancedMergeableItem, CodeMergeStrategyDecider) merge} was
   * invoked) with the one given as merge argument.
   */
  OVERRIDE,

  /**
   * Keeps the original {@link CodeMergeableItem item} (on which
   * {@link CodeAdvancedMergeableItem#merge(CodeAdvancedMergeableItem, CodeMergeStrategyDecider) merge} was
   * invoked) untouched. In other words the merge does nothing.
   */
  KEEP,

  /**
   * {@link #isMerge() Merges} the signature(s) of both elements {@link CodeMergeableItem item}s (joins
   * {@link net.sf.mmm.code.api.element.CodeElement#getAnnotations() annotations},
   * {@link net.sf.mmm.code.api.element.CodeElement#getDoc() docs}, and
   * {@link net.sf.mmm.code.api.element.CodeElement#getComment() comments}) but {@link #OVERRIDE overrides}
   * the original {@link net.sf.mmm.code.api.block.CodeBlockBody body} or initializer
   * ({@link net.sf.mmm.code.api.member.CodeField#getInitializer()} or
   * {@link net.sf.mmm.code.api.block.CodeBlockInitializer}).
   */
  MERGE_OVERRIDE_BODY,

  /**
   * {@link #isMerge() Merges} the signature(s) of both elements {@link CodeMergeableItem item}s (joins
   * {@link net.sf.mmm.code.api.element.CodeElement#getAnnotations() annotations},
   * {@link net.sf.mmm.code.api.element.CodeElement#getDoc() docs}, and
   * {@link net.sf.mmm.code.api.element.CodeElement#getComment() comments}) but {@link #KEEP keeps} the
   * original {@link net.sf.mmm.code.api.block.CodeBlockBody body} or initializer
   * ({@link net.sf.mmm.code.api.member.CodeField#getInitializer()} or
   * {@link net.sf.mmm.code.api.block.CodeBlockInitializer}).
   */
  MERGE_KEEP_BODY;

  /**
   * @return {@code true} if {@link #MERGE_OVERRIDE_BODY} or {@link #MERGE_KEEP_BODY}, {@code false}
   *         otherwise. Please note that a
   *         {@link CodeAdvancedMergeableItem#merge(CodeAdvancedMergeableItem, CodeMergeStrategyDecider)
   *         merge} will only behave different between {@link #MERGE_OVERRIDE_BODY} and
   *         {@link #MERGE_KEEP_BODY} for {@link CodeMergeableItem items} that do have a
   *         {@link net.sf.mmm.code.api.block.CodeBlockBody body} (in other words
   *         {@link net.sf.mmm.code.api.node.CodeFunction}s) or initializer (in other words
   *         ({@link net.sf.mmm.code.api.member.CodeField}s or ({@link net.sf.mmm.code.api.type.CodeType}s).
   */
  public boolean isMerge() {

    return (this == MERGE_OVERRIDE_BODY) || (this == MERGE_KEEP_BODY);
  }

}
