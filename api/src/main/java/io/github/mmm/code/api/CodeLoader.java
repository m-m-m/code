/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.api;

import io.github.mmm.code.api.type.CodeGenericType;
import io.github.mmm.code.api.type.CodeType;

/**
 * Abstract interface to get (or load) {@link #getType(String) types}.
 *
 * @author hohwille
 * @since 1.0.0
 */
public abstract interface CodeLoader extends CodeWithContext {

  /**
   * @param qualifiedName the {@link CodeType#getQualifiedName() qualified name} of the requested {@link CodeType}.
   * @return the requested {@link CodeType} or {@code null} if not found.
   */
  CodeType getType(String qualifiedName);

  /**
   * @param qualifiedName the {@link CodeName} of the requested {@link CodeType}.
   * @return the requested {@link CodeType} or {@code null} if not found.
   */
  CodeType getType(CodeName qualifiedName);

  /**
   * This method makes only sense for Java. For other {@link io.github.mmm.code.api.language.CodeLanguage}s please ignore
   * this method.
   *
   * @param clazz the {@link Class} to get as {@link CodeGenericType}.
   * @return the existing or otherwise newly created {@link CodeGenericType}. Typically a {@link CodeType} but may also
   *         be a {@link CodeType#createArray() array type} in case an {@link Class#isArray() array} was given.
   */
  CodeGenericType getType(Class<?> clazz);
}
