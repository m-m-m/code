/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.member;

import net.sf.mmm.code.api.member.CodeConstructor;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * Implementation of {@link CodeConstructor} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaConstructor extends JavaOperation implements CodeConstructor, CodeNodeItemWithGenericParent<JavaConstructors, JavaConstructor> {

  private final JavaConstructors parent;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public JavaConstructor(JavaConstructors parent) {

    super("");
    this.parent = parent;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaConstructor} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaConstructor(JavaConstructor template, JavaConstructors parent) {

    super(template);
    this.parent = parent;
  }

  @Override
  public JavaConstructors getParent() {

    return this.parent;
  }

  @Override
  public String getName() {

    return getDeclaringType().getSimpleName();
  }

  @Override
  public void setName(String name) {

    throw new ReadOnlyException(getClass().getSimpleName(), "name");
  }

  @Override
  public JavaConstructor copy() {

    return copy(this.parent);
  }

  @Override
  public JavaConstructor copy(JavaConstructors newParent) {

    return new JavaConstructor(this, newParent);
  }

}
