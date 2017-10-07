/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.member;

import java.lang.reflect.AccessibleObject;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import net.sf.mmm.code.api.member.CodeMember;
import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.impl.java.element.JavaElementWithModifiers;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link CodeMember} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class JavaMember extends JavaElementWithModifiers implements CodeMember {

  private String name;

  /**
   * The constructor.
   *
   * @param modifiers the {@link #getModifiers() modifiers}.
   * @param name the {@link #getName() name}.
   */
  public JavaMember(CodeModifiers modifiers, String name) {

    super(modifiers);
    this.name = name;
    verifyName(name, getNamePattern());
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaMember} to copy.
   */
  public JavaMember(JavaMember template) {

    super(template);
    this.name = template.name;
  }

  /**
   * @return the regex {@link Pattern} the {@link #getName() name} has to {@link String#matches(String) match}
   *         or {@code null} to accept any name.
   * @see #verifyName(String, Pattern)
   */
  protected Pattern getNamePattern() {

    return NAME_PATTERN;
  }

  @Override
  public String getName() {

    return this.name;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public void setName(String name) {

    verifyMutalbe();
    if (this.name.equals(name)) {
      return;
    }
    Consumer<String> renamer = this::doSetName;
    ((JavaMembers) getParent()).rename(this, this.name, name, renamer);
  }

  private void doSetName(String newName) {

    this.name = newName;
  }

  @Override
  public abstract JavaMembers<?> getParent();

  @Override
  public JavaType getDeclaringType() {

    return getParent().getParent();
  }

  @Override
  public abstract AccessibleObject getReflectiveObject();

  @Override
  public abstract JavaMember copy();

}
