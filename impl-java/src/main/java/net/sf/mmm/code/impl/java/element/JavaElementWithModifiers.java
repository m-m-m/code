/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.element;

import java.io.IOException;
import java.util.Objects;

import net.sf.mmm.code.api.modifier.CodeElementWithModifiers;
import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.impl.java.JavaContext;

/**
 * Implementation of {@link CodeElementWithModifiers} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class JavaElementWithModifiers extends JavaElement implements CodeElementWithModifiers {

  private CodeModifiers modifiers;

  /**
   * The constructor.
   *
   * @param context the {@link #getContext() context}.
   * @param modifiers the {@link #getModifiers() modifiers}.
   */
  public JavaElementWithModifiers(JavaContext context, CodeModifiers modifiers) {

    super(context);
    this.modifiers = modifiers;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaElementWithModifiers} to copy.
   */
  public JavaElementWithModifiers(JavaElementWithModifiers template) {

    super(template);
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
  protected void doWrite(Appendable sink, String defaultIndent, String currentIndent) throws IOException {

    super.doWrite(sink, defaultIndent, currentIndent);
    sink.append(currentIndent);
    sink.append(this.modifiers.toString());
  }

}
