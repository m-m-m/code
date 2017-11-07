/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.type;

import java.io.IOException;
import java.util.Objects;

import net.sf.mmm.code.api.comment.CodeComment;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.syntax.CodeSyntax;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.base.annoation.BaseAnnotations;
import net.sf.mmm.code.base.element.BaseElementImpl;
import net.sf.mmm.code.base.element.BaseElementWithTypeVariables;

/**
 * {@link BaseGenericTypeProxy} {@link #getDelegate() delegating} to a {@link BaseType} in order to change
 * attributes. Used to represent references to {@link BaseType} in the code that are {@link #isQualified()
 * fully qualified}, have individual {@link #getComment() comments}, or {@link #getAnnotations() annotations}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseTypeProxy extends BaseGenericTypeProxy implements CodeNodeItemWithGenericParent<BaseElementImpl, BaseTypeProxy> {

  private final BaseElementImpl parent;

  private BaseType type;

  private boolean qualified;

  /**
   * The constructor.
   *
   * @param type the {@link #asType() raw type}.
   */
  public BaseTypeProxy(BaseType type) {

    this((BaseElementImpl) type, type);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param type the {@link #asType() raw type}.
   */
  public BaseTypeProxy(BaseElementImpl parent, BaseType type) {

    super();
    Objects.requireNonNull(type, "type");
    this.parent = parent;
    this.type = type;
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param type the {@link #asType() raw type}.
   */
  public BaseTypeProxy(BaseElementWithTypeVariables parent, BaseType type) {

    this((BaseElementImpl) parent, type);
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseTypeProxy} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public BaseTypeProxy(BaseTypeProxy template, BaseElementImpl parent) {

    super(template);
    this.parent = parent;
    this.type = template.type;
  }

  @Override
  public BaseElementImpl getParent() {

    return this.parent;
  }

  @Override
  public BaseType getDelegate() {

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
  public BaseAnnotations getAnnotations() {

    BaseAnnotations annotations = getAnnotations(false);
    if (!annotations.getDeclared().isEmpty()) {
      return annotations;
    }
    return getAnnotations(true);
  }

  @Override
  public BaseType asType() {

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
  public BaseGenericType resolve(CodeGenericType context) {

    BaseType resolvedType = this.type.resolve(context);
    if (resolvedType == this.type) {
      return this;
    }
    BaseTypeProxy proxy = new BaseTypeProxy(this.parent, resolvedType);
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
    this.type.doWriteDeclaration(sink, currentIndent, syntax);
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
  public BaseTypeProxy copy() {

    return copy(this.parent);
  }

  @Override
  public BaseTypeProxy copy(BaseElementImpl newParent) {

    return new BaseTypeProxy(this, newParent);
  }

  /**
   * @param type the {@link BaseType} to qualify.
   * @return an instance of {@link BaseTypeProxy} for the given {@link BaseType} that {@link #isQualified() is
   *         qualified}.
   */
  static BaseTypeProxy ofQualified(BaseType type) {

    BaseTypeProxy result = new BaseTypeProxy(type);
    result.qualified = true;
    result.setImmutable();
    return result;
  }

}