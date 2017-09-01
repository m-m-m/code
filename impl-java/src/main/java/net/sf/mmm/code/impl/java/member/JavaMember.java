/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.member;

import net.sf.mmm.code.api.member.CodeMember;
import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.impl.java.JavaElementWithModifiers;
import net.sf.mmm.code.impl.java.JavaType;

/**
 * Implementation of {@link CodeMember}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class JavaMember extends JavaElementWithModifiers implements CodeMember {

  private final JavaType declaringType;

  private String name;

  /**
   * The constructor.
   *
   * @param declaringType the {@link #getDeclaringType()}.
   * @param modifiers the {@link #getModifiers() modifiers}.
   */
  public JavaMember(JavaType declaringType, CodeModifiers modifiers) {

    super(declaringType.getContext(), modifiers);
    this.declaringType = declaringType;
    this.name = "undefined";
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaMember} to copy.
   */
  public JavaMember(JavaMember template) {

    super(template);
    this.name = template.name;
    this.declaringType = template.declaringType;
  }

  @Override
  public String getName() {

    return this.name;
  }

  @Override
  public void setName(String name) {

    verifyMutalbe();
    this.name = name;
  }

  @Override
  public JavaType getDeclaringType() {

    return this.declaringType;
  }

}
