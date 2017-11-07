/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base;

import java.io.File;
import java.lang.reflect.Type;
import java.util.function.BiFunction;

import net.sf.mmm.code.api.CodeName;
import net.sf.mmm.code.api.expression.CodeExpression;
import net.sf.mmm.code.api.language.CodeLanguage;
import net.sf.mmm.code.api.language.CodeLanguageJava;
import net.sf.mmm.code.base.element.BaseElementWithDeclaringType;
import net.sf.mmm.code.base.loader.BaseLoader;
import net.sf.mmm.code.base.loader.BaseSourceLoader;
import net.sf.mmm.code.base.loader.BaseSourceLoaderImpl;
import net.sf.mmm.code.base.source.BaseSource;
import net.sf.mmm.code.base.source.BaseSourceImpl;
import net.sf.mmm.code.base.type.BaseGenericType;
import net.sf.mmm.code.base.type.BaseType;
import net.sf.mmm.code.base.type.BaseTypeWildcard;

/**
 * Dummy implementation of {@link BaseContextImpl} for testing.
 */
public class TestContext extends BaseContextImplWithCache {

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
  public BaseTypeWildcard getUnboundedWildcard() {

    throw new UnsupportedOperationException();
  }

  @Override
  public BaseGenericType getType(Type type, BaseElementWithDeclaringType declaringElement) {

    throw new UnsupportedOperationException();
  }

  @Override
  public BaseType getNonPrimitiveType(BaseType javaType) {

    throw new UnsupportedOperationException();
  }

  @Override
  public CodeLanguage getLanguage() {

    return CodeLanguageJava.INSTANCE;
  }

  @Override
  public String getQualifiedNameForStandardType(String simpleName, boolean omitStandardPackages) {

    throw new UnsupportedOperationException();
  }

  @Override
  public CodeExpression createExpression(Object value, boolean primitive) {

    throw new UnsupportedOperationException();
  }

  @Override
  public BaseContext getParent() {

    return null;
  }

  @Override
  protected BaseLoader getLoader() {

    return getSource().getLoader();
  }

  private static class TestSoureLoader extends BaseSourceLoaderImpl {

    @Override
    public BaseType getType(String qualifiedName) {

      try {
        Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(qualifiedName);
        return (BaseType) getType(clazz);
      } catch (ClassNotFoundException e) {
        // fail(e.getMessage(), e);
        return null;
      }
    }

    @Override
    public BaseType getType(CodeName qualifiedName) {

      throw new IllegalStateException();
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
        BiFunction<BasePackage, String, BasePackage> factory = (parentPkg, simpleName) -> new BasePackage(parentPkg, simpleName, pkg, null);
        parentPackage = getPackage(parentPackage.getChildren(), source.parseName(pkgName), false, factory, true, true);
      }
      BasePathElements children = parentPackage.getChildren();
      BaseType type = children.getType(clazz.getSimpleName(), false);
      if (type == null) {
        BaseFile file = new BaseFile(parentPackage, clazz, null);
        addPathElementInternal(children, file);
        type = file.getType();
      }
      return type;
    }

    @Override
    public void scan(BasePackage pkg) {

      // not implemented
    }

  }

}
