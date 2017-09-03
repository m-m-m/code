/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.member;

import net.sf.mmm.code.api.member.CodeConstructor;
import net.sf.mmm.code.impl.java.type.JavaType;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * Implementation of {@link CodeConstructor} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaConstructor extends JavaOperation implements CodeConstructor {

  /**
   * The constructor.
   *
   * @param declaringType the {@link #getDeclaringType()}.
   */
  public JavaConstructor(JavaType declaringType) {

    super(declaringType);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaConstructor} to copy.
   */
  public JavaConstructor(JavaConstructor template) {

    super(template);
  }

  @Override
  public String getName() {

    return getDeclaringType().getSimpleName();
  }

  @Override
  public void setName(String name) {

    throw new ReadOnlyException(getClass().getSimpleName(), "name");
  }

}
