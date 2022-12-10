/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.element;

import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.element.CodeElementWithDeclaringType;

/**
 * Base implementation of {@link CodeElementWithDeclaringType}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BaseElementWithDeclaringType extends BaseElement implements CodeElementWithDeclaringType {

  /**
   * The constructor.
   */
  public BaseElementWithDeclaringType() {

    super();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseElementWithDeclaringType} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseElementWithDeclaringType(BaseElementWithDeclaringType template, CodeCopyMapper mapper) {

    super(template, mapper);
  }

}
