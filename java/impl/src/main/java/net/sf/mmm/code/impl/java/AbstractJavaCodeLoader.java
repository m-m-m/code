/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.security.CodeSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.CodeName;
import net.sf.mmm.code.base.BaseFile;
import net.sf.mmm.code.base.BasePackage;
import net.sf.mmm.code.base.BasePathElements;
import net.sf.mmm.code.base.arg.BaseOperationArg;
import net.sf.mmm.code.base.element.BaseElement;
import net.sf.mmm.code.base.element.BaseElementWithTypeVariables;
import net.sf.mmm.code.base.member.BaseOperation;
import net.sf.mmm.code.base.node.BaseNode;
import net.sf.mmm.code.base.node.BaseNodeItem;
import net.sf.mmm.code.base.node.BaseNodeItemContainerAccess;
import net.sf.mmm.code.base.type.BaseArrayType;
import net.sf.mmm.code.base.type.BaseGenericType;
import net.sf.mmm.code.base.type.BaseParameterizedType;
import net.sf.mmm.code.base.type.BaseType;
import net.sf.mmm.code.base.type.BaseTypeVariable;
import net.sf.mmm.code.base.type.BaseTypeVariables;
import net.sf.mmm.code.base.type.BaseTypeWildcard;
import net.sf.mmm.code.impl.java.loader.JavaCodeLoader;
import net.sf.mmm.code.impl.java.source.JavaSource;
import net.sf.mmm.util.exception.api.IllegalCaseException;

/**
 * Abstract base implementation of {@link JavaCodeLoader}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class AbstractJavaCodeLoader extends BaseNodeItemContainerAccess implements JavaCodeLoader {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractJavaCodeLoader.class);

  private JavaContext context;

  private void requireByteCodeSupport() {

    if (!isSupportByteCode()) {
      throw new IllegalStateException("This code loader does not support loading byte-code via reflection!");
    }
  }

  @Override
  public BaseGenericType getType(Class<?> clazz) {

    requireByteCodeSupport();
    if (clazz.isArray()) {
      BaseGenericType componentType = getType(clazz.getComponentType());
      return componentType.createArray();
    }
    CodeSource codeSource = clazz.getProtectionDomain().getCodeSource();
    JavaSource javaSource = this.context.getOrCreateSource(codeSource);
    Package pkg = clazz.getPackage();
    BasePackage basePackage = getPackage(javaSource, pkg);
    return getType(clazz, basePackage);
  }

  private BaseType getType(Class<?> clazz, BasePackage pkg) {

    String simpleName = clazz.getSimpleName();
    BaseType type = pkg.getChildren().getType(simpleName, false, false);
    if (type != null) {
      return type;
    }
    BaseType declaringType = null;
    Class<?> declaringClass = clazz.getDeclaringClass();
    if (declaringClass != null) {
      declaringType = getType(declaringClass, pkg);
      BaseFile file = declaringType.getFile();
      type = new BaseType(file, simpleName, declaringType, clazz);
      addContainerItem(declaringType.getNestedTypes(), type);
    } else {
      BaseFile file = new BaseFile(pkg, clazz);
      addPathElementInternal(pkg.getChildren(), file);
      type = file.getType();
    }
    return type;
  }

  @Override
  public BasePackage getPackage(JavaSource source, Package pkg) {

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

  @Override
  public BaseGenericType getType(Type type, BaseElement declaringElement) {

    requireByteCodeSupport();
    if (type instanceof Class) {
      return getType((Class<?>) type);
    } else if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;
      return new BaseParameterizedType(declaringElement, parameterizedType);
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
      BaseNodeItem parent;
      if (declaringElement instanceof BaseParameterizedType) {
        parent = ((BaseParameterizedType) declaringElement).getTypeParameters();
      } else {
        parent = declaringElement;
      }
      return new BaseTypeWildcard(parent, wildcard);
    } else if (type instanceof GenericArrayType) {
      // GenericArrayType arrayType = (GenericArrayType) type;
      return new BaseArrayType(declaringElement, type);
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
