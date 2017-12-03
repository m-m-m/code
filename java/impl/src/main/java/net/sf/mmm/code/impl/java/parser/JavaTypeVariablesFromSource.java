/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.parser;

import java.lang.reflect.GenericDeclaration;

import net.sf.mmm.code.api.annotation.CodeAnnotations;
import net.sf.mmm.code.api.comment.CodeComment;
import net.sf.mmm.code.api.copy.CodeCopyMapper;
import net.sf.mmm.code.api.doc.CodeDoc;
import net.sf.mmm.code.api.element.CodeElementWithTypeVariables;
import net.sf.mmm.code.base.member.BaseOperation;
import net.sf.mmm.code.base.type.BaseType;
import net.sf.mmm.code.base.type.BaseTypeVariables;

/**
 * {@link BaseTypeVariables} for operations that have to be created before the operation is parsed.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaTypeVariablesFromSource extends BaseTypeVariables implements CodeElementWithTypeVariables {

  /**
   * The constructor.
   */
  public JavaTypeVariablesFromSource() {

    super((BaseType) null);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseTypeVariables} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public JavaTypeVariablesFromSource(JavaTypeVariablesFromSource template, CodeCopyMapper mapper) {

    super(template, mapper);
  }

  @Override
  public void setParent(BaseOperation declaringOperation) {

    super.setParent(declaringOperation);
  }

  @Override
  public void setParent(BaseType declaringType) {

    super.setParent(declaringType);
  }

  @Override
  public BaseTypeVariables getTypeParameters() {

    return this;
  }

  @Override
  public JavaTypeVariablesFromSource copy() {

    return copy(getDefaultCopyMapper());
  }

  @Override
  public JavaTypeVariablesFromSource copy(CodeCopyMapper mapper) {

    return new JavaTypeVariablesFromSource(this, mapper);
  }

  // sick stuff from here...

  @Override
  public CodeAnnotations getAnnotations() {

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
