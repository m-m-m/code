/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.type;

import java.io.IOException;

import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.type.CodeGenericType;

/**
 * Implementation of {@link JavaGenericType} for {@link #isArray() array} in Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaArrayType extends JavaGenericType implements CodeNodeItemWithGenericParent<JavaGenericType, JavaArrayType> {

  private final JavaGenericType parent;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public JavaArrayType(JavaGenericType parent) {

    super();
    this.parent = parent;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaArrayType} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaArrayType(JavaArrayType template, JavaGenericType parent) {

    super(template);
    this.parent = parent;
  }

  @Override
  public JavaGenericType getParent() {

    return this.parent;
  }

  @Override
  public final boolean isArray() {

    return true;
  }

  @Override
  public JavaType asType() {

    return getContext().getRootType();
  }

  @Override
  public JavaGenericType resolve(CodeGenericType context) {

    JavaGenericType resolvedType = this.parent.resolve(context);
    if (resolvedType == this.parent) {
      return this;
    }
    return new JavaArrayType(resolvedType);
  }

  @Override
  public JavaType getDeclaringType() {

    return null;
  }

  @Override
  public void writeReference(Appendable sink, boolean declaration) throws IOException {

    this.parent.writeReference(sink, declaration);
    sink.append("[]");
  }

  @Override
  public JavaArrayType copy() {

    return copy(this.parent);
  }

  @Override
  public JavaArrayType copy(JavaGenericType newParent) {

    return new JavaArrayType(this, newParent);
  }

}
