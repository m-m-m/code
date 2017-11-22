/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.merge;

import net.sf.mmm.code.api.annotation.CodeAnnotation;
import net.sf.mmm.code.api.annotation.CodeAnnotations;
import net.sf.mmm.code.api.element.CodeElement;

/**
 * Implementation of {@link CodeMergeStrategyDecider} that will always decide to
 * {@link CodeMergeStrategy#isMerge() merge} but {@link CodeMergeStrategy#MERGE_OVERRIDE_BODY overrides} the
 * body if a {@link javax.annotation.Generated} annotation is present and otherwise
 * {@link CodeMergeStrategy#MERGE_KEEP_BODY keeps} the original body.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class CodeMergeStrategyDeciderDefault implements CodeMergeStrategyDecider {

  /** The singleton instance of this class. */
  public static final CodeMergeStrategyDeciderDefault INSTANCE = new CodeMergeStrategyDeciderDefault();

  /**
   * The constructor.
   */
  private CodeMergeStrategyDeciderDefault() {

    super();
  }

  @Override
  public CodeMergeStrategy decide(CodeAdvancedMergeableItem<?> original, CodeAdvancedMergeableItem<?> other, CodeMergeStrategy parentStrategy) {

    if (original instanceof CodeElement) {
      CodeAnnotations annotations = ((CodeElement) original).getAnnotations();
      for (CodeAnnotation annotation : annotations.getDeclared()) {
        String simpleName = annotation.getType().getSimpleName();
        if (simpleName.equals("Generated")) {
          return CodeMergeStrategy.MERGE_OVERRIDE_BODY;
        }
      }
    }
    return CodeMergeStrategy.MERGE_KEEP_BODY;
  }

}
