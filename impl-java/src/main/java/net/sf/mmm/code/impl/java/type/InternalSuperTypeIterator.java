/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.type;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.util.collection.base.AbstractIterator;

/**
 * Internal class to iterate over all {@link JavaType#getSuperTypes() super types} of a JavaType super types
 * left recursively.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public final class InternalSuperTypeIterator extends AbstractIterator<InternalSuperTypeIterator> {

  private static final Logger LOG = LoggerFactory.getLogger(InternalSuperTypeIterator.class);

  private final Set<JavaGenericType> iteratedTypes;

  private final InternalSuperTypeIterator parent;

  private final JavaGenericType type;

  private final Iterator<? extends JavaGenericType> superTypes;

  private JavaGenericType nextSuperType;

  private boolean nextReturned;

  private boolean initialized;

  /**
   * The constructor.
   *
   * @param type the {@link JavaType} to start the iteration with.
   */
  public InternalSuperTypeIterator(JavaGenericType type) {

    this(type, null);
  }

  private InternalSuperTypeIterator(JavaGenericType type, InternalSuperTypeIterator parent) {

    super();
    this.parent = parent;
    this.type = type;
    this.nextSuperType = null;
    this.nextReturned = true;
    this.superTypes = type.asType().getSuperTypes().getDeclared().iterator();
    if (this.parent == null) {
      this.iteratedTypes = new HashSet<>();
    } else {
      this.iteratedTypes = this.parent.iteratedTypes;
    }
    // findFirst();
  }

  /**
   * @return the currently iterated super-type.
   */
  public JavaGenericType nextSuperType() {

    if (!this.initialized) {
      findFirst();
      this.initialized = true;
    }
    hasNext();
    LOG.debug("getSuperType() invoked on {} returning {}", getIdPath(), this.nextSuperType);
    if (this.nextReturned) {
      return null;
    }
    this.nextReturned = true;
    return this.nextSuperType;
  }

  @Override
  protected InternalSuperTypeIterator findNext() {

    LOG.debug("findNext invoked on {} for {} with {}", getIdPath(), this.type, this.nextSuperType);
    while (this.superTypes.hasNext()) {
      JavaGenericType next = this.superTypes.next();
      boolean added = this.iteratedTypes.add(next);
      if (added) {
        assert (this.nextReturned);
        this.nextSuperType = next;
        this.nextReturned = false;
        LOG.debug("> findNext invoked on {} for {} found {}", getIdPath(), this.type, this.nextSuperType);
        InternalSuperTypeIterator child = new InternalSuperTypeIterator(this.nextSuperType, this);
        LOG.debug("> findNext invoked on {} for {} with {} returning child {}", getIdPath(), this.type, this.nextSuperType, child.getIdPath());
        return child;
      } else {
        LOG.debug("> findNext invoked on {} for {} visited duplicate type {}", getIdPath(), this.type, next);
      }
    }
    return nextParent();
  }

  private InternalSuperTypeIterator nextParent() {

    if (this.parent == null) {
      this.nextSuperType = null;
      return null;
    }
    LOG.debug("> findNext invoked on {} for {} returning to parent {}", getIdPath(), this.type, this.parent.getIdPath());
    // LOG.debug("> findNext invoked on {} for {} setting parent.superType to null", getIdPath(), this.type);
    // this.parent.superType = null;
    if (!this.parent.nextReturned) {
      LOG.debug("Parent nextReturned is false");
      return this.parent;
    } else if (this.parent.hasNext()) {
      return this.parent.next();
    } else {
      return this.parent.nextParent();
    }
  }

  private String getId() {

    return Integer.toHexString(System.identityHashCode(this));
  }

  private String getIdPath() {

    if (this.parent == null) {
      return getId();
    } else {
      StringBuilder buffer = new StringBuilder();
      getIdPath(buffer);
      return buffer.toString();
    }
  }

  private void getIdPath(StringBuilder buffer) {

    if (this.parent != null) {
      this.parent.getIdPath(buffer);
    }
    if (buffer.length() > 0) {
      buffer.append('.');
    }
    buffer.append(getId());
  }

  @Override
  public String toString() {

    return getClass().getSimpleName() + "@" + getIdPath() + " on " + this.type + " with next super-type " + this.nextSuperType;
  }

}
