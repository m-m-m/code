/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.element;

import java.util.Objects;

import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.element.CodeElementWithModifiers;
import io.github.mmm.code.api.modifier.CodeModifiers;

/**
 * Implementation of {@link CodeElementWithModifiers} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BaseElementWithModifiers extends BaseElementWithDeclaringType
    implements CodeElementWithModifiers {

  private CodeModifiers modifiers;

  /**
   * The constructor.
   *
   * @param modifiers the {@link #getModifiers() modifiers}.
   */
  public BaseElementWithModifiers(CodeModifiers modifiers) {

    super();
    this.modifiers = modifiers;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseElementWithModifiers} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseElementWithModifiers(BaseElementWithModifiers template, CodeCopyMapper mapper) {

    super(template, mapper);
    this.modifiers = template.modifiers;
  }

  @Override
  public CodeModifiers getModifiers() {

    return this.modifiers;
  }

  @Override
  public void setModifiers(CodeModifiers modifiers) {

    Objects.requireNonNull(modifiers, "modifiers");
    verifyMutalbe();
    this.modifiers = modifiers;
  }

  @Override
  public abstract BaseElementWithModifiers copy();

}
