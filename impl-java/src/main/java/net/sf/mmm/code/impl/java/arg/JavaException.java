/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.arg;

import net.sf.mmm.code.api.arg.CodeException;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.impl.java.type.JavaType;

/**
 * Implementation of {@link CodeException} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaException extends JavaOperationArg implements CodeException, CodeNodeItemWithGenericParent<JavaExceptions, JavaException> {

  private final JavaExceptions parent;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public JavaException(JavaExceptions parent) {

    super();
    this.parent = parent;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaException} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaException(JavaException template, JavaExceptions parent) {

    super(template);
    this.parent = parent;
  }

  @Override
  public JavaExceptions getParent() {

    return this.parent;
  }

  @Override
  public JavaType getDeclaringType() {

    return getParent().getDeclaringType();
  }

  @Override
  public JavaException copy() {

    return copy(this.parent);
  }

  @Override
  public JavaException copy(JavaExceptions newParent) {

    return new JavaException(this, newParent);
  }

}
