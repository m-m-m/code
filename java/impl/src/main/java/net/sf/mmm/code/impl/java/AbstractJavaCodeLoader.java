/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.CodeName;
import net.sf.mmm.code.base.BasePackage;
import net.sf.mmm.code.base.BasePathElements;
import net.sf.mmm.code.base.loader.BaseCodeLoader;
import net.sf.mmm.code.base.node.BaseNodeItemContainerAccess;
import net.sf.mmm.code.base.source.BaseSource;

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

  @Override
  public BasePackage getPackage(BaseSource source, Package pkg) {

    BasePackage rootPackage = source.getRootPackage();
    if (pkg == null) {
      return rootPackage;
    }
    return getPackage(rootPackage, pkg, this.context.parseName(pkg.getName()));
  }

  private BasePackage getPackage(BasePackage root, Package pkg, CodeName qname) {

    requireByteCodeSupport();
    if (qname == null) {
      return root;
    }
    CodeName parentName = qname.getParent();
    Package parentPkg = null; // Package.getPackage(parentName.getFullName());
    BasePackage parentPackage = getPackage(root, parentPkg, parentName);
    String simpleName = qname.getSimpleName();
    BasePathElements children = parentPackage.getChildren();
    BasePackage childPackage = children.getPackage(simpleName, false, false);
    if (childPackage == null) {
      Package reflectiveObject = pkg;
      if (reflectiveObject == null) {
        reflectiveObject = Package.getPackage(qname.getFullName());
      }
      BasePackage superLayerPackage = null; // TODO
      childPackage = new BasePackage(parentPackage, simpleName, reflectiveObject, superLayerPackage);
      addPathElementInternal(children, childPackage);
    }
    return childPackage;
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

}
