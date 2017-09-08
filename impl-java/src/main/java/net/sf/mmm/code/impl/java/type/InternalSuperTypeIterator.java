/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.type;

import java.util.Iterator;

import net.sf.mmm.util.collection.base.AbstractIterator;

/**
 * Internal class to iterate over all {@link JavaType#getSuperTypes() super types} of a JavaType super types
 * left recursively.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class InternalSuperTypeIterator extends AbstractIterator<InternalSuperTypeIterator> {

  private InternalSuperTypeIterator parent;

  private JavaType type;

  private Iterator<? extends JavaGenericType> superTypes;

  /**
   * The constructor.
   *
   * @param type the {@link JavaType} to start the iteration with.
   */
  public InternalSuperTypeIterator(JavaType type) {

    this(type, null);
  }

  private InternalSuperTypeIterator(JavaType type, InternalSuperTypeIterator parent) {

    super();
    this.parent = parent;
    this.superTypes = this.type.getSuperTypes().getDeclared().iterator();
    findFirst();
  }

  /**
   * @return the currently iterated {@link JavaType}.
   */
  public JavaType getType() {

    return this.type;
  }

  @Override
  protected InternalSuperTypeIterator findNext() {

    if (this.superTypes.hasNext()) {
      JavaType superType = this.superTypes.next().asType();
      return new InternalSuperTypeIterator(superType, this);
    }
    return this.parent;
  }

}
