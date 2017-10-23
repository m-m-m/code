/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.impl.java.AbstractJavaCodeLoader;
import net.sf.mmm.code.impl.java.JavaPackage;
import net.sf.mmm.code.impl.java.source.JavaSource;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link AbstractJavaCodeLoader} using {@link ClassLoader} and {@link #isSupportByteCode()
 * byte code} analysis via reflection.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaByteCodeLoader extends AbstractJavaCodeLoader {

  private static final Logger LOG = LoggerFactory.getLogger(JavaByteCodeLoader.class);

  private final ClassLoader classloader;

  /**
   * The constructor.
   */
  public JavaByteCodeLoader() {

    this(Thread.currentThread().getContextClassLoader());
  }

  /**
   * The constructor.
   *
   * @param classloader the underlying {@link ClassLoader} to adopt.
   */
  public JavaByteCodeLoader(ClassLoader classloader) {

    super();
    this.classloader = classloader;
  }

  @Override
  public boolean isSupportByteCode() {

    return true;
  }

  @Override
  public boolean isSupportSourceCode() {

    return false;
  }

  @Override
  public void scan(JavaPackage pkg) {

    // TODO Auto-generated method stub

  }

  @Override
  public JavaPackage getPackage(String qualifiedName) {

    Package pkg = Package.getPackage(qualifiedName);
    if (pkg == null) {
      return null;
    }
    JavaSource source = getContext().getSource(); // TODO actually dead wrong!
    return getPackage(source, pkg);
  }

  @Override
  public JavaType getType(String qualifiedName) {

    try {
      Class<?> clazz = this.classloader.loadClass(qualifiedName);
      return (JavaType) getType(clazz);
    } catch (ClassNotFoundException e) {
      LOG.debug("Class {} not found.", qualifiedName, e);
      return null;
    }
  }

}
