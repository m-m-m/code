/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.type;

import java.io.IOException;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.function.Consumer;

import net.sf.mmm.code.api.member.CodeOperation;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.type.CodeTypeVariables;
import net.sf.mmm.code.impl.java.element.JavaElementWithTypeVariables;
import net.sf.mmm.code.impl.java.member.JavaOperation;

/**
 * Implementation of {@link CodeTypeVariables} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaTypeVariables extends JavaGenericTypeParameters<JavaTypeVariable>
    implements CodeTypeVariables<JavaTypeVariable>, CodeNodeItemWithGenericParent<JavaElementWithTypeVariables, JavaTypeVariables> {

  /** The empty and {@link #isImmutable() immutable} instance of {@link JavaTypeVariables}. */
  public static final JavaTypeVariables EMPTY = new JavaTypeVariables();

  private final JavaType declaringType;

  private final JavaOperation declaringOperation;

  /**
   * The constructor for {@link #EMPTY}.
   */
  private JavaTypeVariables() {

    super();
    this.declaringType = null;
    this.declaringOperation = null;
    setImmutable();
  }

  /**
   * The constructor.
   *
   * @param declaringType the {@link #getDeclaringType() declaring type}.
   */
  public JavaTypeVariables(JavaType declaringType) {

    super();
    this.declaringType = declaringType;
    this.declaringOperation = null;
  }

  /**
   * The constructor.
   *
   * @param declaringOperation the {@link #getDeclaringOperation() declaring operation}.
   */
  public JavaTypeVariables(JavaOperation declaringOperation) {

    super();
    this.declaringType = declaringOperation.getDeclaringType();
    this.declaringOperation = declaringOperation;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaTypeVariables} to copy.
   * @param declaringType the {@link #getDeclaringType() declaring type}.
   */
  public JavaTypeVariables(JavaTypeVariables template, JavaType declaringType) {

    super(template);
    this.declaringType = declaringType;
    this.declaringOperation = null;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaTypeVariables} to copy.
   * @param declaringOperation the {@link #getDeclaringOperation() declaring operation}.
   */
  public JavaTypeVariables(JavaTypeVariables template, JavaOperation declaringOperation) {

    super(template);
    this.declaringType = declaringOperation.getDeclaringType();
    this.declaringOperation = declaringOperation;
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
    JavaElementWithTypeVariables parent = getParent();
    if (parent == null) {
      return; // should only happen for EMPTY instance.
    }
    GenericDeclaration reflectiveObject = parent.getReflectiveObject();
    if (reflectiveObject != null) {
      TypeVariable<?>[] typesParams = reflectiveObject.getTypeParameters();
      for (TypeVariable<?> typeVar : typesParams) {
        JavaTypeVariable typeVariable = new JavaTypeVariable(this, typeVar);
        addInternal(typeVariable);
      }
    }
  }

  @Override
  public JavaElementWithTypeVariables getParent() {

    if (this.declaringOperation != null) {
      return this.declaringOperation;
    }
    return getDeclaringType();
  }

  @Override
  public JavaType getDeclaringType() {

    return this.declaringType;
  }

  @Override
  public CodeOperation getDeclaringOperation() {

    return this.declaringOperation;
  }

  @Override
  protected String getKey(JavaTypeVariable item) {

    return item.getName();
  }

  @Override
  public JavaTypeVariable add(String name) {

    JavaTypeVariable variable = new JavaTypeVariable(this, name, null);
    add(variable);
    return variable;
  }

  @Override
  public JavaTypeVariable get(String name) {

    return get(name, false, true);
  }

  @Override
  public JavaTypeVariable get(String name, boolean includeDeclaringTypes) {

    return get(name, includeDeclaringTypes, true);
  }

  JavaTypeVariable get(String name, boolean includeDeclaringTypes, boolean init) {

    initialize(init);
    JavaTypeVariable typeVariable = getByName(name);
    if (typeVariable != null) {
      return typeVariable;
    }
    JavaType parent = null;
    if ((this.declaringOperation != null) && !this.declaringOperation.getModifiers().isStatic()) {
      parent = this.declaringType;
    } else if (!this.declaringType.getModifiers().isStatic() && this.declaringType.isNested()) {
      parent = this.declaringType.getDeclaringType();
    }
    if (parent != null) {
      return parent.getTypeParameters().get(name, includeDeclaringTypes, init);
    }
    return null;
  }

  @Override
  public JavaTypeVariable getRequired(String name) {

    return super.getRequired(name);
  }

  @Override
  protected void rename(JavaTypeVariable child, String oldName, String newName, Consumer<String> renamer) {

    super.rename(child, oldName, newName, renamer);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent) throws IOException {

    writeReference(sink, true);
  }

  @Override
  public JavaTypeVariables copy() {

    return copy(getParent());
  }

  @Override
  public JavaTypeVariables copy(JavaElementWithTypeVariables newParent) {

    if (newParent instanceof JavaType) {
      return new JavaTypeVariables(this, (JavaType) newParent);
    } else if (newParent instanceof JavaOperation) {
      return new JavaTypeVariables(this, (JavaOperation) newParent);
    } else {
      throw new IllegalArgumentException("" + newParent);
    }
  }

  void writeReference(Appendable sink, boolean declaration) throws IOException {

    List<JavaTypeVariable> typeVariables = getList();
    if (!typeVariables.isEmpty()) {
      String prefix = "<";
      for (JavaTypeVariable variable : typeVariables) {
        sink.append(prefix);
        variable.write(sink, "", "");
        prefix = ", ";
      }
      sink.append('>');
    }
  }

}
