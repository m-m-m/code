/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.lang.reflect.Type;

import net.sf.mmm.code.impl.java.element.JavaElementNode;
import net.sf.mmm.code.impl.java.source.JavaSource;
import net.sf.mmm.code.impl.java.type.JavaArrayType;
import net.sf.mmm.code.impl.java.type.JavaGenericType;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * {@link JavaLoader} that encapsulates the physical loading of code.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface JavaCodeLoader extends JavaLoader {

  /**
   * @return {@code true} if loading of byte-code is supported, {@code false} otherwise.
   * @see net.sf.mmm.code.api.source.CodeSource#getByteCodeLocation()
   */
  boolean isSupportByteCode();

  /**
   * @return {@code true} if loading from source-code is supported, {@code false} otherwise.
   * @see net.sf.mmm.code.api.source.CodeSource#getSourceCodeLocation()
   */
  boolean isSupportSourceCode();

  /**
   * @param source the declaring {@link JavaSource} supposed to be the owner of the requested package.
   * @param pkg the Java {@link Package}.
   * @return the existing or otherwise created {@link JavaPackage}.
   */
  JavaPackage getPackage(JavaSource source, Package pkg);

  /**
   * @param clazz the {@link Class} to get as {@link JavaType}.
   * @return the existing or otherwise newly created {@link JavaGenericType}. Typically a {@link JavaType} but
   *         may also be a {@link JavaArrayType} in case an {@link Class#isArray() array} was given.
   */
  JavaGenericType getType(Class<?> clazz);

  /**
   * @param type the {@link Type} to get as {@link JavaGenericType}.
   * @param declaringElement the owning {@link JavaElementNode} declaring the {@link Type}.
   * @return the existing or otherwise newly created {@link JavaGenericType}.
   */
  JavaGenericType getType(Type type, JavaElementNode declaringElement);

  /**
   * @param pkg the {@link JavaPackage} to scan. Will load all {@link JavaPackage#getChildren() children} of
   *        the package.
   */
  void scan(JavaPackage pkg);

}
