/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.member;

import java.io.IOException;
import java.util.function.Consumer;

import net.sf.mmm.code.api.member.CodeMember;
import net.sf.mmm.code.api.member.CodeMembers;
import net.sf.mmm.code.api.syntax.CodeSyntax;
import net.sf.mmm.code.base.node.BaseNodeItemContainer;
import net.sf.mmm.code.base.type.BaseType;

/**
 * Base implementation of {@link CodeMembers}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <M> type of the contained {@link BaseMember}s.
 * @since 1.0.0
 */
public abstract class BaseMembers<M extends CodeMember> extends BaseNodeItemContainer<M> implements CodeMembers<M> {

  private final BaseType parent;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public BaseMembers(BaseType parent) {

    super();
    this.parent = parent;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseMembers} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public BaseMembers(BaseMembers<M> template, BaseType parent) {

    super(template);
    this.parent = parent;
  }

  @Override
  public BaseType getParent() {

    return this.parent;
  }

  @Override
  protected void rename(M member, String oldName, String newName, Consumer<String> renamer) {

    super.rename(member, oldName, newName, renamer);
    this.parent.getProperties().renameMember(member, oldName, newName);
  }

  @Override
  public void add(M item) {

    if (item.getParent() != this) {
      throw new IllegalStateException();
    }
    super.add(item);
  }

  @Override
  public BaseType getDeclaringType() {

    return getParent().getDeclaringType();
  }

  @Override
  public abstract BaseMembers<M> getSourceCodeObject();

  @Override
  public abstract BaseMembers<M> copy();

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeSyntax syntax) throws IOException {

    for (M declaredMember : getList()) {
      sink.append(newline);
      sink.append(currentIndent);
      declaredMember.write(sink, newline, defaultIndent, currentIndent, syntax);
    }
  }
}
