package io.github.mmm.code.base.loader;

import io.github.mmm.code.api.CodeName;
import io.github.mmm.code.base.BaseFile;
import io.github.mmm.code.base.BasePackage;
import io.github.mmm.code.base.type.BaseGenericType;
import io.github.mmm.code.base.type.BaseType;

/**
 * {@link BaseSourceLoaderImpl} to load from existing {@link BasePackage package structure} in memory.
 *
 * @since 1.0.0
 */
public class BaseSourceLoaderRootPackage extends BaseSourceLoaderImpl {

  /**
   * The constructor.
   */
  public BaseSourceLoaderRootPackage() {

  }

  @Override
  public void scan(BasePackage pkg) {

    // nothing to do...
  }

  @Override
  public BaseType getType(String qualifiedName) {

    return getType(getContext().parseName(qualifiedName));
  }

  @Override
  public BaseType getType(CodeName qualifiedName) {

    BaseFile file = getSource().getRootPackage().getChildren().getFile(qualifiedName);
    if (file != null) {
      return file.getType();
    }
    return null;
  }

  @Override
  public BaseGenericType getType(Class<?> clazz) {

    return null;
  }

  @Override
  public void close() {

    // ignore
  }

}
