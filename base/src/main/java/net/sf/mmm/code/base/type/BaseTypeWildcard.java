/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.type;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

import net.sf.mmm.code.api.copy.CodeCopyMapper;
import net.sf.mmm.code.api.copy.CodeCopyType;
import net.sf.mmm.code.api.item.CodeItemWithDeclaringType;
import net.sf.mmm.code.api.node.CodeNodeItem;
import net.sf.mmm.code.api.type.CodeTypeWildcard;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * Base implementation of {@link CodeTypeWildcard}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class BaseTypeWildcard extends BaseTypePlaceholder implements CodeTypeWildcard {

  private final CodeNodeItem parent;

  private final WildcardType reflectiveObject;

  private boolean superWildcard;

  private Type[] reflectiveBounds;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public BaseTypeWildcard(CodeNodeItem parent) {

    this(parent, null, null);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param bound the {@link #getBound() bound}.
   * @param superWildcard the {@link #isSuper() super wildcard flag}.
   */
  public BaseTypeWildcard(CodeNodeItem parent, BaseGenericType bound, boolean superWildcard) {

    this(parent, null, bound);
    this.superWildcard = superWildcard;
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}.
   */
  public BaseTypeWildcard(CodeNodeItem parent, WildcardType reflectiveObject) {

    this(parent, reflectiveObject, null);
  }

  private BaseTypeWildcard(CodeNodeItem parent, WildcardType reflectiveObject, BaseGenericType bound) {

    super(bound);
    this.parent = parent;
    this.reflectiveObject = reflectiveObject;
    if (this.reflectiveObject != null) {
      this.reflectiveBounds = reflectiveObject.getLowerBounds();
      if (this.reflectiveBounds.length == 0) {
        this.reflectiveBounds = reflectiveObject.getUpperBounds();
      } else {
        this.superWildcard = true;
      }
    }
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseTypeWildcard} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseTypeWildcard(BaseTypeWildcard template, CodeCopyMapper mapper) {

    super(template, mapper);
    this.parent = mapper.map(template.parent, CodeCopyType.PARENT);
    this.reflectiveObject = null;
    this.superWildcard = template.superWildcard;
  }

  @Override
  public CodeNodeItem getParent() {

    return this.parent;
  }

  @Override
  public BaseTypeWildcard asTypeWildcard() {

    return this;
  }

  @Override
  public WildcardType getReflectiveObject() {

    return this.reflectiveObject;
  }

  @Override
  protected Type[] getReflectiveBounds() {

    Type[] bounds = this.reflectiveBounds;
    this.reflectiveBounds = null;
    return bounds;
  }

  @Override
  public final String getName() {

    return NAME_WILDCARD;
  }

  @Override
  public void setName(String name) {

    throw new ReadOnlyException(getClass().getSimpleName(), "name");
  }

  @Override
  public BaseType getDeclaringType() {

    if (this.parent instanceof CodeItemWithDeclaringType) {
      return (BaseType) ((CodeItemWithDeclaringType) this.parent).getDeclaringType();
    } else {
      return null;
    }
  }

  @Override
  public final boolean isExtends() {

    return !this.superWildcard;
  }

  @Override
  public final boolean isSuper() {

    return this.superWildcard;
  }

  @Override
  public final boolean isWildcard() {

    return true;
  }

  @Override
  public String getSimpleName() {

    return getName();
  }

  @Override
  public String getQualifiedName() {

    return getName();
  }

  @Override
  public BaseTypeWildcard copy() {

    return copy(getDefaultCopyMapper());
  }

  @Override
  public BaseTypeWildcard copy(CodeCopyMapper mapper) {

    return new BaseTypeWildcard(this, mapper);
  }

}
