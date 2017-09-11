/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.impl.java.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.item.CodeItemWithName;
import net.sf.mmm.code.api.node.CodeNodeItemContainer;
import net.sf.mmm.code.api.node.CodeNodeItemContainerWithName;
import net.sf.mmm.code.api.node.CodeNodeItemWithQualifiedName;
import net.sf.mmm.code.impl.java.element.JavaElement;
import net.sf.mmm.code.impl.java.item.JavaItem;
import net.sf.mmm.code.impl.java.member.JavaMember;
import net.sf.mmm.code.impl.java.type.JavaType;
import net.sf.mmm.util.exception.api.DuplicateObjectException;
import net.sf.mmm.util.exception.api.ObjectNotFoundException;

/**
 * Implementation of {@link CodeNodeItemContainer} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <J> the type of the contained {@link JavaItem}.
 * @param <I> the type of the contained {@link CodeItem}s.
 * @since 1.0.0
 */
public abstract class JavaNodeItemContainer<I extends CodeItem, J extends I> extends JavaNodeItem implements CodeNodeItemContainer<I> {

  private static final Logger LOG = LoggerFactory.getLogger(JavaNodeItemContainer.class);

  private List<J> list;

  private final Map<String, J> map;

  /**
   * The constructor.
   */
  protected JavaNodeItemContainer() {

    super();
    this.list = new ArrayList<>();
    if (isNamed()) {
      this.map = new HashMap<>();
    } else {
      this.map = null;
    }
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link JavaNodeItemContainer} to copy.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public JavaNodeItemContainer(JavaNodeItemContainer<I, J> template) {

    super(template);
    this.list = doCopy((List) template.list, this);
    if (template.map == null) {
      this.map = null;
    } else {
      this.map = new HashMap<>();
      for (J item : this.list) {
        put(item);
      }
    }
  }

  @Override
  protected void doSetImmutable() {

    super.doSetImmutable();
    this.list = makeImmutable(this.list);
  }

  /**
   * @return {@code true} if this is a named container that requires a {@link #getMap() map}, {@code false}
   *         otherwise.
   */
  protected boolean isNamed() {

    return this instanceof CodeNodeItemContainerWithName;
  }

  /**
   * @return the actual {@link List} of items.
   */
  protected List<J> getList() {

    return this.list;
  }

  /**
   * @return the optional {@link Map} of the items.
   */
  protected Map<String, J> getMap() {

    return this.map;
  }

  /**
   * @param child the child to rename.
   * @param oldName the old {@link JavaMember#getName() name}.
   * @param newName the new {@link JavaMember#getName() name} to be {@link JavaMember#setName(String) set}.
   * @param renamer the {@link Consumer} to actually perform the renaming (that may change the
   *        {@link #hashCode()} of the child).
   */
  protected void rename(J child, String oldName, String newName, Consumer<String> renamer) {

    if (this.map != null) {
      if (this.map.containsKey(newName)) {
        throw new DuplicateObjectException(child.getClass().getSimpleName(), newName);
      }
      I old = this.map.remove(oldName);
      assert (old == child);
      renamer.accept(newName);
      old = this.map.put(newName, child);
      assert (old == null);
    }
  }

  /**
   * @param name the {@link CodeItemWithName#getName() name} of the requested item.
   * @return the requested item or {@code null} if not found.
   */
  protected J getByName(String name) {

    return this.map.get(name);
  }

  /**
   * @param name the {@link CodeItemWithName#getName() name} of the requested item.
   * @return the requested item.
   * @throws ObjectNotFoundException if the requested item was not found.
   * @see CodeNodeItemContainerWithName#getRequired(String)
   */
  protected J getRequired(String name) {

    initialize();
    J item = getByName(name);
    if (item == null) {
      throw new ObjectNotFoundException(getClass().getSimpleName(), name);
    }
    return item;
  }

  /**
   * @param item the item to add.
   */
  protected void add(J item) {

    verifyMutalbe();
    addInternal(item);
  }

  /**
   * @param item the item to add.
   */
  protected void addInternal(J item) {

    boolean duplicate;
    if (this.map != null) {
      duplicate = put(item);
    } else {
      duplicate = this.list.contains(item);
    }
    if (duplicate) {
      LOG.debug("Omitting duplicate child item {} in {}.", item, getClass().getSimpleName());
    }
    this.list.add(item);
  }

  private boolean put(J item) {

    String key;
    if (item instanceof CodeNodeItemWithQualifiedName) {
      key = ((CodeNodeItemWithQualifiedName) item).getSimpleName();
    } else {
      key = ((CodeItemWithName) item).getName();
    }
    Object duplicate = this.map.get(key);
    if (duplicate != null) {
      if (duplicate == item) {
        return true;
      }
      throw new DuplicateObjectException(item.getClass().getSimpleName(), key);
    }
    this.map.put(key, item);
    return false;
  }

  @Override
  public boolean remove(I item) {

    verifyMutalbe();
    return getList().remove(item);
  }

  @Override
  public abstract JavaElement getParent();

  @Override
  public JavaType getDeclaringType() {

    JavaElement parent = getParent();
    if (parent instanceof JavaType) {
      return (JavaType) parent;
    } else {
      return parent.getDeclaringType();
    }
  }

  @Override
  public final boolean equals(Object obj) {

    return this == obj;
  }

  @Override
  public final int hashCode() {

    return System.identityHashCode(this);
  }

}
