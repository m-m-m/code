/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.type;

import java.io.IOException;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeTypePlaceholder;
import net.sf.mmm.code.api.type.CodeTypeVariable;
import net.sf.mmm.code.api.type.CodeTypeVariables;
import net.sf.mmm.code.impl.java.JavaContext;
import net.sf.mmm.code.impl.java.node.JavaNodeItem;
import net.sf.mmm.util.exception.api.IllegalCaseException;

/**
 * Implementation of {@link CodeTypeVariable} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class JavaTypePlaceholder extends JavaGenericType implements CodeTypePlaceholder {

  private JavaGenericType bound;

  /**
   * The constructor.
   */
  public JavaTypePlaceholder() {

    this(null);
  }

  /**
   * The constructor.
   *
   * @param bound the {@link #getBound() bound}.
   */
  public JavaTypePlaceholder(JavaGenericType bound) {

    super();
    this.bound = bound;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaTypePlaceholder} to copy.
   */
  public JavaTypePlaceholder(JavaTypePlaceholder template) {

    super(template);
    this.bound = template.bound;
  }

  @Override
  public abstract JavaNodeItem getParent();

  @Override
  public JavaTypePlaceholder asTypePlaceholder() {

    return this;
  }

  @Override
  public JavaGenericType resolve(CodeGenericType context) {

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
  public JavaGenericType getBound() {

    if (this.bound == null) {
      JavaContext context = getContext();
      Type[] bounds = getReflectiveBounds();
      if (bounds == null) {
        this.bound = context.getRootType();
      } else {
        if (bounds.length == 1) {
          this.bound = context.getType(bounds[0], this);
        } else if (bounds.length > 1) {
          this.bound = new JavaComposedType(this, bounds);
        } else {
          Type reflectiveObject = getReflectiveObject();
          throw new IllegalCaseException(reflectiveObject.getClass().getSimpleName() + " (" + reflectiveObject + ") has empty bounds!");
        }
      }
    }
    return this.bound;
  }

  @Override
  public void setBound(CodeGenericType bound) {

    verifyMutalbe();
    this.bound = (JavaGenericType) bound;
  }

  @Override
  public abstract JavaTypePlaceholder copy();

  @Override
  public void writeReference(Appendable sink, boolean declaration) throws IOException {

    sink.append(getName());
    if (declaration) {
      if (isSuper()) {
        sink.append(" super ");
      } else {
        sink.append(" extends ");
      }
      getBound().writeReference(sink, false);
    }
  }

  @Override
  public JavaType asType() {

    return getBound().asType();
  }

  /**
   * @deprecated a {@link CodeTypeVariable} can not have {@link CodeTypeVariables}. The result will always be
   *             empty and {@link #isImmutable() immutable}.
   */
  @Override
  @Deprecated
  public final JavaTypeVariables getTypeParameters() {

    return JavaTypeVariables.EMPTY;
  }

}
