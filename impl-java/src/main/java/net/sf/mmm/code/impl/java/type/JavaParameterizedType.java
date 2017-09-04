/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.type;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.code.api.type.CodeTypeVariables;

/**
 * Implementation of {@link JavaGenericType} for a {@link java.lang.reflect.ParameterizedType}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaParameterizedType extends JavaGenericType {

  private static final Logger LOG = LoggerFactory.getLogger(JavaParameterizedType.class);

  private final JavaType declaringType;

  private final JavaTypeVariables typeVariables;

  private JavaType type;

  private boolean qualified;

  /**
   * The constructor.
   *
   * @param declaringType the {@link #getDeclaringType() declaring type}.
   */
  public JavaParameterizedType(JavaType declaringType) {

    super(declaringType.getContext());
    this.declaringType = declaringType;
    this.typeVariables = new JavaTypeVariables(declaringType);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaParameterizedType} to copy.
   * @param declaringType the {@link #getDeclaringType() declaring type}.
   */
  public JavaParameterizedType(JavaParameterizedType template, JavaType declaringType) {

    super(template);
    this.declaringType = declaringType;
    this.typeVariables = template.typeVariables.copy(declaringType);
  }

  @Override
  public CodeTypeVariables getTypeVariables() {

    return this.typeVariables;
  }

  @Override
  public boolean isQualified() {

    return this.qualified;
  }

  @Override
  public boolean isArray() {

    // TODO Auto-generated method stub
    return false;
  }

  @Override
  protected boolean isCommentForceInline() {

    return true;
  }

  @Override
  protected void doWrite(Appendable sink, String defaultIndent, String currentIndent) throws IOException {

    super.doWrite(sink, defaultIndent, currentIndent);
    writeReference(sink, true);
  }

  @Override
  public void writeReference(Appendable sink, boolean declaration) throws IOException {

    if (this.type == null) {
      LOG.warn("Parameterized type with undefined type declared in {}", this.declaringType.getSimpleName());
      sink.append("Undefined");
    } else {
      this.type.writeReference(sink, false);
    }
  }

  @Override
  public JavaParameterizedType copy(CodeType newDeclaringType) {

    return new JavaParameterizedType(this, (JavaType) newDeclaringType);
  }

  @Override
  public JavaType getDeclaringType() {

    return this.declaringType;
  }

  @Override
  public JavaType asType() {

    return this.type;
  }

  /**
   * @param type the new {@link JavaType}.
   */
  public void setType(JavaType type) {

    verifyMutalbe();
    this.type = type;
  }

  @Override
  public JavaTypeVariable asTypeVariable() {

    return null;
  }

  @Override
  public JavaGenericType resolve(CodeGenericType context) {

    // TODO Auto-generated method stub
    return this;
  }

}
