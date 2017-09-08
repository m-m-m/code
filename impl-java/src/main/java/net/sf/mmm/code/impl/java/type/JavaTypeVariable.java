/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.type;

import java.io.IOException;

import net.sf.mmm.code.api.member.CodeOperation;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeTypeVariable;
import net.sf.mmm.code.api.type.CodeTypeVariables;

/**
 * Implementation of {@link CodeTypeVariable} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaTypeVariable extends JavaGenericType implements CodeTypeVariable, CodeNodeItemWithGenericParent<JavaTypeVariables, JavaTypeVariable> {

  private final JavaTypeVariables parent;

  private JavaGenericType bound;

  private String name;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param name the {@link #getName() name}.
   */
  public JavaTypeVariable(JavaTypeVariables parent, String name) {

    super();
    this.parent = parent;
    this.name = name;
    this.bound = getContext().getRootType();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaTypeVariable} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaTypeVariable(JavaTypeVariable template, JavaTypeVariables parent) {

    super(template);
    this.parent = parent;
    this.name = template.name;
    this.bound = template.bound;
  }

  @Override
  public JavaTypeVariables getParent() {

    return this.parent;
  }

  @Override
  public JavaTypeVariable asTypeVariable() {

    return this;
  }

  @Override
  public JavaGenericType resolve(CodeGenericType context) {

    // TODO Auto-generated method stub
    return this;
  }

  @Override
  public CodeGenericType getBound() {

    return this.bound;
  }

  @Override
  public void writeReference(Appendable sink, boolean declaration) throws IOException {

    sink.append(this.name);
    if (declaration) {
      if (isSuper()) {
        sink.append(" super ");
      } else {
        sink.append(" extends ");
      }
      this.bound.writeReference(sink, false);
    }
  }

  @Override
  public String getName() {

    return this.name;
  }

  @Override
  public void setName(String name) {

    verifyMutalbe();
    this.parent.rename(this, this.name, name, this::doSetName);
  }

  private void doSetName(String newName) {

    this.name = newName;
  }

  @Override
  public JavaType getDeclaringType() {

    return this.parent.getDeclaringType();
  }

  @Override
  public CodeOperation getDeclaringOperation() {

    return this.parent.getDeclaringOperation();
  }

  @Override
  public boolean isExtends() {

    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isSuper() {

    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isWildcard() {

    return "?".equals(this.name);
  }

  @Override
  public JavaType asType() {

    return this.bound.asType();
  }

  /**
   * @deprecated a {@link CodeTypeVariable} can not have {@link CodeTypeVariables}. The result will always be
   *             empty and {@link #isImmutable() immutable}.
   */
  @Override
  @Deprecated
  public JavaTypeVariables getTypeVariables() {

    return JavaTypeVariables.EMPTY;
  }

  @Override
  public JavaTypeVariable copy() {

    return copy(this.parent);
  }

  @Override
  public JavaTypeVariable copy(JavaTypeVariables newParent) {

    return new JavaTypeVariable(this, newParent);
  }

}
