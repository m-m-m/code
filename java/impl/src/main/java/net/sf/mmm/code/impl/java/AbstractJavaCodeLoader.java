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
import net.sf.mmm.code.base.node.AbstractCodeNodeItemContainerAccess;
import net.sf.mmm.code.impl.java.arg.JavaOperationArg;
import net.sf.mmm.code.impl.java.element.JavaElement;
import net.sf.mmm.code.impl.java.element.JavaElementImpl;
import net.sf.mmm.code.impl.java.element.JavaElementWithTypeVariables;
import net.sf.mmm.code.impl.java.loader.JavaCodeLoader;
import net.sf.mmm.code.impl.java.member.JavaOperation;
import net.sf.mmm.code.impl.java.node.JavaNode;
import net.sf.mmm.code.impl.java.node.JavaNodeItem;
import net.sf.mmm.code.impl.java.source.JavaSource;
import net.sf.mmm.code.impl.java.type.JavaArrayType;
import net.sf.mmm.code.impl.java.type.JavaGenericType;
import net.sf.mmm.code.impl.java.type.JavaParameterizedType;
import net.sf.mmm.code.impl.java.type.JavaType;
import net.sf.mmm.code.impl.java.type.JavaTypeVariable;
import net.sf.mmm.code.impl.java.type.JavaTypeVariables;
import net.sf.mmm.code.impl.java.type.JavaTypeWildcard;
import net.sf.mmm.util.exception.api.IllegalCaseException;

/**
 * Abstract base implementation of {@link JavaCodeLoader}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class AbstractJavaCodeLoader extends AbstractCodeNodeItemContainerAccess implements JavaCodeLoader {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractJavaCodeLoader.class);

  private JavaContext context;

  private void requireByteCodeSupport() {

    if (!isSupportByteCode()) {
      throw new IllegalStateException("This code loader does not support loading byte-code via reflection!");
    }
  }

  @Override
  public JavaGenericType getType(Class<?> clazz) {

    requireByteCodeSupport();
    if (clazz.isArray()) {
      JavaGenericType componentType = getType(clazz.getComponentType());
      return componentType.createArray();
    }
    CodeSource codeSource = clazz.getProtectionDomain().getCodeSource();
    JavaSource javaSource = this.context.getOrCreateSource(codeSource);
    Package pkg = clazz.getPackage();
    JavaPackage javaPackage = getPackage(javaSource, pkg);
    return getType(clazz, javaPackage);
  }

  private JavaType getType(Class<?> clazz, JavaPackage javaPackage) {

    String simpleName = clazz.getSimpleName();
    JavaType type = javaPackage.getChildren().getType(simpleName, false, false);
    if (type != null) {
      return type;
    }
    JavaType declaringType = null;
    Class<?> declaringClass = clazz.getDeclaringClass();
    if (declaringClass != null) {
      declaringType = getType(declaringClass, javaPackage);
      JavaFile file = declaringType.getFile();
      type = new JavaType(file, simpleName, declaringType, clazz);
      addContainerItem(declaringType.getNestedTypes(), type);
    } else {
      JavaFile file = new JavaFile(javaPackage, clazz);
      javaPackage.getChildren().addInternal(file);
      type = file.getType();
    }
    return type;
  }

  @Override
  public JavaPackage getPackage(JavaSource source, Package pkg) {

    JavaPackage rootPackage = source.getRootPackage();
    if (pkg == null) {
      return rootPackage;
    }
    return getPackage(rootPackage, pkg, this.context.parseName(pkg.getName()));
  }

  private JavaPackage getPackage(JavaPackage root, Package pkg, CodeName qname) {

    requireByteCodeSupport();
    if (qname == null) {
      return root;
    }
    CodeName parentName = qname.getParent();
    Package parentPkg = null; // Package.getPackage(parentName.getFullName());
    JavaPackage parentPackage = getPackage(root, parentPkg, parentName);
    String simpleName = qname.getSimpleName();
    JavaPathElements children = parentPackage.getChildren();
    JavaPackage javaPackage = children.getPackage(simpleName, false, false);
    if (javaPackage == null) {
      Package reflectiveObject = pkg;
      if (reflectiveObject == null) {
        reflectiveObject = Package.getPackage(qname.getFullName());
      }
      JavaPackage superLayerPackage = null; // TODO
      javaPackage = new JavaPackage(parentPackage, simpleName, reflectiveObject, superLayerPackage);
      children.addInternal(javaPackage);
    }
    return javaPackage;
  }

  @Override
  public JavaGenericType getType(Type type, JavaElement declaringElement) {

    requireByteCodeSupport();
    if (type instanceof Class) {
      return getType((Class<?>) type);
    } else if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;
      return new JavaParameterizedType(declaringElement, parameterizedType);
    } else if (type instanceof TypeVariable) {
      TypeVariable<?> typeVar = (TypeVariable<?>) type;
      if (declaringElement instanceof JavaType) {
        JavaType declaringType = (JavaType) declaringElement;
        assert (typeVar.getGenericDeclaration() == declaringType.getReflectiveObject());
        return new JavaTypeVariable(declaringType.getTypeParameters(), typeVar);
      } else if (declaringElement instanceof JavaOperation) {
        JavaOperation operation = (JavaOperation) declaringElement;
        if (typeVar.getGenericDeclaration() == operation.getReflectiveObject()) {
          return new JavaTypeVariable(operation.getTypeParameters(), typeVar);
        }
      }
      JavaTypeVariable typeVariable = findTypeVariable(declaringElement, typeVar.getName());
      if (typeVariable != null) {
        return typeVariable;
      }
      LOG.warn("Could not find type variable {} in {}", typeVar, declaringElement);
      return new JavaTypeVariable(declaringElement.getDeclaringType().getTypeParameters(), typeVar);
    } else if (type instanceof WildcardType) {
      WildcardType wildcard = (WildcardType) type;
      JavaNodeItem parent;
      if (declaringElement instanceof JavaParameterizedType) {
        parent = ((JavaParameterizedType) declaringElement).getTypeParameters();
      } else {
        parent = (JavaElementImpl) declaringElement;
      }
      return new JavaTypeWildcard(parent, wildcard);
    } else if (type instanceof GenericArrayType) {
      // GenericArrayType arrayType = (GenericArrayType) type;
      return new JavaArrayType(declaringElement, type);
    } else {
      throw new IllegalCaseException(type.getClass().getSimpleName());
    }
  }

  private JavaElementWithTypeVariables findElementWithTypeVariables(JavaNode node) {

    if (node instanceof JavaElementWithTypeVariables) {
      return (JavaElementWithTypeVariables) node;
    } else if (node instanceof JavaOperationArg) {
      JavaOperationArg arg = (JavaOperationArg) node;
      JavaOperation operation = arg.getDeclaringOperation();
      if (operation != null) {
        return operation;
      } else {
        return arg.getDeclaringType();
      }
    } else if (node instanceof JavaGenericType) {
      return findElementWithTypeVariables(node.getParent());
    } else if (node instanceof JavaTypeVariables) {
      return findElementWithTypeVariables(node.getParent());
    } else {
      throw new IllegalCaseException(node.getClass().getSimpleName());
    }
  }

  private JavaTypeVariable findTypeVariable(JavaNode node, String name) {

    JavaElementWithTypeVariables elementWithTypeVariables = findElementWithTypeVariables(node);
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
