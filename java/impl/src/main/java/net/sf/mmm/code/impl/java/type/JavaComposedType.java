/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.type;

import java.io.IOException;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.syntax.CodeSyntax;
import net.sf.mmm.code.api.type.CodeComposedType;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.impl.java.JavaContext;
import net.sf.mmm.code.impl.java.item.JavaReflectiveObject;

/**
 * Implementation of {@link JavaGenericType} for a {@link java.lang.reflect.ParameterizedType}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaComposedType extends JavaGenericType
    implements CodeComposedType, CodeNodeItemWithGenericParent<JavaTypePlaceholder, JavaComposedType>, JavaReflectiveObject<Type> {

  private final JavaTypePlaceholder parent;

  private Type[] bounds;

  private List<JavaGenericType> types;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public JavaComposedType(JavaTypePlaceholder parent) {

    this(parent, null);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param bounds the optional bounds if created from reflective object, otherwise {@code null}.
   */
  public JavaComposedType(JavaTypePlaceholder parent, Type[] bounds) {

    super();
    this.parent = parent;
    this.bounds = bounds;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaComposedType} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaComposedType(JavaComposedType template, JavaTypePlaceholder parent) {

    super(template);
    this.parent = parent;
    this.bounds = null;
    template.getTypes();
    this.types = new ArrayList<>(template.types);
  }

  @Override
  protected void doSetImmutable() {

    super.doSetImmutable();
    this.types = Collections.unmodifiableList(this.types);
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
    getTypes();
  }

  @Override
  public JavaTypePlaceholder getParent() {

    return this.parent;
  }

  /**
   * @return the {@link TypeVariable} or {@link WildcardType} declaring this composed type via its bounds.
   */
  @Override
  public Type getReflectiveObject() {

    return this.parent.getReflectiveObject();
  }

  @Override
  public JavaType getDeclaringType() {

    return null;
  }

  @Override
  public List<? extends JavaGenericType> getTypes() {

    if (this.types == null) {
      if (this.bounds == null) {
        this.types = new ArrayList<>();
      } else {
        assert (this.bounds.length > 1);
        this.types = new ArrayList<>(this.bounds.length);
        JavaContext context = getContext();
        for (Type bound : this.bounds) {
          JavaType type = (JavaType) context.getType((Class<?>) bound);
          this.types.add(type);
        }
        this.bounds = null;
      }
    }
    return this.types;
  }

  @Override
  public void add(CodeGenericType type) {

    verifyMutalbe();
    getTypes();
    this.types.add((JavaType) type);
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
  public String getSimpleName() {

    StringBuilder sb = new StringBuilder();
    String prefix = "";
    for (JavaGenericType type : getTypes()) {
      sb.append(prefix);
      sb.append(type.getSimpleName());
      prefix = " & ";
    }
    return sb.toString();
  }

  @Override
  public String getQualifiedName() {

    StringBuilder sb = new StringBuilder();
    String prefix = "";
    for (JavaGenericType type : getTypes()) {
      sb.append(prefix);
      sb.append(type.getQualifiedName());
      prefix = " & ";
    }
    return sb.toString();
  }

  @Override
  public JavaComposedType copy() {

    return copy(this.parent);
  }

  @Override
  public JavaComposedType copy(JavaTypePlaceholder newParent) {

    return new JavaComposedType(this, newParent);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeSyntax syntax) throws IOException {

    super.doWrite(sink, newline, null, "", syntax);
    writeReference(sink, true);
  }

  @Override
  public void writeReference(Appendable sink, boolean declaration, Boolean qualified) throws IOException {

    String prefix = "";
    for (JavaGenericType type : this.types) {
      sink.append(prefix);
      type.writeReference(sink, declaration, qualified);
      prefix = " & ";
    }
  }

}
