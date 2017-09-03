/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.member;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.mmm.code.api.member.CodeConstructor;
import net.sf.mmm.code.api.member.CodeConstructors;
import net.sf.mmm.code.api.member.CodeMethods;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link CodeMethods} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaConstructors extends JavaMembers<CodeConstructor> implements CodeConstructors {

  private List<JavaConstructor> constructors;

  /**
   * The constructor.
   *
   * @param declaringType the {@link #getDeclaringType()}.
   */
  public JavaConstructors(JavaType declaringType) {

    super(declaringType);
    this.constructors = new ArrayList<>();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaConstructors} to copy.
   */
  public JavaConstructors(JavaConstructors template) {

    super(template);
  }

  @Override
  public List<? extends JavaConstructor> getDeclared() {

    return this.constructors;
  }

  @Override
  public Iterable<? extends JavaConstructor> getInherited() {

    return Collections.emptyList();
  }

  @Override
  public Iterable<? extends JavaConstructor> getAll() {

    return getDeclared();
  }

  @Override
  public JavaConstructor get(CodeType... parameterTypes) {

    for (JavaConstructor method : this.constructors) {
    }
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public JavaConstructor add() {

    JavaConstructor constructor = new JavaConstructor(getDeclaringType());
    this.constructors.add(constructor);
    return constructor;
  }

}
