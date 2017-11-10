/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base;

import org.assertj.core.api.Assertions;

import net.sf.mmm.code.base.annoation.BaseAnnotation;
import net.sf.mmm.code.base.comment.BaseSingleLineComment;
import net.sf.mmm.code.base.source.BaseSource;
import net.sf.mmm.code.base.type.BaseType;

/**
 * Abstract super-class for {@link BaseContextImpl}.
 */
@SuppressWarnings("javadoc")
public abstract class BaseContextTest extends Assertions {

  protected static final String TEST_TEXT1 = "This is a test";

  protected static final String TEST_TEXT2 = "API-Doc comment.";

  protected static final BaseSingleLineComment TEST_COMMENT = new BaseSingleLineComment(TEST_TEXT1);

  private BaseAnnotation annotation;

  /**
   * @return the {@link BaseContext} to test.
   */
  protected BaseContext createContext() {

    return new TestContext();
  }

  protected BaseAnnotation getTestAnnotation(BaseSource source) {

    if (this.annotation == null) {
      BaseType type = (BaseType) source.getContext().getType(Override.class);
      this.annotation = new BaseAnnotation(source, type);
    }
    return this.annotation;
  }

}
