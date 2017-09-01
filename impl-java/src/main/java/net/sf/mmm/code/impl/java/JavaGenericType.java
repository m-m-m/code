/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import net.sf.mmm.code.api.CodeGenericType;

/**
 * Implementation of {@link CodeGenericType} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class JavaGenericType extends JavaElement implements CodeGenericType {

  /**
   * The constructor.
   *
   * @param context the {@link #getContext() context}.
   */
  public JavaGenericType(JavaContext context) {

    super(context);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaElement} to copy.
   */
  public JavaGenericType(JavaGenericType template) {

    super(template);
  }

  @Override
  public boolean isAssignableFrom(CodeGenericType type) {

    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public abstract JavaType getRawType();

}
