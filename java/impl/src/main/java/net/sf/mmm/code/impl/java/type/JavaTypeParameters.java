/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import net.sf.mmm.code.api.member.CodeOperation;
import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.type.CodeTypeParameters;
import net.sf.mmm.code.impl.java.JavaContext;

/**
 * Implementation of {@link CodeTypeParameters} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaTypeParameters extends JavaGenericTypeParameters<JavaGenericType>
    implements CodeTypeParameters<JavaGenericType>, CodeNodeItemWithGenericParent<JavaParameterizedType, JavaTypeParameters> {

  /** The empty and {@link #isImmutable() immutable} instance of {@link JavaTypeParameters}. */
  public static final JavaTypeParameters EMPTY = new JavaTypeParameters();

  private final JavaParameterizedType parent;

  /**
   * The constructor for #EMPTY
   */
  private JavaTypeParameters() {

    super();
    this.parent = null;
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public JavaTypeParameters(JavaParameterizedType parent) {

    super();
    this.parent = parent;
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaTypeParameters} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaTypeParameters(JavaTypeParameters template, JavaParameterizedType parent) {

    super(template);
    this.parent = parent;
  }

  @Override
  protected void doInitialize() {

    super.doInitialize();
    ParameterizedType reflectiveObject = this.parent.getReflectiveObject();
    if (reflectiveObject != null) {
      JavaContext context = getContext();
      for (Type type : reflectiveObject.getActualTypeArguments()) {
        JavaGenericType genericType = context.getType(type, this.parent);
        addInternal(genericType);
      }
    }
  }

  @Override
  public JavaParameterizedType getParent() {

    return this.parent;
  }

  @Override
  public CodeOperation getDeclaringOperation() {

    return this.parent.getDeclaringOperation();
  }

  @Override
  public JavaTypeParameters copy() {

    return copy(this.parent);
  }

  @Override
  public JavaTypeParameters copy(JavaParameterizedType newParent) {

    return new JavaTypeParameters(this, newParent);
  }

}
