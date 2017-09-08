/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.type;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.type.CodeComposedType;
import net.sf.mmm.code.api.type.CodeGenericType;

/**
 * Implementation of {@link JavaGenericType} for a {@link java.lang.reflect.ParameterizedType}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaComposedType extends JavaGenericType implements CodeComposedType, CodeNodeItemWithGenericParent<JavaType, JavaComposedType> {

  private final JavaType parent;

  private List<JavaType> types;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public JavaComposedType(JavaType parent) {

    super();
    this.parent = parent;
    this.types = new ArrayList<>();
    this.types.add(parent);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaComposedType} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaComposedType(JavaComposedType template, JavaType parent) {

    super(template);
    this.parent = parent;
    this.types = new ArrayList<>(template.types);
    if (this.types.isEmpty()) {
      this.types.add(parent);
    } else {
      this.types.set(0, parent);
    }
  }

  @Override
  public JavaType getParent() {

    return this.parent;
  }

  @Override
  public JavaType getDeclaringType() {

    return null;
  }

  @Override
  public List<? extends JavaType> getTypes() {

    return this.types;
  }

  @Override
  public JavaType asType() {

    if (this.types.isEmpty()) {
      return getContext().getRootType();
    }
    return this.types.get(0).asType();
  }

  @Override
  public JavaGenericType resolve(CodeGenericType context) {

    // can not resolve any further...
    return this;
  }

  @Override
  public JavaComposedType copy() {

    return copy(this.parent);
  }

  @Override
  public JavaComposedType copy(JavaType newParent) {

    return new JavaComposedType(this, newParent);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent) throws IOException {

    super.doWrite(sink, newline, null, null);
    writeReference(sink, true);
  }

  @Override
  public void writeReference(Appendable sink, boolean declaration) throws IOException {

    String prefix = "";
    for (JavaGenericType type : this.types) {
      sink.append(prefix);
      type.writeReference(sink, declaration);
      prefix = " & ";
    }
  }

}
