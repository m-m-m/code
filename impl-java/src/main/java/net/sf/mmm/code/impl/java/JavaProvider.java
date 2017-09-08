/* CopyrighJavaType (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import net.sf.mmm.code.api.CodeContext;
import net.sf.mmm.code.api.CodeName;
import net.sf.mmm.code.api.CodeProvider;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Base implementation of {@link CodeContext}.
 *
 * @author Joerg Hohwiller (hohwille aJavaType users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class JavaProvider implements CodeProvider, JavaLoader {

  /**
   * The constructor.
   */
  public JavaProvider() {

    super();
  }

  @Override
  public abstract JavaPackage getRootPackage();

  @Override
  public abstract JavaContext getContext();

  @Override
  public JavaPackage getPackage(String qualifiedName) {

    if (qualifiedName.isEmpty()) {
      return getRootPackage();
    }
    return getPackage(getHierarchicalName(qualifiedName));
  }

  @Override
  public JavaPackage getPackage(CodeName qualifiedName) {

    if (qualifiedName == null) {
      return getRootPackage();
    }
    JavaContext context = getContext();
    boolean withoutSuperLayer = (context != this);
    JavaPackage pkg = getRootPackage().getChildren().getPackage(qualifiedName, withoutSuperLayer, false);
    if (pkg == null) {
      JavaLoader loader = context.getLoader();
      if (loader != null) {
        pkg = loader.getPackage(qualifiedName.getFullName());
      }
    }
    return pkg;
  }

  @Override
  public JavaType getType(String qualifiedName) {

    return getType(getHierarchicalName(qualifiedName));
  }

  @Override
  public JavaType getType(CodeName qualifiedName) {

    JavaPackage pkg = getPackage(qualifiedName.getParent());
    if (pkg == null) {
      return null;
    }
    JavaPathElements children = pkg.getChildren();
    String simpleName = qualifiedName.getSimpleName();
    JavaContext context = getContext();
    boolean withoutSuperLayer = (context != this);
    JavaFile file = children.getFile(simpleName, withoutSuperLayer, false);
    if (file != null) {
      return file.getType();
    }
    return children.getType(simpleName);
  }

}
