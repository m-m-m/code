/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.loader;

import java.lang.reflect.Type;

import net.sf.mmm.code.base.BasePackage;
import net.sf.mmm.code.base.element.BaseElement;
import net.sf.mmm.code.base.type.BaseGenericType;
import net.sf.mmm.code.impl.java.source.JavaSource;

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
   * @return the existing or otherwise created {@link BasePackage}.
   */
  BasePackage getPackage(JavaSource source, Package pkg);

  /**
   * @param clazz the {@link Class} to get as {@link BaseGenericType}.
   * @return the existing or otherwise newly created {@link BaseGenericType}. Typically a
   *         {@link net.sf.mmm.code.base.type.BaseType} but may also be a
   *         {@link net.sf.mmm.code.base.type.BaseArrayType} in case an {@link Class#isArray() array} was
   *         given.
   */
  BaseGenericType getType(Class<?> clazz);

  /**
   * @param type the {@link Type} to get as {@link BaseGenericType}.
   * @param declaringElement the owning {@link BaseElement} declaring the {@link Type}.
   * @return the existing or otherwise newly created {@link BaseGenericType}.
   */
  BaseGenericType getType(Type type, BaseElement declaringElement);

  /**
   * @param pkg the {@link BasePackage} to scan. Will load all {@link BasePackage#getChildren() children} of
   *        the package.
   */
  void scan(BasePackage pkg);

}
