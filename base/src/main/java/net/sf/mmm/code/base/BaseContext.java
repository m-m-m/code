/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base;

import java.lang.reflect.Type;

import net.sf.mmm.code.api.CodeContext;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.base.element.BaseElementWithDeclaringType;
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
  BaseContext getParent();

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
   * @param type the {@link Type} to get as {@link CodeGenericType}.
   * @param declaringElement the owning {@link BaseElementWithDeclaringType} declaring the {@link Type}.
   * @return the existing or otherwise newly created {@link CodeGenericType}.
   */
  BaseGenericType getType(Type type, BaseElementWithDeclaringType declaringElement);

  /**
   * @param javaType the {@link BaseType} that might be {@link BaseType#isPrimitive() primitive}.
   * @return the corresponding {@link BaseType#getNonPrimitiveType() non-primitive type}.
   */
  BaseType getNonPrimitiveType(BaseType javaType);

}
