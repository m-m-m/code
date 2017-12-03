/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.element;

import net.sf.mmm.code.api.copy.CodeCopyMapper;
import net.sf.mmm.code.api.element.CodeElementWithDeclaringType;

/**
 * Base implementation of {@link BaseElement}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BaseElementWithDeclaringTypeImpl extends BaseElementImpl implements CodeElementWithDeclaringType {

  /**
   * The constructor.
   */
  public BaseElementWithDeclaringTypeImpl() {

    super();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseElementWithDeclaringTypeImpl} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseElementWithDeclaringTypeImpl(BaseElementWithDeclaringTypeImpl template, CodeCopyMapper mapper) {

    super(template, mapper);
  }

}
