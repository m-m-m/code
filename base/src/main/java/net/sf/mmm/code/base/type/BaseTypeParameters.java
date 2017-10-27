/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.type.CodeTypeParameters;
import net.sf.mmm.code.base.BaseContext;
import net.sf.mmm.code.base.member.BaseOperation;

/**
 * Base implementation of {@link CodeTypeParameters}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseTypeParameters extends BaseGenericTypeParameters<BaseGenericType>
    implements CodeTypeParameters<BaseGenericType>, CodeNodeItemWithGenericParent<BaseParameterizedType, BaseTypeParameters> {

  /** The empty and {@link #isImmutable() immutable} instance of {@link BaseTypeParameters}. */
  public static final BaseTypeParameters EMPTY = new BaseTypeParameters();

  private final BaseParameterizedType parent;

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
   * @param parent the {@link #getParent() parent}.
   */
  public BaseTypeParameters(BaseTypeParameters template, BaseParameterizedType parent) {

    super(template);
    this.parent = parent;
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
  public BaseTypeParameters copy() {

    return copy(this.parent);
  }

  @Override
  public BaseTypeParameters copy(BaseParameterizedType newParent) {

    return new BaseTypeParameters(this, newParent);
  }

}
