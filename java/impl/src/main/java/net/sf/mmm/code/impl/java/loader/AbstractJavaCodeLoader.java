/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.loader;

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.base.BaseFile;
import net.sf.mmm.code.base.BasePackage;
import net.sf.mmm.code.base.loader.BaseCodeLoader;
import net.sf.mmm.code.base.node.BaseNodeItemContainerAccess;
import net.sf.mmm.code.impl.java.JavaContext;

/**
 * Abstract base implementation of {@link BaseCodeLoader}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class AbstractJavaCodeLoader extends BaseNodeItemContainerAccess implements BaseCodeLoader {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractJavaCodeLoader.class);

  private JavaContext context;

  private void requireByteCodeSupport() {

    if (!isSupportByteCode()) {
      throw new IllegalStateException("This code loader does not support loading byte-code via reflection!");
    }
  }

  /**
   * @return the {@link JavaContext}.
   */
  public JavaContext getContext() {

    return this.context;
  }

  /**
   * @param context the initial {@link #getContext() context}.
   */
  public void setContext(JavaContext context) {

    if (this.context == null) {
      this.context = context;
    }
    if (this.context != context) {
      throw new IllegalStateException("Already initialized!");
    }
  }

  public abstract Supplier<BaseFile> getSourceFileSupplier(BasePackage pkg, String simpleName);

}
