/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.type;

import java.io.IOException;
import java.lang.reflect.Type;

import io.github.mmm.code.api.comment.CodeComment;
import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.item.CodeItem;
import io.github.mmm.code.api.language.CodeLanguage;
import io.github.mmm.code.api.node.CodeNodeItem;
import io.github.mmm.code.api.type.CodeGenericType;
import io.github.mmm.code.base.annoation.BaseAnnotations;
import io.github.mmm.code.base.doc.BaseDoc;

/**
 * Base implementation of {@link BaseGenericType} for a proxy-type to a {@link #getDelegate() delegate}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BaseGenericTypeProxy extends BaseGenericType {

  /**
   * The constructor.
   */
  public BaseGenericTypeProxy() {

    super();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseGenericTypeProxy} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseGenericTypeProxy(BaseGenericTypeProxy template, CodeCopyMapper mapper) {

    super(template, mapper);
  }

  /**
   * @return the {@link BaseGenericType} this proxy delegates to. May be initialized lazy by this method.
   */
  public abstract BaseGenericType getDelegate();

  @Override
  public CodeNodeItem getParent() {

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
  public BaseDoc getDoc() {

    return getDelegate().getDoc();
  }

  @Override
  public CodeComment getComment() {

    return getComment(true);
  }

  /**
   * @param fromDelegate - {@code true} to get from {@link #getDelegate()}, {@code false} otherwise (to get from
   *        {@code super} class).
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
  public BaseAnnotations getAnnotations() {

    return getAnnotations(true);
  }

  /**
   * @param fromDelegate - {@code true} to get from {@link #getDelegate()}, {@code false} otherwise (to get from
   *        {@code super} class).
   * @return the {@link #getAnnotations() annotations}.
   */
  public BaseAnnotations getAnnotations(boolean fromDelegate) {

    if (fromDelegate) {
      return getDelegate().getAnnotations();
    } else {
      return super.getAnnotations();
    }
  }

  @Override
  public BaseType asType() {

    return getDelegate().asType();
  }

  @Override
  public Type getReflectiveObject() {

    return getDelegate().getReflectiveObject();
  }

  @Override
  public BaseGenericType resolve(CodeGenericType context) {

    return getDelegate().resolve(context);
  }

  @Override
  public BaseType getDeclaringType() {

    return getDelegate().getDeclaringType();
  }

  @Override
  public BaseGenericTypeParameters<?> getTypeParameters() {

    return getDelegate().getTypeParameters();
  }

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language) throws IOException {

    doWrite(sink, newline, defaultIndent, currentIndent, language, true);
  }

  /**
   * @see #doWrite(Appendable, String, String, String, CodeLanguage)
   * @param sink the {@link Appendable} where to {@link Appendable#append(CharSequence) append} the code from this
   *        {@link CodeItem}.
   * @param newline the newline {@link String}.
   * @param defaultIndent the {@link String} used for indentation (e.g. a number of spaces to insert per indent level).
   * @param currentIndent the current indent (number of spaces). Initially the empty string ({@code ""}). Before a
   *        recursion the {@code indent} will be appended.
   * @param language the {@link CodeLanguage} to use.
   * @param fromDelegate - {@code true} to get from {@link #getDelegate()}, {@code false} otherwise (to get from
   *        {@code super} class).
   * @throws IOException if thrown by {@link Appendable}.
   */
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent, CodeLanguage language, boolean fromDelegate)
      throws IOException {

    if (fromDelegate) {
      getDelegate().write(sink, newline, defaultIndent, currentIndent, language);
    } else {
      super.doWrite(sink, newline, defaultIndent, currentIndent, language);
    }
  }

  @Override
  public void writeReference(Appendable sink, boolean declaration, Boolean qualified) throws IOException {

    getDelegate().writeReference(sink, declaration, qualified);
  }

}
