/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.type;

import java.io.IOException;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.function.Consumer;

import net.sf.mmm.code.api.element.CodeElementWithTypeVariables;
import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.api.member.CodeOperation;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.type.CodeTypeVariable;
import net.sf.mmm.code.api.type.CodeTypeVariables;
import net.sf.mmm.code.base.element.BaseElementWithTypeVariables;
import net.sf.mmm.code.base.member.BaseOperation;

/**
 * Base implementation of {@link CodeTypeVariables}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseTypeVariables extends BaseGenericTypeParameters<BaseTypeVariable>
    implements CodeTypeVariables<BaseTypeVariable>, CodeNodeItemWithGenericParent<BaseElementWithTypeVariables, BaseTypeVariables> {

  /** The empty and {@link #isImmutable() immutable} instance of {@link BaseTypeVariables}. */
  public static final BaseTypeVariables EMPTY = new BaseTypeVariables();

  private BaseOperation declaringOperation;

  private BaseType declaringType;

  private BaseTypeVariables sourceCodeObject;

  /**
   * The constructor for {@link #EMPTY}.
   */
  private BaseTypeVariables() {

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
  public BaseTypeVariables(BaseType declaringType) {

    super();
    this.declaringType = declaringType;
    this.declaringOperation = null;
  }

  /**
   * The constructor.
   *
   * @param declaringOperation the {@link #getDeclaringOperation() declaring operation}.
   */
  public BaseTypeVariables(BaseOperation declaringOperation) {

    super();
    this.declaringType = null;
    this.declaringOperation = declaringOperation;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseTypeVariables} to copy.
   * @param declaringType the {@link #getDeclaringType() declaring type}.
   */
  public BaseTypeVariables(BaseTypeVariables template, BaseType declaringType) {

    super(template);
    this.declaringType = declaringType;
    this.declaringOperation = null;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseTypeVariables} to copy.
   * @param declaringOperation the {@link #getDeclaringOperation() declaring operation}.
   */
  public BaseTypeVariables(BaseTypeVariables template, BaseOperation declaringOperation) {

    super(template);
    this.declaringType = declaringOperation.getDeclaringType();
    this.declaringOperation = declaringOperation;
  }

  /**
   * @param declaringType the new value of {@link #getParent()}.
   */
  public void setParent(BaseType declaringType) {

    verifyMutalbe();
    this.declaringType = declaringType;
    this.declaringOperation = null;
  }

  /**
   * @param declaringOperation the new value of {@link #getParent()}.
   */
  public void setParent(BaseOperation declaringOperation) {

    verifyMutalbe();
    this.declaringType = null;
    this.declaringOperation = declaringOperation;
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
    CodeElementWithTypeVariables parent = getParent();
    if (parent == null) {
      return; // should only happen for EMPTY instance.
    }
    GenericDeclaration reflectiveObject = parent.getReflectiveObject();
    if (reflectiveObject != null) {
      TypeVariable<?>[] typesParams = reflectiveObject.getTypeParameters();
      for (TypeVariable<?> typeVar : typesParams) {
        BaseTypeVariable typeVariable = new BaseTypeVariable(this, typeVar);
        addInternal(typeVariable);
      }
    }
  }

  @Override
  public BaseElementWithTypeVariables getParent() {

    if (this.declaringOperation != null) {
      return this.declaringOperation;
    }
    return getDeclaringType();
  }

  @Override
  public BaseType getDeclaringType() {

    if ((this.declaringType == null) && (this.declaringOperation != null)) {
      this.declaringType = this.declaringOperation.getDeclaringType();
    }
    return this.declaringType;
  }

  @Override
  public CodeOperation getDeclaringOperation() {

    return this.declaringOperation;
  }

  @Override
  public BaseTypeVariables getSourceCodeObject() {

    if ((this.sourceCodeObject == null) && !isInitialized()) {
      if (this.declaringOperation != null) {
        BaseOperation sourceOperation = this.declaringOperation.getSourceCodeObject();
        if (sourceOperation != null) {
          this.sourceCodeObject = sourceOperation.getTypeParameters();
        }
      } else if (this.declaringType != null) {
        BaseType sourceType = this.declaringType.getSourceCodeObject();
        if (sourceType != null) {
          this.sourceCodeObject = sourceType.getTypeParameters();
        }
      }
    }
    return this.sourceCodeObject;
  }

  @Override
  protected String getKey(BaseTypeVariable item) {

    return item.getName();
  }

  @Override
  public BaseTypeVariable add(String name) {

    BaseTypeVariable variable = new BaseTypeVariable(this, name, null);
    add(variable);
    return variable;
  }

  @Override
  public BaseTypeVariable getDeclared(String name) {

    return get(name, false, true);
  }

  @Override
  public BaseTypeVariable get(String name, boolean includeDeclaringTypes) {

    return get(name, includeDeclaringTypes, true);
  }

  BaseTypeVariable get(String name, boolean includeDeclaringTypes, boolean init) {

    initialize(init);
    BaseTypeVariable typeVariable = getByName(name);
    if (typeVariable != null) {
      return typeVariable;
    }
    BaseType parent = null;
    if ((this.declaringOperation != null) && !this.declaringOperation.getModifiers().isStatic()) {
      parent = getDeclaringType();
    } else if (!getDeclaringType().getModifiers().isStatic() && this.declaringType.isNested()) {
      parent = this.declaringType.getDeclaringType();
    }
    if (parent != null) {
      return parent.getTypeParameters().get(name, includeDeclaringTypes, init);
    }
    return null;
  }

  @Override
  protected void rename(BaseTypeVariable child, String oldName, String newName, Consumer<String> renamer) {

    super.rename(child, oldName, newName, renamer);
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    writeReference(sink, true);
  }

  @Override
  public BaseTypeVariables copy() {

    return copy(getParent());
  }

  @Override
  public BaseTypeVariables copy(BaseElementWithTypeVariables newParent) {

    if (newParent instanceof BaseType) {
      return new BaseTypeVariables(this, (BaseType) newParent);
    } else if (newParent instanceof BaseOperation) {
      return new BaseTypeVariables(this, (BaseOperation) newParent);
    } else {
      throw new IllegalArgumentException("" + newParent);
    }
  }

  void writeReference(Appendable sink, boolean declaration) throws IOException {

    List<BaseTypeVariable> typeVariables = getList();
    if (!typeVariables.isEmpty()) {
      String prefix = "<";
      for (CodeTypeVariable variable : typeVariables) {
        sink.append(prefix);
        variable.writeReference(sink, declaration);
        prefix = ", ";
      }
      sink.append('>');
    }
  }

}
