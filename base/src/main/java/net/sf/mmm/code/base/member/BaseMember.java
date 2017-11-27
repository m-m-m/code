/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.member;

import java.lang.reflect.AccessibleObject;
import java.util.function.Consumer;

import net.sf.mmm.code.api.copy.CodeCopyMapper;
import net.sf.mmm.code.api.member.CodeMember;
import net.sf.mmm.code.api.member.CodeMembers;
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
   * @param parent the {@link #getParent() parent}.
   * @param modifiers the {@link #getModifiers() modifiers}.
   * @param name the {@link #getName() name}.
   */
  public BaseMember(CodeMembers<?> parent, CodeModifiers modifiers, String name) {

    super(modifiers);
    this.name = parent.getLanguage().verifyName(this, name);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseMember} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseMember(BaseMember template, CodeCopyMapper mapper) {

    super(template, mapper);
    this.name = template.name;
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

    this.name = getLanguage().verifyName(this, newName);
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
  public abstract CodeMember getSourceCodeObject();

  @Override
  public abstract BaseMember copy();

}
