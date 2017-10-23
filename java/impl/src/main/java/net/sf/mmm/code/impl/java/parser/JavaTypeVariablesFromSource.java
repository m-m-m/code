/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.parser;

import java.lang.reflect.GenericDeclaration;

import net.sf.mmm.code.api.comment.CodeComment;
import net.sf.mmm.code.api.doc.CodeDoc;
import net.sf.mmm.code.impl.java.annotation.JavaAnnotations;
import net.sf.mmm.code.impl.java.element.JavaElementWithTypeVariables;
import net.sf.mmm.code.impl.java.member.JavaOperation;
import net.sf.mmm.code.impl.java.type.JavaType;
import net.sf.mmm.code.impl.java.type.JavaTypeVariables;

/**
 * {@link JavaTypeVariables} for operations that have to be created before the operation is parsed.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaTypeVariablesFromSource extends JavaTypeVariables implements JavaElementWithTypeVariables {

  /**
   * The constructor.
   */
  public JavaTypeVariablesFromSource() {

    super((JavaType) null);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaTypeVariables} to copy.
   * @param declaringOperation the {@link #getDeclaringOperation() declaring operation}.
   */
  public JavaTypeVariablesFromSource(JavaTypeVariablesFromSource template, JavaOperation declaringOperation) {

    super(template, declaringOperation);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaTypeVariables} to copy.
   * @param declaringType the {@link #getDeclaringType() declaring type}.
   */
  public JavaTypeVariablesFromSource(JavaTypeVariablesFromSource template, JavaType declaringType) {

    super(template, declaringType);
  }

  @Override
  public void setParent(JavaOperation declaringOperation) {

    super.setParent(declaringOperation);
  }

  @Override
  public void setParent(JavaType declaringType) {

    super.setParent(declaringType);
  }

  @Override
  public JavaTypeVariables getTypeParameters() {

    return this;
  }

  @Override
  public JavaTypeVariablesFromSource copy() {

    return copy(getParent());
  }

  @Override
  public JavaTypeVariablesFromSource copy(JavaElementWithTypeVariables newParent) {

    if (newParent instanceof JavaType) {
      return new JavaTypeVariablesFromSource(this, (JavaType) newParent);
    } else if (newParent instanceof JavaOperation) {
      return new JavaTypeVariablesFromSource(this, (JavaOperation) newParent);
    } else {
      throw new IllegalArgumentException("" + newParent);
    }
  }

  // sick stuff from here...

  @Override
  public JavaAnnotations getAnnotations() {

    return null;
  }

  @Override
  public CodeComment getComment() {

    return null;
  }

  @Override
  public void setComment(CodeComment comment) {

  }

  @Override
  public GenericDeclaration getReflectiveObject() {

    return null;
  }

  @Override
  public CodeDoc getDoc() {

    return null;
  }

  @Override
  public void removeFromParent() {

  }

}
