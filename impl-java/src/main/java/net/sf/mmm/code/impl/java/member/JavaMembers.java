/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.member;

import java.io.IOException;

import net.sf.mmm.code.api.member.CodeMember;
import net.sf.mmm.code.api.member.CodeMembers;
import net.sf.mmm.code.impl.java.item.JavaItemContainerWithInheritance;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link CodeMembers} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <M> type of the contained {@link JavaMember}s.
 * @since 1.0.0
 */
public abstract class JavaMembers<M extends CodeMember> extends JavaItemContainerWithInheritance<M> implements CodeMembers<M> {

  /**
   * The constructor.
   *
   * @param declaringType the {@link #getDeclaringType()}.
   */
  public JavaMembers(JavaType declaringType) {

    super(declaringType);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaMembers} to copy.
   */
  public JavaMembers(JavaMembers<M> template) {

    super(template);
  }

  @Override
  protected void doWrite(Appendable sink, String defaultIndent, String currentIndent) throws IOException {

    for (M declaredMember : getDeclared()) {
      writeNewline(sink);
      declaredMember.write(sink, defaultIndent, currentIndent);
    }
  }
}
