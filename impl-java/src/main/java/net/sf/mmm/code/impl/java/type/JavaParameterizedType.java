/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.type;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeParameterizedType;
import net.sf.mmm.code.impl.java.member.JavaOperation;

/**
 * Implementation of {@link JavaGenericType} for a {@link java.lang.reflect.ParameterizedType}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaParameterizedType extends JavaGenericType
    implements CodeParameterizedType, CodeNodeItemWithGenericParent<JavaTypeVariables, JavaParameterizedType> {

  private static final Logger LOG = LoggerFactory.getLogger(JavaParameterizedType.class);

  private final JavaTypeVariables parent;

  private final JavaTypeVariables typeVariables;

  private JavaType type;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public JavaParameterizedType(JavaTypeVariables parent) {

    super();
    this.parent = parent;
    this.typeVariables = parent.createChild();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaParameterizedType} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaParameterizedType(JavaParameterizedType template, JavaTypeVariables parent) {

    super(template);
    this.parent = parent;
    this.typeVariables = template.typeVariables.copy(parent.getParent());
    this.type = template.type;
  }

  @Override
  public JavaTypeVariables getParent() {

    return this.parent;
  }

  @Override
  public JavaTypeVariables getTypeVariables() {

    return this.typeVariables;
  }

  @Override
  public JavaType getDeclaringType() {

    return this.parent.getDeclaringType();
  }

  @Override
  public JavaOperation getDeclaringOperation() {

    return this.parent.getDeclaringOperation();
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

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent) throws IOException {

    super.doWrite(sink, newline, null, null);
    writeReference(sink, true);
  }

  @Override
  public JavaParameterizedType copy() {

    return copy(this.parent);
  }

  @Override
  public JavaParameterizedType copy(JavaTypeVariables newParent) {

    return new JavaParameterizedType(this, newParent);
  }

  @Override
  public void writeReference(Appendable sink, boolean declaration) throws IOException {

    if (this.type == null) {
      LOG.warn("Parameterized type with undefined type declared in {}", getDeclaringType().getSimpleName());
      sink.append("Undefined");
    } else {
      this.type.writeReference(sink, false);
    }
  }

}
