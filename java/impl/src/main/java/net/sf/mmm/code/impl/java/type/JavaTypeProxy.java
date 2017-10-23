/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.type;

import java.io.IOException;
import java.util.Objects;

import net.sf.mmm.code.api.comment.CodeComment;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.syntax.CodeSyntax;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.impl.java.annotation.JavaAnnotations;
import net.sf.mmm.code.impl.java.element.JavaElement;

/**
 * {@link JavaGenericTypeProxy} {@link #getDelegate() delegating} to a {@link JavaType} in order to change
 * attributes. Used to represent references to {@link JavaType} in the code that are {@link #isQualified()
 * fully qualified}, have individual {@link #getComment() comments}, or {@link #getAnnotations() annotations}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaTypeProxy extends JavaGenericTypeProxy implements CodeNodeItemWithGenericParent<JavaElement, JavaTypeProxy> {

  private final JavaElement parent;

  private JavaType type;

  private boolean qualified;

  /**
   * The constructor.
   *
   * @param type the {@link #asType() raw type}.
   */
  public JavaTypeProxy(JavaType type) {

    this(type, type);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param type the {@link #asType() raw type}.
   */
  public JavaTypeProxy(JavaElement parent, JavaType type) {

    super();
    Objects.requireNonNull(type, "type");
    this.parent = parent;
    this.type = type;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaTypeProxy} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaTypeProxy(JavaTypeProxy template, JavaElement parent) {

    super(template);
    this.parent = parent;
    this.type = template.type;
  }

  @Override
  public JavaElement getParent() {

    return this.parent;
  }

  @Override
  public JavaType getDelegate() {

    return this.type;
  }

  @Override
  public CodeComment getComment() {

    CodeComment comment = getComment(false);
    if (comment != null) {
      return comment;
    }
    return getComment(true);
  }

  @Override
  public JavaAnnotations getAnnotations() {

    JavaAnnotations annotations = getAnnotations(false);
    if (!annotations.getDeclared().isEmpty()) {
      return annotations;
    }
    return getAnnotations(true);
  }

  @Override
  public JavaType asType() {

    return this.type;
  }

  @Override
  public boolean isQualified() {

    return this.qualified;
  }

  /**
   * @param qualified the new value of {@link #getQualifiedName()}.
   */
  public void setQualified(boolean qualified) {

    verifyMutalbe();
    this.qualified = qualified;
  }

  @Override
  public JavaGenericType resolve(CodeGenericType context) {

    JavaType resolvedType = this.type.resolve(context);
    if (resolvedType == this.type) {
      return this;
    }
    JavaTypeProxy proxy = new JavaTypeProxy(this.parent, resolvedType);
    proxy.qualified = this.qualified;
    return proxy;
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeSyntax syntax) throws IOException {

    if (defaultIndent == null) {
      writeReference(sink, true);
      return;
    }
    super.doWrite(sink, newline, defaultIndent, currentIndent, syntax, false);
    this.type.doWriteDeclaration(sink, currentIndent);
    this.type.doWriteBody(sink, newline, defaultIndent, currentIndent, syntax);
  }

  @Override
  public void writeReference(Appendable sink, boolean declaration) throws IOException {

    sink.append(this.type.getQualifiedName());
    if (declaration) {
      getTypeParameters().write(sink, "", null, "");
    }
  }

  @Override
  public JavaTypeProxy copy() {

    return copy(this.parent);
  }

  @Override
  public JavaTypeProxy copy(JavaElement newParent) {

    return new JavaTypeProxy(this, newParent);
  }

  /**
   * @param type the {@link JavaType} to qualify.
   * @return an instance of {@link JavaTypeProxy} for the given {@link JavaType} that {@link #isQualified() is
   *         qualified}.
   */
  static JavaTypeProxy ofQualified(JavaType type) {

    JavaTypeProxy result = new JavaTypeProxy(type);
    result.qualified = true;
    result.setImmutable();
    return result;
  }

}
