/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.copy.CodeCopyType;
import io.github.mmm.code.api.type.CodeGenericType;
import io.github.mmm.code.api.type.CodeTypeParameters;
import io.github.mmm.code.base.BaseContext;
import io.github.mmm.code.base.member.BaseOperation;

/**
 * Base implementation of {@link CodeTypeParameters}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseTypeParameters extends BaseGenericTypeParameters<CodeGenericType> implements CodeTypeParameters {

  /** The empty and {@link #isImmutable() immutable} instance of {@link BaseTypeParameters}. */
  public static final BaseTypeParameters EMPTY = new BaseTypeParameters();

  private final BaseParameterizedType parent;

  private BaseTypeParameters sourceCodeObject;

  /**
   * The constructor for #EMPTY
   */
  private BaseTypeParameters() {

    super();
    this.parent = null;
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public BaseTypeParameters(BaseParameterizedType parent) {

    super();
    this.parent = parent;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseTypeParameters} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseTypeParameters(BaseTypeParameters template, CodeCopyMapper mapper) {

    super(template, mapper);
    this.parent = mapper.map(template.parent, CodeCopyType.PARENT);
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
    ParameterizedType reflectiveObject = this.parent.getReflectiveObject();
    if (reflectiveObject != null) {
      BaseContext context = getContext();
      for (Type type : reflectiveObject.getActualTypeArguments()) {
        BaseGenericType genericType = context.getType(type, this.parent);
        addInternal(genericType);
      }
    }
  }

  @Override
  public BaseParameterizedType getParent() {

    return this.parent;
  }

  @Override
  public BaseType getDeclaringType() {

    return this.parent.getDeclaringType();
  }

  @Override
  public BaseOperation getDeclaringOperation() {

    return this.parent.getDeclaringOperation();
  }

  @Override
  public BaseTypeParameters getSourceCodeObject() {

    if ((this.sourceCodeObject == null) && !isInitialized()) {
      BaseParameterizedType sourceParameterizedType = this.parent.getSourceCodeObject();
      if (sourceParameterizedType != null) {
        this.sourceCodeObject = sourceParameterizedType.getTypeParameters();
      }
    }
    return this.sourceCodeObject;
  }

  @Override
  protected CodeGenericType ensureParent(CodeGenericType item) {

    if (item.getParent() != this) {
      return doCopyNodeUnsafe(item, this);
    }
    return item;
  }

  @Override
  public BaseTypeParameters copy() {

    return copy(getDefaultCopyMapper());
  }

  @Override
  public BaseTypeParameters copy(CodeCopyMapper mapper) {

    return new BaseTypeParameters(this, mapper);
  }

}
