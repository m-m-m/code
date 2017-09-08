/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.arg;

import net.sf.mmm.code.api.arg.CodeParameter;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link CodeParameter} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaParameter extends JavaOperationArg implements CodeParameter, CodeNodeItemWithGenericParent<JavaParameters, JavaParameter> {

  private final JavaParameters parent;

  private String name;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param name the {@link #getName() name}.
   */
  public JavaParameter(JavaParameters parent, String name) {

    super();
    this.parent = parent;
    this.name = name;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaParameter} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaParameter(JavaParameter template, JavaParameters parent) {

    super(template);
    this.parent = parent;
    this.name = template.name;
  }

  @Override
  public JavaParameters getParent() {

    return this.parent;
  }

  @Override
  public JavaType getDeclaringType() {

    return getParent().getDeclaringType();
  }

  @Override
  public String getName() {

    return this.name;
  }

  @Override
  public void setName(String name) {

    verifyMutalbe();
    this.name = name;
  }

  @Override
  public JavaParameter copy() {

    return copy(this.parent);
  }

  @Override
  public JavaParameter copy(JavaParameters newParent) {

    return new JavaParameter(this, newParent);
  }

}
