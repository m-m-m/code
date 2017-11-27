/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.type;

import java.io.IOException;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.function.Consumer;

import net.sf.mmm.code.api.copy.CodeCopyMapper;
import net.sf.mmm.code.api.copy.CodeCopyMapperNone;
import net.sf.mmm.code.api.element.CodeElementWithTypeVariables;
import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.api.member.CodeOperation;
import net.sf.mmm.code.api.merge.CodeMergeStrategy;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.api.type.CodeTypeVariable;
import net.sf.mmm.code.api.type.CodeTypeVariables;
import net.sf.mmm.code.base.member.BaseOperation;

/**
 * Base implementation of {@link CodeTypeVariables}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseTypeVariables extends BaseGenericTypeParameters<CodeTypeVariable> implements CodeTypeVariables {

  /** The empty and {@link #isImmutable() immutable} instance of {@link BaseTypeVariables}. */
  public static final BaseTypeVariables EMPTY = new BaseTypeVariables();

  private BaseOperation declaringOperation;

  private BaseType declaringType;

  private CodeTypeVariables sourceCodeObject;

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
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseTypeVariables(BaseTypeVariables template, BaseType declaringType, CodeCopyMapper mapper) {

    super(template, mapper);
    this.declaringType = declaringType;
    this.declaringOperation = null;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseTypeVariables} to copy.
   * @param declaringOperation the {@link #getDeclaringOperation() declaring operation}.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseTypeVariables(BaseTypeVariables template, BaseOperation declaringOperation, CodeCopyMapper mapper) {

    super(template, mapper);
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
  public CodeElementWithTypeVariables getParent() {

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
  public CodeTypeVariables getSourceCodeObject() {

    if ((this.sourceCodeObject == null) && !isInitialized()) {
      if (this.declaringOperation != null) {
        CodeOperation sourceOperation = this.declaringOperation.getSourceCodeObject();
        if (sourceOperation != null) {
          this.sourceCodeObject = sourceOperation.getTypeParameters();
        }
      } else if (this.declaringType != null) {
        CodeType sourceType = this.declaringType.getSourceCodeObject();
        if (sourceType != null) {
          this.sourceCodeObject = sourceType.getTypeParameters();
        }
      }
    }
    return this.sourceCodeObject;
  }

  @Override
  protected String getKey(CodeTypeVariable item) {

    return item.getName();
  }

  @Override
  public BaseTypeVariable add(String name) {

    BaseTypeVariable variable = new BaseTypeVariable(this, name, null);
    add(variable);
    return variable;
  }

  @Override
  public CodeTypeVariable getDeclared(String name) {

    return get(name, false, true);
  }

  @Override
  public CodeTypeVariable get(String name, boolean includeDeclaringTypes) {

    return get(name, includeDeclaringTypes, true);
  }

  CodeTypeVariable get(String name, boolean includeDeclaringTypes, boolean init) {

    initialize(init);
    CodeTypeVariable typeVariable = getByName(name);
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
  protected void rename(CodeTypeVariable child, String oldName, String newName, Consumer<String> renamer) {

    super.rename(child, oldName, newName, renamer);
  }

  @Override
  public CodeTypeVariables merge(CodeTypeVariables o, CodeMergeStrategy strategy) {

    if (strategy == CodeMergeStrategy.KEEP) {
      return this;
    }
    BaseTypeVariables other = (BaseTypeVariables) o;
    List<? extends CodeTypeVariable> otherTypeVariables = other.getDeclared();
    if (strategy == CodeMergeStrategy.OVERRIDE) {
      clear();
      for (CodeTypeVariable otherTypeVariable : otherTypeVariables) {
        add(otherTypeVariable.copy(this));
      }
    } else {
      List<? extends CodeTypeVariable> myTypeVariables = getDeclared();
      int i = 0;
      int len = myTypeVariables.size();
      assert (len == otherTypeVariables.size());
      for (CodeTypeVariable otherTypeVariable : otherTypeVariables) {
        CodeTypeVariable myTypeVariable = null;
        if (i < len) {
          myTypeVariable = myTypeVariables.get(i++); // merging via index as by name could cause errors
        }
        if (myTypeVariable == null) {
          add(otherTypeVariable.copy(this));
        } else {
          // TODO myTypeVariable.doMerge(otherTypeVariable, strategy);
        }
      }
    }
    return this;
  }

  @Override
  public BaseTypeVariables copy() {

    return copy(getParent());
  }

  @Override
  public BaseTypeVariables copy(CodeElementWithTypeVariables newParent) {

    return copy(newParent, CodeCopyMapperNone.INSTANCE);
  }

  @Override
  public BaseTypeVariables copy(CodeElementWithTypeVariables newParent, CodeCopyMapper mapper) {

    if (newParent instanceof BaseType) {
      return new BaseTypeVariables(this, (BaseType) newParent, mapper);
    } else if (newParent instanceof BaseOperation) {
      return new BaseTypeVariables(this, (BaseOperation) newParent, mapper);
    } else {
      throw new IllegalArgumentException("" + newParent);
    }
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    writeReference(sink, true);
  }

  void writeReference(Appendable sink, boolean declaration) throws IOException {

    List<CodeTypeVariable> typeVariables = getList();
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
