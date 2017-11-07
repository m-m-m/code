/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base;

import org.assertj.core.api.Assertions;

/**
 * Abstract super-class for {@link BaseContextImpl}.
 */
public abstract class BaseContextTest extends Assertions {

  /**
   * @return the {@link BaseContext} to test.
   */
  protected BaseContext createContext() {

    return new TestContext();
  }

}
