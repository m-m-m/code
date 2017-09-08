/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.member;

import net.sf.mmm.code.api.arg.CodeReturn;
import net.sf.mmm.code.api.member.CodeMethod;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.impl.java.type.JavaGenericType;
import net.sf.mmm.code.impl.java.type.JavaSuperTypes;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link CodeMethod} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaMethod extends JavaOperation implements CodeMethod, CodeNodeItemWithGenericParent<JavaMethods, JavaMethod> {

  private final JavaMethods parent;

  private CodeReturn returns;

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaMethod(JavaMethods parent, String name) {

    super(name);
    this.parent = parent;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaMethod} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaMethod(JavaMethod template, JavaMethods parent) {

    super(template);
    this.parent = parent;
  }

  @Override
  public JavaMethods getParent() {

    return this.parent;
  }

  @Override
  protected void doSetImmutable() {

    super.doSetImmutable();
    this.returns.setImmutable();
  }

  @Override
  public CodeReturn getReturns() {

    return this.returns;
  }

  @Override
  public void setReturns(CodeReturn returns) {

    verifyMutalbe();
    this.returns = returns;
  }

  @Override
  public JavaMethod getParentMethod() {

    return getParentMethod(this.parent.getParent());
  }

  private JavaMethod getParentMethod(JavaType type) {

    JavaMethod parentMethod;
    if (type.isClass() || type.isEnumeration()) { // enumeration can override Object methods
      parentMethod = getParentMethodFromClasses(type);
      if (parentMethod != null) {
        return parentMethod;
      }
    }
    return getParentMethodFromInterfaces(type);
  }

  private JavaMethod getParentMethodFromClasses(JavaType type) {

    JavaGenericType superGenericClass = type.getSuperTypes().getSuperClass();
    if (superGenericClass == null) {
      return null;
    }
    JavaType superClass = superGenericClass.asType();
    JavaMethod parentMethod = superClass.getMethods().get(this);
    if (parentMethod != null) {
      return parentMethod;
    }
    return getParentMethodFromClasses(superClass);
  }

  private JavaMethod getParentMethodFromInterfaces(JavaType type) {

    JavaSuperTypes superTypes = type.getSuperTypes();
    for (JavaGenericType superType : superTypes.getDeclared()) {
      if (superType.isInterface()) {
        JavaType superInterface = superType.asType();
        JavaMethod parentMethod = superInterface.getMethods().get(this);
        if (parentMethod != null) {
          return parentMethod;
        }
        parentMethod = getParentMethodFromInterfaces(superInterface);
        if (parentMethod != null) {
          return parentMethod;
        }
      }
    }
    return null;
  }

  @Override
  public JavaMethod copy() {

    return copy(this.parent);
  }

  @Override
  public JavaMethod copy(JavaMethods newParent) {

    return new JavaMethod(this, newParent);
  }

}
