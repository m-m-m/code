/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.type;

import java.io.IOException;
import java.lang.reflect.Type;

import net.sf.mmm.code.api.comment.CodeComment;
import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.syntax.CodeSyntax;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.impl.java.annotation.JavaAnnotations;
import net.sf.mmm.code.impl.java.doc.JavaDoc;
import net.sf.mmm.code.impl.java.item.JavaReflectiveObject;
import net.sf.mmm.code.impl.java.node.JavaNode;

/**
 * Implementation of {@link JavaGenericType} for {@link #isArray() array} in Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class JavaGenericTypeProxy extends JavaGenericType implements JavaReflectiveObject<Type> {

  /**
   * The constructor.
   */
  public JavaGenericTypeProxy() {

    super();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaGenericTypeProxy} to copy.
   */
  public JavaGenericTypeProxy(JavaGenericTypeProxy template) {

    super(template);
  }

  /**
   * @return the {@link JavaGenericType} this proxy delegates to. May be initialized lazy by this method.
   */
  public abstract JavaGenericType getDelegate();

  @Override
  public JavaNode getParent() {

    return getDelegate().getParent();
  }

  @Override
  public String getQualifiedName() {

    return getDelegate().getQualifiedName();
  }

  @Override
  public String getSimpleName() {

    return getDelegate().getSimpleName();
  }

  @Override
  public boolean isQualified() {

    return getDelegate().isQualified();
  }

  @Override
  public JavaDoc getDoc() {

    return getDelegate().getDoc();
  }

  @Override
  public CodeComment getComment() {

    return getComment(true);
  }

  /**
   * @param fromDelegate - {@code true} to get from {@link #getDelegate()}, {@code false} otherwise (to get
   *        from {@code super} class).
   * @return the {@link #getComment() comment}.
   */
  public CodeComment getComment(boolean fromDelegate) {

    if (fromDelegate) {
      return getDelegate().getComment();
    } else {
      return super.getComment();
    }
  }

  @Override
  public JavaAnnotations getAnnotations() {

    return getAnnotations(true);
  }

  /**
   * @param fromDelegate - {@code true} to get from {@link #getDelegate()}, {@code false} otherwise (to get
   *        from {@code super} class).
   * @return the {@link #getAnnotations() annotations}.
   */
  public JavaAnnotations getAnnotations(boolean fromDelegate) {

    if (fromDelegate) {
      return getDelegate().getAnnotations();
    } else {
      return super.getAnnotations();
    }
  }

  @Override
  public JavaType asType() {

    return getDelegate().asType();
  }

  @Override
  public Type getReflectiveObject() {

    return getDelegate().getReflectiveObject();
  }

  @Override
  public JavaGenericType resolve(CodeGenericType context) {

    return getDelegate().resolve(context);
  }

  @Override
  public JavaType getDeclaringType() {

    return getDelegate().getDeclaringType();
  }

  @Override
  public JavaGenericTypeParameters<?> getTypeParameters() {

    return getDelegate().getTypeParameters();
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeSyntax syntax) throws IOException {

    doWrite(sink, newline, defaultIndent, currentIndent, syntax, true);
  }

  /**
   * @see #doWrite(Appendable, String, String, String, CodeSyntax)
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the code from
   *        this {@link CodeItem}.
   * @param newline the newline {@link String}.
   * @param defaultIndent the {@link String} used for indentation (e.g. a number of spaces to insert per
   *        indent level).
   * @param currentIndent the current indent (number of spaces). Initially the empty string ({@code ""}).
   *        Before a recursion the {@code indent} will be appended.
   * @param syntax the {@link CodeSyntax} to use.
   * @param fromDelegate - {@code true} to get from {@link #getDelegate()}, {@code false} otherwise (to get
   *        from {@code super} class).
   * @throws IOException if thrown by {@link Appendable}.
   */
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeSyntax syntax, boolean fromDelegate)
      throws IOException {

    if (fromDelegate) {
      getDelegate().write(sink, newline, defaultIndent, currentIndent, syntax);
    } else {
      super.doWrite(sink, newline, defaultIndent, currentIndent, syntax);
    }
  }

  @Override
  public void writeReference(Appendable sink, boolean declaration, Boolean qualified) throws IOException {

    getDelegate().writeReference(sink, declaration, qualified);
  }

}
