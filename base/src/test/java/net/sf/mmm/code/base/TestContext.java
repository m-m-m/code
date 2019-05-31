/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.BiFunction;

import net.sf.mmm.code.api.CodeName;
import net.sf.mmm.code.api.element.CodeElementWithDeclaringType;
import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.api.language.JavaLanguage;
import net.sf.mmm.code.base.loader.BaseLoader;
import net.sf.mmm.code.base.loader.BaseSourceLoader;
import net.sf.mmm.code.base.loader.BaseSourceLoaderImpl;
import net.sf.mmm.code.base.source.BaseSource;
import net.sf.mmm.code.base.source.BaseSourceImpl;
import net.sf.mmm.code.base.type.BaseGenericType;
import net.sf.mmm.code.base.type.BaseType;
import net.sf.mmm.code.base.type.BaseTypeWildcard;

/**
 * Dummy implementation of {@link AbstractBaseContext} for testing.
 */
public class TestContext extends AbstractBaseContextWithCache {

  /**
   * The constructor.
   */
  public TestContext() {

    super(createSource());
  }

  private static BaseSourceImpl createSource() {

    BaseSourceLoader loader = new TestSoureLoader();
    BaseSourceImpl source = new BaseSourceImpl(new File(""), null, null, null, loader);
    return source;
  }

  @Override
  public BaseType getRootType() {

    return (BaseType) getType(Object.class);
  }

  @Override
  public BaseType getRootEnumerationType() {

    return (BaseType) getType(Enum.class);
  }

  @Override
  public BaseType getVoidType() {

    return (BaseType) getType(void.class);
  }

  @Override
  public BaseType getRootExceptionType() {

    return (BaseType) getType(Throwable.class);
  }

  @Override
  public BaseType getBooleanType(boolean primitive) {

    if (primitive) {
      return (BaseType) getType(boolean.class);
    } else {
      return (BaseType) getType(Boolean.class);
    }
  }

  @Override
  public BaseTypeWildcard getUnboundedWildcard() {

    throw new UnsupportedOperationException();
  }

  @Override
  public BaseGenericType getType(Type type, CodeElementWithDeclaringType declaringElement) {

    if (type instanceof Class) {
      return getType((Class<?>) type);
    } else if (type instanceof ParameterizedType) {
      return getType(((ParameterizedType) type).getRawType(), declaringElement);
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public BaseType getNonPrimitiveType(BaseType javaType) {

    throw new UnsupportedOperationException();
  }

  @Override
  public CodeLanguage getLanguage() {

    return JavaLanguage.get();
  }

  @Override
  public String getQualifiedNameForStandardType(String simpleName, boolean omitStandardPackages) {

    throw new UnsupportedOperationException();
  }

  @Override
  public BaseFactory getFactory() {

    return new TestFactory();
  }

  @Override
  public BaseContext getParent() {

    return null;
  }

  @Override
  protected BaseLoader getLoader() {

    return getSource().getLoader();
  }

  @Override
  public ClassLoader getClassLoader() {

    return Thread.currentThread().getContextClassLoader();
  }

  private static class TestSoureLoader extends BaseSourceLoaderImpl {

    @Override
    public BaseType getType(String qualifiedName) {

      try {
        Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(qualifiedName);
        return (BaseType) getType(clazz);
      } catch (ClassNotFoundException e) {
        // fail(e.getMessage(), e);
        BaseSource source = getSource();
        if (source.isMutable()) {
          CodeName path = getContext().parseName(qualifiedName);
          BaseFile file = source.getRootPackage().getChildren().getFile(path);
          if (file != null) {
            return file.getType();
          }
        }
        return null;
      }
    }

    @Override
    public BaseType getType(CodeName qualifiedName) {

      return null;
    }

    @Override
    public BaseGenericType getType(Class<?> clazz) {

      if (clazz.isArray()) {
        BaseGenericType componentType = getType(clazz.getComponentType());
        return componentType.createArray();
      }
      BaseSource source = getSource();
      BasePackage parentPackage = source.getRootPackage();
      Package pkg = clazz.getPackage();
      if (pkg != null) {
        String pkgName = pkg.getName();
        BiFunction<BasePackage, String, BasePackage> factory = (parentPkg, simpleName) -> createPackage(pkg, parentPkg,
            simpleName);
        parentPackage = getPackage(parentPackage.getChildren(), source.parseName(pkgName), false, factory, true, true);
      }
      BasePathElements children = parentPackage.getChildren();
      BaseType type = (BaseType) children.getType(clazz.getSimpleName(), false);
      if (type == null) {
        BaseFile file = new BaseFile(parentPackage, clazz, null);
        addPathElementInternal(children, file);
        type = file.getType();
      }
      return type;
    }

    private BasePackage createPackage(Package pkg, BasePackage parentPkg, String simpleName) {

      BasePackage basePackage = new BasePackage(parentPkg, simpleName, pkg, null, (pkg != null));
      basePackage.setImmutable();
      return basePackage;
    }

    @Override
    public void scan(BasePackage pkg) {

      // not implemented
    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public ClassLoader getClassLoader() {

      return Thread.currentThread().getContextClassLoader();
    }

  }

}