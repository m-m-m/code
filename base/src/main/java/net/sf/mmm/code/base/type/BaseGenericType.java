/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.type;

import java.lang.reflect.Type;

import net.sf.mmm.code.api.copy.CodeCopyMapper;
import net.sf.mmm.code.api.node.CodeNodeItem;
import net.sf.mmm.code.api.type.CodeComposedType;
import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.code.api.type.CodeTypePlaceholder;
import net.sf.mmm.code.api.type.CodeTypeVariable;
import net.sf.mmm.code.api.type.CodeTypeWildcard;
import net.sf.mmm.code.base.element.BaseElementWithDeclaringType;

/**
 * Base implementation of {@link CodeGenericType}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class BaseGenericType extends BaseElementWithDeclaringType implements CodeGenericType {

  private BaseArrayType arrayType;

  /**
   * The constructor.
   */
  public BaseGenericType() {

    super();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseGenericType} to copy.
   * @param mapper the {@link CodeCopyMapper} to use.
   */
  public BaseGenericType(BaseGenericType template, CodeCopyMapper mapper) {

    super(template, mapper);
  }

  @Override
  public abstract CodeNodeItem getParent();

  @Override
  public boolean isAssignableFrom(CodeGenericType type) {

    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public CodeComposedType asComposedType() {

    return null;
  }

  @Override
  public CodeTypeVariable asTypeVariable() {

    return null;
  }

  @Override
  public CodeTypeWildcard asTypeWildcard() {

    return null;
  }

  @Override
  public CodeTypePlaceholder asTypePlaceholder() {

    return null;
  }

  @Override
  public BaseGenericType asUnqualifiedType() {

    return null;
  }

  @Override
  public abstract BaseType asType();

  @Override
  public BaseArrayType createArray() {

    if (this.arrayType == null) {
      this.arrayType = new BaseArrayType(this);
      this.arrayType.setImmutable();
    }
    return this.arrayType;
  }

  @Override
  public BaseGenericTypeParameters<?> getTypeParameters() {

    return BaseTypeParameters.EMPTY;
  }

  @Override
  public BaseGenericType getComponentType() {

    return null;
  }

  @Override
  public boolean isArray() {

    return false;
  }

  @Override
  public boolean isQualified() {

    return false;
  }

  @Override
  public abstract BaseGenericType resolve(CodeGenericType context);

  @Override
  public abstract Type getReflectiveObject();

  @Override
  public CodeGenericType getSourceCodeObject() {

    // CodeItem sourceItem = getParent().getSourceCodeObject();
    // if (sourceItem instanceof BaseSuperTypes) {
    // ((BaseSuperTypes) sourceItem).getDeclared();
    // }
    return null;
  }

  @Override
  public abstract BaseGenericType copy();

  @Override
  public abstract BaseGenericType copy(CodeCopyMapper mapper);

  @Override
  public abstract BaseType getDeclaringType();

}
