/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.security.CodeSource;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.CodeName;
import net.sf.mmm.code.api.expression.CodeExpression;
import net.sf.mmm.code.api.syntax.CodeSyntax;
import net.sf.mmm.code.base.BaseContext;
import net.sf.mmm.code.base.BaseFile;
import net.sf.mmm.code.base.BasePackage;
import net.sf.mmm.code.base.BasePathElement;
import net.sf.mmm.code.base.BasePathElements;
import net.sf.mmm.code.base.arg.BaseOperationArg;
import net.sf.mmm.code.base.element.BaseElement;
import net.sf.mmm.code.base.element.BaseElementImpl;
import net.sf.mmm.code.base.element.BaseElementWithTypeVariables;
import net.sf.mmm.code.base.loader.BaseCodeLoader;
import net.sf.mmm.code.base.member.BaseOperation;
import net.sf.mmm.code.base.node.BaseNode;
import net.sf.mmm.code.base.node.BaseNodeItemImpl;
import net.sf.mmm.code.base.source.BaseSource;
import net.sf.mmm.code.base.type.BaseArrayType;
import net.sf.mmm.code.base.type.BaseGenericType;
import net.sf.mmm.code.base.type.BaseParameterizedType;
import net.sf.mmm.code.base.type.BaseType;
import net.sf.mmm.code.base.type.BaseTypeVariable;
import net.sf.mmm.code.base.type.BaseTypeVariables;
import net.sf.mmm.code.base.type.BaseTypeWildcard;
import net.sf.mmm.code.impl.java.expression.constant.JavaConstant;
import net.sf.mmm.code.impl.java.loader.AbstractJavaCodeLoader;
import net.sf.mmm.code.impl.java.source.JavaSource;
import net.sf.mmm.util.exception.api.IllegalCaseException;

/**
 * Implementation of {@link net.sf.mmm.code.api.CodeContext} for Java.
 *
 * @author hohwille
 * @since 1.0.0
 */
public abstract class JavaContext extends JavaProvider implements BaseContext, BaseCodeLoader {

  private static final Logger LOG = LoggerFactory.getLogger(JavaContext.class);

  private final AbstractJavaCodeLoader loader;

  private final JavaSource source;

  /**
   * The constructor.
   *
   * @param loader the {@link BaseCodeLoader}.
   * @param source the {@link #getSource() source}.
   */
  public JavaContext(AbstractJavaCodeLoader loader, JavaSource source) {

    super();
    this.loader = loader;
    this.loader.setContext(this);
    this.source = source;
    this.source.setContext(this);
  }

  @Override
  public abstract JavaContext getParent();

  @Override
  public JavaContext getContext() {

    return this;
  }

  @Override
  public JavaSource getSource() {

    return this.source;
  }

  @Override
  public CodeSyntax getSyntax() {

    return getRootContext().getSyntax();
  }

  /**
   * @return the root {@link JavaContext context} responsible for the fundamental code (from JDK).
   */
  public abstract JavaRootContext getRootContext();

  @Override
  public BasePackage getRootPackage() {

    return this.source.getRootPackage();
  }

  /**
   * @return the {@link JavaSource#getToplevelPackage() top-level package}.
   */
  public BasePackage getToplevelPackage() {

    BasePackage pkg = getRootPackage();
    boolean todo = true;
    while (todo) {
      todo = false;
      List<BasePathElement> children = pkg.getChildren().getInternalList();
      if (children.size() == 1) {
        BasePathElement child = children.get(0);
        if (child.isFile()) {
          pkg = (BasePackage) child;
          todo = true;
        }
      }
    }
    return pkg;
  }

  protected AbstractJavaCodeLoader getLoader() {

    return this.loader;
  }

  @Override
  public BaseGenericType getType(Class<?> clazz) {

    // this.loader.requireByteCodeSupport();
    if (clazz.isArray()) {
      BaseGenericType componentType = getType(clazz.getComponentType());
      return componentType.createArray();
    }
    CodeSource codeSource = clazz.getProtectionDomain().getCodeSource();
    JavaSource javaSource = /* this.context. */getOrCreateSource(codeSource);
    Package pkg = clazz.getPackage();
    BasePackage basePackage = getPackage(javaSource, pkg);
    return getTypeInternal(clazz, basePackage);
  }

  private BaseType getTypeInternal(Class<?> clazz, BasePackage pkg) {

    boolean withoutSuperLayer = true; // TODO revisit
    String simpleName = clazz.getSimpleName();
    Class<?> declaringClass = clazz.getDeclaringClass();
    BaseType type = pkg.getChildren().getType(simpleName, withoutSuperLayer, false);
    if (type != null) {
      return type;
    }
    BaseType declaringType = null;
    if (declaringClass != null) {
      declaringType = getTypeInternal(declaringClass, pkg);
      BaseFile file = declaringType.getFile();
      type = new BaseType(file, simpleName, declaringType, clazz);
      addContainerItem(declaringType.getNestedTypes(), type);
    } else {
      getLoader().getSourceFileSupplier(pkg, clazz.getSimpleName());
      BaseFile file = new BaseFile(pkg, clazz);
      addPathElementInternal(pkg.getChildren(), file);
      type = file.getType();
    }
    return type;
  }

  @Override
  public BaseGenericType getType(Type type, BaseElement declaringElement) {

    // this.loader.requireByteCodeSupport();
    if (type instanceof Class) {
      return getType((Class<?>) type);
    } else if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;
      return new BaseParameterizedType((BaseElementImpl) declaringElement, parameterizedType);
    } else if (type instanceof TypeVariable) {
      TypeVariable<?> typeVar = (TypeVariable<?>) type;
      if (declaringElement instanceof BaseType) {
        BaseType declaringType = (BaseType) declaringElement;
        assert (typeVar.getGenericDeclaration() == declaringType.getReflectiveObject());
        return new BaseTypeVariable(declaringType.getTypeParameters(), typeVar);
      } else if (declaringElement instanceof BaseOperation) {
        BaseOperation operation = (BaseOperation) declaringElement;
        if (typeVar.getGenericDeclaration() == operation.getReflectiveObject()) {
          return new BaseTypeVariable(operation.getTypeParameters(), typeVar);
        }
      }
      BaseTypeVariable typeVariable = findTypeVariable(declaringElement, typeVar.getName());
      if (typeVariable != null) {
        return typeVariable;
      }
      LOG.warn("Could not find type variable {} in {}", typeVar, declaringElement);
      return new BaseTypeVariable(declaringElement.getDeclaringType().getTypeParameters(), typeVar);
    } else if (type instanceof WildcardType) {
      WildcardType wildcard = (WildcardType) type;
      BaseNodeItemImpl parent;
      if (declaringElement instanceof BaseParameterizedType) {
        parent = ((BaseParameterizedType) declaringElement).getTypeParameters();
      } else {
        parent = (BaseNodeItemImpl) declaringElement;
      }
      return new BaseTypeWildcard(parent, wildcard);
    } else if (type instanceof GenericArrayType) {
      // GenericArrayType arrayType = (GenericArrayType) type;
      return new BaseArrayType((BaseElementImpl) declaringElement, type);
    } else {
      throw new IllegalCaseException(type.getClass().getSimpleName());
    }
  }

  private BaseElementWithTypeVariables findElementWithTypeVariables(BaseNode node) {

    if (node instanceof BaseElementWithTypeVariables) {
      return (BaseElementWithTypeVariables) node;
    } else if (node instanceof BaseOperationArg) {
      BaseOperationArg arg = (BaseOperationArg) node;
      BaseOperation operation = arg.getDeclaringOperation();
      if (operation != null) {
        return operation;
      } else {
        return arg.getDeclaringType();
      }
    } else if (node instanceof BaseGenericType) {
      return findElementWithTypeVariables(node.getParent());
    } else if (node instanceof BaseTypeVariables) {
      return findElementWithTypeVariables(node.getParent());
    } else {
      throw new IllegalCaseException(node.getClass().getSimpleName());
    }
  }

  private BaseTypeVariable findTypeVariable(BaseNode node, String name) {

    BaseElementWithTypeVariables elementWithTypeVariables = findElementWithTypeVariables(node);
    if (elementWithTypeVariables != null) {
      return elementWithTypeVariables.getTypeParameters().get(name, true);
    }
    return null;
  }

  @Override
  public boolean isSupportByteCode() {

    return this.loader.isSupportByteCode();
  }

  @Override
  public boolean isSupportSourceCode() {

    return this.loader.isSupportSourceCode();
  }

  @Override
  public BasePackage getPackage(BaseSource codeSource, Package pkg) {

    BasePackage rootPackage = codeSource.getRootPackage();
    if (pkg == null) {
      return rootPackage;
    }
    return getPackage(rootPackage, pkg, parseName(pkg.getName()));
  }

  private BasePackage getPackage(BasePackage root, Package pkg, CodeName qname) {

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
      childPackage = new BasePackage(parentPackage, simpleName, reflectiveObject, superLayerPackage, null);
      addPathElementInternal(children, childPackage);
    }
    return childPackage;
  }

  @Override
  public void scan(BasePackage pkg) {

    ((JavaContext) pkg.getContext()).getLoader().scan(pkg);
  }

  /**
   * @param codeSource the {@link CodeSource}.
   * @return the existing or otherwise created {@link JavaSource}.
   */
  protected abstract JavaSource getOrCreateSource(CodeSource codeSource);

  @Override
  public abstract BaseType getRootType();

  @Override
  public abstract BaseType getRootEnumerationType();

  @Override
  public abstract BaseType getVoidType();

  @Override
  public abstract BaseType getRootExceptionType();

  @Override
  public abstract BaseTypeWildcard getUnboundedWildcard();

  @Override
  public CodeExpression createExpression(Object value, boolean primitive) {

    return JavaConstant.of(value, primitive);
  }

}
