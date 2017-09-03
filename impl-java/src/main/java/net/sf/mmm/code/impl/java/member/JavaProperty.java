/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.member;

import java.io.IOException;

import net.sf.mmm.code.api.CodeGenericType;
import net.sf.mmm.code.api.member.CodeProperty;
import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link CodeProperty} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class JavaProperty extends JavaMember implements CodeProperty {

  private CodeGenericType type;

  /**
   * The constructor.
   *
   * @param declaringType the {@link #getDeclaringType()}.
   * @param modifiers the {@link #getModifiers() modifiers}.
   */
  public JavaProperty(JavaType declaringType, CodeModifiers modifiers) {

    super(declaringType, modifiers);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaProperty} to copy.
   */
  public JavaProperty(JavaProperty template) {

    super(template);
    this.type = template.type;
  }

  @Override
  public boolean isReadable() {

    return true;
  }

  @Override
  public boolean isWritable() {

    return !getModifiers().isFinal();
  }

  @Override
  public CodeGenericType getType() {

    return this.type;
  }

  @Override
  public void setType(CodeGenericType type) {

    verifyMutalbe();
    this.type = type;
  }

  @Override
  protected void doWrite(Appendable sink, String defaultIndent, String currentIndent) throws IOException {

  }

}
