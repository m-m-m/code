/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base;

import java.lang.reflect.Type;

import net.sf.mmm.code.api.CodeContext;
import net.sf.mmm.code.api.element.CodeElement;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.base.element.BaseElement;
import net.sf.mmm.code.base.source.BaseSource;
import net.sf.mmm.code.base.type.BaseGenericType;
import net.sf.mmm.code.base.type.BaseType;
import net.sf.mmm.code.base.type.BaseTypeWildcard;

/**
 * Base interface for {@link CodeContext}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface BaseContext extends CodeContext, BaseProvider {

  @Override
  BaseType getRootType();

  @Override
  BaseType getRootEnumerationType();

  @Override
  BaseType getVoidType();

  @Override
  BaseType getRootExceptionType();

  @Override
  BaseTypeWildcard getUnboundedWildcard();

  /**
   * @param clazz the {@link Class} to get as {@link CodeGenericType}.
   * @return the existing or otherwise newly created {@link CodeGenericType}. Typically a {@link CodeType} but
   *         may also be a {@link CodeType#createArray() array type} in case an {@link Class#isArray() array}
   *         was given.
   */
  BaseGenericType getType(Class<?> clazz);

  /**
   * @param type the {@link Type} to get as {@link CodeGenericType}.
   * @param declaringElement the owning {@link CodeElement} declaring the {@link Type}.
   * @return the existing or otherwise newly created {@link CodeGenericType}.
   */
  BaseGenericType getType(Type type, BaseElement declaringElement);

  /**
   * @param source the declaring {@link BaseSource} supposed to be the owner of the requested package.
   * @param pkg the Java {@link Package}.
   * @return the existing or otherwise created {@link BasePackage}.
   */
  BasePackage getPackage(BaseSource source, Package pkg);

  /**
   * @param javaType the {@link BaseType} that might be {@link BaseType#isPrimitive() primitive}.
   * @return the corresponding {@link BaseType#getNonPrimitiveType() non-primitive type}.
   */
  BaseType getNonPrimitiveType(BaseType javaType);

  /**
   * @return the {@link BaseSource#getToplevelPackage() top-level package}.
   */
  BasePackage getToplevelPackage();

}
