/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.type;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import io.github.mmm.code.api.type.CodeGenericType;

/**
 * Internal class to iterate over all {@link io.github.mmm.code.api.type.CodeType#getSuperTypes() super types} of a
 * {@link io.github.mmm.code.api.type.CodeType} left recursively.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class InternalSuperTypeIterator implements Iterator<InternalSuperTypeIterator> {

  private InternalSuperTypeIterator next;

  private boolean done;

  private final Set<CodeGenericType> iteratedTypes;

  private final InternalSuperTypeIterator parent;

  private final CodeGenericType type;

  private final Iterator<? extends CodeGenericType> superTypes;

  /**
   * The constructor.
   *
   * @param type the {@link CodeGenericType} to start the iteration with.
   */
  public InternalSuperTypeIterator(BaseGenericType type) {

    this(type, null);
  }

  private InternalSuperTypeIterator(CodeGenericType type, InternalSuperTypeIterator parent) {

    super();
    this.parent = parent;
    this.type = type;
    this.superTypes = type.asType().getSuperTypes().getDeclared().iterator();
    if (this.parent == null) {
      this.iteratedTypes = new HashSet<>();
    } else {
      this.iteratedTypes = this.parent.iteratedTypes;
    }
    // due to its recursive nature this will not work if initialized here, will be done in hasNext()
    // this.next = findNext();
  }

  /**
   * @return the currently iterated super-type.
   */
  public CodeGenericType getType() {

    return this.type;
  }

  @Override
  public final boolean hasNext() {

    if (this.next != null) {
      return true;
    }
    if (this.done) {
      return false;
    }
    this.next = findNext();
    if (this.next == null) {
      this.done = true;
    }
    return (!this.done);
  }

  @Override
  public final InternalSuperTypeIterator next() {

    if (this.next == null) {
      throw new NoSuchElementException();
    } else {
      InternalSuperTypeIterator result = this.next;
      this.next = null;
      return result;
    }
  }

  private InternalSuperTypeIterator findNext() {

    while (this.superTypes.hasNext()) {
      CodeGenericType superType = this.superTypes.next();
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
