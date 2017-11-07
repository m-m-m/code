/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.member;

import java.lang.reflect.AccessibleObject;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import net.sf.mmm.code.api.member.CodeMember;
import net.sf.mmm.code.api.modifier.CodeModifiers;
import net.sf.mmm.code.base.element.BaseElementWithModifiers;
import net.sf.mmm.code.base.type.BaseType;

/**
 * Base implementation of {@link CodeMember}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BaseMember extends BaseElementWithModifiers implements CodeMember {

  private String name;

  /**
   * The constructor.
   *
   * @param modifiers the {@link #getModifiers() modifiers}.
   * @param name the {@link #getName() name}.
   */
  public BaseMember(CodeModifiers modifiers, String name) {

    super(modifiers);
    this.name = name;
    verifyName(name, getNamePattern());
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseMember} to copy.
   */
  public BaseMember(BaseMember template) {

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
    ((BaseMembers) getParent()).rename(this, this.name, name, renamer);
  }

  private void doSetName(String newName) {

    this.name = newName;
  }

  @Override
  public abstract BaseMembers<?> getParent();

  @Override
  public BaseType getDeclaringType() {

    return getParent().getParent();
  }

  @Override
  public abstract AccessibleObject getReflectiveObject();

  @Override
  public abstract BaseMember getSourceCodeObject();

  @Override
  public abstract BaseMember copy();

}
