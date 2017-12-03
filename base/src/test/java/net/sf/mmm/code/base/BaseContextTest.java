/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base;

import org.assertj.core.api.Assertions;

import net.sf.mmm.code.api.annotation.CodeAnnotation;
import net.sf.mmm.code.api.annotation.CodeAnnotations;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.base.comment.BaseSingleLineComment;

/**
 * Abstract super-class for {@link BaseContextImpl}.
 */
@SuppressWarnings("javadoc")
public abstract class BaseContextTest extends Assertions {

  protected static final String TEST_TEXT1 = "This is a test";

  protected static final String TEST_TEXT2 = "API-Doc comment.";

  protected static final BaseSingleLineComment TEST_COMMENT = new BaseSingleLineComment(TEST_TEXT1);

  private CodeType overrideType;

  /**
   * @return the {@link BaseContext} to test.
   */
  protected BaseContext createContext() {

    return new TestContext();
  }

  protected CodeAnnotation getTestAnnotation(CodeAnnotations annotations) {

    return annotations.add(getTestAnnotationType(annotations));
  }

  protected CodeType getTestAnnotationType(CodeAnnotations annotations) {

    if (this.overrideType == null) {
      this.overrideType = annotations.getContext().getType(Override.class).asType();
    }
    return this.overrideType;
  }

}
