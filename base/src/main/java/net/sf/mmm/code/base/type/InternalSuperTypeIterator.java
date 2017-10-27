/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.type;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sf.mmm.code.api.type.CodeGenericType;
import net.sf.mmm.util.collection.base.AbstractIterator;

/**
 * Internal class to iterate over all {@link net.sf.mmm.code.api.type.CodeType#getSuperTypes() super types} of
 * a {@link net.sf.mmm.code.api.type.CodeType} super types left recursively.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class InternalSuperTypeIterator extends AbstractIterator<InternalSuperTypeIterator> {

  private final Set<CodeGenericType> iteratedTypes;

  private final InternalSuperTypeIterator parent;

  private final BaseGenericType type;

  private final Iterator<? extends BaseGenericType> superTypes;

  /**
   * The constructor.
   *
   * @param type the {@link CodeGenericType} to start the iteration with.
   */
  public InternalSuperTypeIterator(BaseGenericType type) {

    this(type, null);
  }

  private InternalSuperTypeIterator(BaseGenericType type, InternalSuperTypeIterator parent) {

    super();
    this.parent = parent;
    this.type = type;
    this.superTypes = type.asType().getSuperTypes().getDeclared().iterator();
    if (this.parent == null) {
      this.iteratedTypes = new HashSet<>();
    } else {
      this.iteratedTypes = this.parent.iteratedTypes;
    }
  }

  /**
   * @return the currently iterated super-type.
   */
  public BaseGenericType getType() {

    return this.type;
  }

  @Override
  protected InternalSuperTypeIterator findNext() {

    while (this.superTypes.hasNext()) {
      BaseGenericType superType = this.superTypes.next();
      boolean added = this.iteratedTypes.add(superType);
      if (added) {
        InternalSuperTypeIterator child = new InternalSuperTypeIterator(superType, this);
        return child;
      }
    }
    return nextParent();
  }

  private InternalSuperTypeIterator nextParent() {

    if (this.parent == null) {
      return null;
    }
    if (this.parent.hasNext()) {
      return this.parent.next();
    } else {
      return this.parent.nextParent();
    }
  }

  @Override
  public String toString() {

    return getClass().getSimpleName() + ":" + this.type;
  }

}
