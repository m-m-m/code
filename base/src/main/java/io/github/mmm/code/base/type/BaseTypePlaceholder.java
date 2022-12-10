/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.type;

import java.io.IOException;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.language.CodeLanguage;
import io.github.mmm.code.api.type.CodeGenericType;
import io.github.mmm.code.api.type.CodeTypePlaceholder;
import io.github.mmm.code.api.type.CodeTypeVariable;
import io.github.mmm.code.api.type.CodeTypeVariables;
import io.github.mmm.code.base.BaseContext;

/**
 * Base implementation of {@link CodeTypeVariable}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BaseTypePlaceholder extends BaseGenericType implements CodeTypePlaceholder {

  private BaseGenericType bound;

  /**
   * The constructor.
   */
  public BaseTypePlaceholder() {

    this(null);
  }

  /**
   * The constructor.
   *
   * @param bound the {@link #getBound() bound}.
   */
  public BaseTypePlaceholder(BaseGenericType bound) {

    super();
    this.bound = bound;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseTypePlaceholder} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseTypePlaceholder(BaseTypePlaceholder template, CodeCopyMapper mapper) {

    super(template, mapper);
    this.bound = template.bound;
  }

  @Override
  public BaseTypePlaceholder asTypePlaceholder() {

    return this;
  }

  @Override
  public BaseGenericType resolve(CodeGenericType context) {

    return this.bound;
  }

  /**
   * <b>Note:</b> Return type will be {@link TypeVariable} or {@link WildcardType}.
   */
  @Override
  public abstract Type getReflectiveObject();

  /**
   * @return the {@link Type}s for the {@link #getBound() bound} from the {@link #getReflectiveObject()}
   * @see java.lang.reflect.TypeVariable#getBounds()
   * @see java.lang.reflect.WildcardType#getLowerBounds()
   * @see java.lang.reflect.WildcardType#getUpperBounds()
   */
  protected abstract Type[] getReflectiveBounds();

  @Override
  public BaseGenericType getBound() {

    if (this.bound == null) {
      BaseContext context = getContext();
      Type[] bounds = getReflectiveBounds();
      if (bounds == null) {
        this.bound = context.getRootType();
      } else {
        if (bounds.length == 1) {
          this.bound = context.getType(bounds[0], this);
        } else if (bounds.length > 1) {
          this.bound = new BaseComposedType(this, bounds);
        } else {
          Type reflectiveObject = getReflectiveObject();
          throw new IllegalStateException(
              reflectiveObject.getClass().getSimpleName() + " (" + reflectiveObject + ") has empty bounds!");
        }
      }
    }
    return this.bound;
  }

  @Override
  public void setBound(CodeGenericType bound) {

    verifyMutalbe();
    this.bound = (BaseGenericType) bound;
  }

  @Override
  public BaseType asType() {

    return getBound().asType();
  }

  /**
   * @deprecated a {@link CodeTypeVariable} can not have {@link CodeTypeVariables}. The result will always be empty and
   *             {@link #isImmutable() immutable}.
   */
  @Override
  @Deprecated
  public final BaseTypeVariables getTypeParameters() {

    return BaseTypeVariables.EMPTY;
  }

  @Override
  public abstract BaseTypePlaceholder copy();

  @Override
  protected void doWrite(Appendable sink, String newline, String defaultIndent, String currentIndent,
      CodeLanguage language) throws IOException {

    doWriteComment(sink, newline, defaultIndent, currentIndent, language);
    doWriteAnnotations(sink, newline, defaultIndent, currentIndent, language);
    writeReference(sink, (defaultIndent != null), null);
  }

  @Override
  public void writeReference(Appendable sink, boolean declaration, Boolean qualified) throws IOException {

    sink.append(getName());
    if (declaration && (getBound() != null)) {
      if (isSuper()) {
        sink.append(" super ");
      } else if (isExtends()) {
        BaseContext context = getContext();
        if ((context != null) && context.getRootType().equals(this.bound)) {
          return;
        }
        sink.append(" extends ");
      }
      this.bound.writeReference(sink, false, qualified);
    }
  }

}
