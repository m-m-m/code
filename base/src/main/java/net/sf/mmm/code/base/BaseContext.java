/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base;

import java.lang.reflect.Type;

import net.sf.mmm.code.api.CodeContext;
import net.sf.mmm.code.api.element.CodeElementWithDeclaringType;
import net.sf.mmm.code.api.type.CodeGenericType;
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
  BaseContext getParent();

  /**
   * @return the root {@link BaseContext context} responsible for the fundamental code (from the SDK like JDK for Java).
   */
  default BaseContext getRootContext() {

    return getParent().getRootContext();
  }

  /**
   * @param id the {@link BaseSource#getId() ID} of the requested source.
   * @return the existing {@link BaseSource} for the given {@link BaseSource#getId() ID} or {@code null} if not found.
   */
  BaseSource getSource(String id);

  @Override
  BaseType getRootType();

  @Override
  BaseType getRootEnumerationType();

  @Override
  BaseType getVoidType();

  @Override
  BaseType getRootExceptionType();

  @Override
  BaseType getBooleanType(boolean primitive);

  @Override
  BaseTypeWildcard getUnboundedWildcard();

  /**
   * @param type the {@link Type} to get as {@link CodeGenericType}.
   * @param declaringElement the owning {@link CodeElementWithDeclaringType} declaring the {@link Type}.
   * @return the existing or otherwise newly created {@link CodeGenericType}.
   */
  BaseGenericType getType(Type type, CodeElementWithDeclaringType declaringElement);

  /**
   * @param javaType the {@link BaseType} that might be {@link BaseType#isPrimitive() primitive}.
   * @return the corresponding {@link BaseType#getNonPrimitiveType() non-primitive type}.
   */
  BaseType getNonPrimitiveType(BaseType javaType);

  @Override
  BaseFactory getFactory();

  @Override
  default BaseType getOrCreateType(String qualifiedName, boolean add) {

    return (BaseType) CodeContext.super.getOrCreateType(qualifiedName, add);
  }

  @Override
  BaseContext createChildContext();

}
