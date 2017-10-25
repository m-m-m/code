/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.member;

import java.io.IOException;
import java.util.function.Consumer;

import net.sf.mmm.code.api.member.CodeMembers;
import net.sf.mmm.code.api.syntax.CodeSyntax;
import net.sf.mmm.code.base.node.AbstractCodeNodeItemContainer;
import net.sf.mmm.code.impl.java.node.JavaNodeItem;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link CodeMembers} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <M> type of the contained {@link JavaMember}s.
 * @since 1.0.0
 */
public abstract class JavaMembers<M extends JavaMember> extends AbstractCodeNodeItemContainer<M> implements CodeMembers<M>, JavaNodeItem {

  private final JavaType parent;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public JavaMembers(JavaType parent) {

    super();
    this.parent = parent;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaMembers} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaMembers(JavaMembers<M> template, JavaType parent) {

    super(template);
    this.parent = parent;
  }

  @Override
  public JavaType getParent() {

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
  public JavaType getDeclaringType() {

    return getParent().getDeclaringType();
  }

  @Override
  public abstract JavaMembers<M> copy();

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeSyntax syntax) throws IOException {

    for (M declaredMember : getList()) {
      sink.append(newline);
      sink.append(currentIndent);
      declaredMember.write(sink, newline, defaultIndent, currentIndent, syntax);
    }
  }
}
