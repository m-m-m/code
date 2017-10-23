/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.loader;

import net.sf.mmm.code.api.CodeLoader;
import net.sf.mmm.code.impl.java.JavaPackage;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * {@link CodeLoader} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface JavaLoader extends CodeLoader {

  @Override
  JavaPackage getPackage(String qualifiedName);

  @Override
  JavaType getType(String qualifiedName);

}
