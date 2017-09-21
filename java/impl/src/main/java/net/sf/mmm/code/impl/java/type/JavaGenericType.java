/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.type;

import java.lang.reflect.Type;

import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.impl.java.element.JavaElement;

/**
 * Implementation of {@link CodeGenericType} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class JavaGenericType extends JavaElement implements CodeGenericType {

  private JavaArrayType arrayType;

  /**
   * The constructor.
   */
  public JavaGenericType() {

    super();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaGenericType} to copy.
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
  public JavaComposedType asComposedType() {

    return null;
  }

  @Override
  public JavaTypeVariable asTypeVariable() {

    return null;
  }

  @Override
  public JavaTypeWildcard asTypeWildcard() {

    return null;
  }

  @Override
  public JavaTypePlaceholder asTypePlaceholder() {

    return null;
  }

  @Override
  public JavaGenericType asUnqualifiedType() {

    return null;
  }

  @Override
  public JavaArrayType createArray() {

    if (this.arrayType == null) {
      this.arrayType = new JavaArrayType(this, this);
    }
    return this.arrayType;
  }

  @Override
  public JavaGenericTypeParameters<?> getTypeParameters() {

    return JavaTypeParameters.EMPTY;
  }

  @Override
  public JavaGenericType getComponentType() {

    return null;
  }

  @Override
  public boolean isArray() {

    return false;
  }

  @Override
  public boolean isQualified() {

    return false;
  }

  @Override
  public abstract JavaGenericType resolve(CodeGenericType context);

  @Override
  public abstract Type getReflectiveObject();

  @Override
  public abstract JavaGenericType copy();

}
