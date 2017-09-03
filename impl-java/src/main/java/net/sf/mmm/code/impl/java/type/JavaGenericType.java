/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.type;

import java.io.IOException;

import net.sf.mmm.code.api.CodeGenericType;
import net.sf.mmm.code.impl.java.JavaContext;
import net.sf.mmm.code.impl.java.element.JavaElement;
import net.sf.mmm.code.impl.java.element.JavaElementWithQualifiedName;

/**
 * Implementation of {@link CodeGenericType} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class JavaGenericType extends JavaElementWithQualifiedName implements CodeGenericType {

  /**
   * The constructor.
   *
   * @param context the {@link #getContext() context}.
   */
  public JavaGenericType(JavaContext context) {

    super(context, null, null);
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
  public abstract JavaType asType();

  @Override
  public abstract JavaTypeVariable asTypeVariable();

  @Override
  public void writeReference(Appendable sink, boolean declaration) throws IOException {

    if (isQualified()) {
      sink.append(getQualifiedName());
    } else {
      sink.append(getSimpleName());
    }
    getTypeVariables().write(sink, "", "");
  }

}
