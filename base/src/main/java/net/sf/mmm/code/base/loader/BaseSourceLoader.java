/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.loader;

import net.sf.mmm.code.base.BasePackage;

/**
 * {@link BaseLoader} that encapsulates the physical loading of code from a
 * {@link net.sf.mmm.code.base.source.BaseSource}.
 *
 * @see net.sf.mmm.code.base.source.BaseSourceImpl#getLoader()
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public interface BaseSourceLoader extends BaseLoader {

  /**
   * @param pkg the {@link BasePackage} to scan. Will load all {@link BasePackage#getChildren() children} of
   *        the package.
   */
  void scan(BasePackage pkg);

}
