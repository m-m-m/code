/* CopyrighJavaType (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base;

import net.sf.mmm.code.api.CodeName;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.base.loader.BaseLoader;
import net.sf.mmm.code.base.node.BaseNodeItemContainerAccess;
import net.sf.mmm.code.base.type.BaseType;
import net.sf.mmm.util.exception.api.ObjectNotFoundException;

/**
 * Base implementation of {@link BaseProvider}.
 *
 * @author Joerg Hohwiller (hohwille aJavaType users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BaseProviderImpl extends BaseNodeItemContainerAccess implements BaseProvider {

  /**
   * The constructor.
   */
  public BaseProviderImpl() {

    super();
  }

  @Override
  public BasePackage getPackage(String qualifiedName) {

    if (qualifiedName.isEmpty()) {
      return getRootPackage();
    }
    return getPackage(parseName(qualifiedName));
  }

  @Override
  public BasePackage getPackage(CodeName qualifiedName) {

    if (qualifiedName == null) {
      return getRootPackage();
    }
    BaseContext context = getContext();
    boolean withoutSuperLayer = (context != this); // determine if called in context or in source
    BasePackage pkg = getRootPackage().getChildren().getPackage(qualifiedName, withoutSuperLayer, false);
    if (pkg == null) {
      BaseLoader loader = getLoader();
      if (loader != null) {
        pkg = loader.getPackage(qualifiedName.getFullName());
      }
    }
    return pkg;
  }

  @Override
  public BaseType getType(String qualifiedName) {

    return getType(parseName(qualifiedName));
  }

  @Override
  public BaseType getType(CodeName qualifiedName) {

    BasePackage pkg = getPackage(qualifiedName.getParent());
    if (pkg == null) {
      return null;
    }
    BasePathElements children = pkg.getChildren();
    String simpleName = qualifiedName.getSimpleName();
    BaseContext context = getContext();
    boolean withoutSuperLayer = (context != this);
    BaseFile file = children.getFile(simpleName, withoutSuperLayer, false);
    if (file != null) {
      return file.getType();
    }
    BaseType type = children.getType(simpleName);
    if (type == null) {
      BaseLoader loader = getLoader();
      if (loader != null) {
        type = loader.getType(qualifiedName);
      }
    }
    return type;
  }

  @Override
  public BaseType getRequiredType(String qualifiedName) {

    BaseType type = getType(qualifiedName);
    if (type == null) {
      throw new ObjectNotFoundException(CodeType.class, qualifiedName);
    }
    return type;
  }

}
