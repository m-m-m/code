/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.type;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

import net.sf.mmm.code.api.node.CodeNodeItemWithGenericParent;
import net.sf.mmm.code.api.type.CodeTypeVariable;
import net.sf.mmm.code.api.type.CodeTypeWildcard;
import net.sf.mmm.code.impl.java.element.JavaElement;
import net.sf.mmm.code.impl.java.item.JavaReflectiveObject;
import net.sf.mmm.code.impl.java.node.JavaNodeItem;
import net.sf.mmm.code.impl.java.node.JavaNodeItemContainer;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * Implementation of {@link CodeTypeVariable} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public class JavaTypeWildcard extends JavaTypePlaceholder
    implements CodeTypeWildcard, CodeNodeItemWithGenericParent<JavaNodeItem, JavaTypeWildcard>, JavaReflectiveObject<WildcardType> {

  private final JavaNodeItem parent;

  private final WildcardType reflectiveObject;

  private boolean superWildcard;

  private Type[] reflectiveBounds;

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   */
  public JavaTypeWildcard(JavaNodeItem parent) {

    this(parent, null, null);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param bound the {@link #getBound() bound}.
   */
  public JavaTypeWildcard(JavaNodeItem parent, JavaGenericType bound) {

    this(parent, null, bound);
  }

  /**
   * The constructor.
   *
   * @param parent the {@link #getParent() parent}.
   * @param reflectiveObject the {@link #getReflectiveObject() reflective object}.
   */
  public JavaTypeWildcard(JavaNodeItem parent, WildcardType reflectiveObject) {

    this(parent, reflectiveObject, null);
  }

  private JavaTypeWildcard(JavaNodeItem parent, WildcardType reflectiveObject, JavaGenericType bound) {

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
   * @param template the {@link JavaTypeWildcard} to copy.
   * @param parent the {@link #getParent() parent}.
   */
  public JavaTypeWildcard(JavaTypeWildcard template, JavaNodeItem parent) {

    super(template);
    this.parent = parent;
    this.reflectiveObject = null;
    this.superWildcard = template.superWildcard;
  }

  @Override
  public JavaNodeItem getParent() {

    return this.parent;
  }

  @Override
  public JavaTypeWildcard asTypeWildcard() {

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
  public JavaType getDeclaringType() {

    if (this.parent instanceof JavaNodeItemContainer) {
      return ((JavaNodeItemContainer<?>) this.parent).getDeclaringType();
    } else {
      return ((JavaElement) this.parent).getDeclaringType();
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
  public JavaTypeWildcard copy() {

    return copy(this.parent);
  }

  @Override
  public JavaTypeWildcard copy(JavaNodeItem newParent) {

    return new JavaTypeWildcard(this, newParent);
  }

}
