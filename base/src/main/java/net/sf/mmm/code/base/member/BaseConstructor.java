/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.member;

import java.lang.reflect.Constructor;
import java.util.regex.Pattern;

import net.sf.mmm.code.api.member.CodeConstructor;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.base.type.BaseTypeVariables;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * Base implementation of {@link CodeConstructor}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseConstructor extends BaseOperation implements CodeConstructor, CodeNodeItemWithGenericParent<BaseConstructors, BaseConstructor> {

  private final BaseConstructors parent;

  private final Constructor<?> reflectiveObject;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public BaseConstructor(BaseConstructors parent) {

    this(parent, null);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}. May be {@code null}.
   */
  public BaseConstructor(BaseConstructors parent, Constructor<?> reflectiveObject) {

    super("");
    this.parent = parent;
    this.reflectiveObject = reflectiveObject;
  }

  /**
   * The constructor.
   *
   * @param typeVariables the {@link #getTypeParameters() type variables}.
   * @param parent the {@link #getParent() parent}.
   */
  public BaseConstructor(BaseTypeVariables typeVariables, BaseConstructors parent) {

    super("", typeVariables);
    this.parent = parent;
    this.reflectiveObject = null;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseConstructor} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public BaseConstructor(BaseConstructor template, BaseConstructors parent) {

    super(template);
    this.parent = parent;
    this.reflectiveObject = null;
  }

  @Override
  protected Pattern getNamePattern() {

    return null;
  }

  @Override
  public BaseConstructors getParent() {

    return this.parent;
  }

  @Override
  public Constructor<?> getReflectiveObject() {

    return this.reflectiveObject;
  }

  @Override
  public String getName() {

    return getDeclaringType().getSimpleName();
  }

  @Override
  public void setName(String name) {

    throw new ReadOnlyException(getClass().getSimpleName(), "name");
  }

  @Override
  public BaseConstructor copy() {

    return copy(this.parent);
  }

  @Override
  public BaseConstructor copy(BaseConstructors newParent) {

    return new BaseConstructor(this, newParent);
  }

}
