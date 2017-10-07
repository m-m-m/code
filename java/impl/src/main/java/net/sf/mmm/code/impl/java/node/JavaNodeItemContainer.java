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

import net.sf.mmm.code.api.item.CodeItemWithName;
import net.sf.mmm.code.api.item.CodeItemWithQualifiedName;
import net.sf.mmm.code.api.node.CodeNodeItemContainer;
import net.sf.mmm.code.api.node.CodeNodeItemContainerWithName;
import net.sf.mmm.code.impl.java.element.JavaElementNode;
import net.sf.mmm.code.impl.java.item.JavaItem;
import net.sf.mmm.code.impl.java.member.JavaMember;
import net.sf.mmm.code.impl.java.type.JavaType;
import net.sf.mmm.util.exception.api.DuplicateObjectException;
import net.sf.mmm.util.exception.api.ObjectNotFoundException;

/**
 * Implementation of {@link CodeNodeItemContainer} for Java.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <I> the type of the contained {@link JavaItem}.
 * @since 1.0.0
 */
public abstract class JavaNodeItemContainer<I extends JavaItem> extends JavaNodeItem implements CodeNodeItemContainer<I> {

  private static final Logger LOG = LoggerFactory.getLogger(JavaNodeItemContainer.class);

  private List<I> list;

  private List<I> mutableList;

  private final Map<String, I> map;

  /**
   * The constructor.
   */
  protected JavaNodeItemContainer() {

    super();
    this.mutableList = new ArrayList<>();
    this.list = this.mutableList;
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
  public JavaNodeItemContainer(JavaNodeItemContainer<I> template) {

    super(template);
    this.list = doCopy((List) template.list, this);
    if (template.map == null) {
      this.map = null;
    } else {
      this.map = new HashMap<>();
      for (I item : this.list) {
        put(item);
      }
    }
  }

  @Override
  protected void doSetImmutable() {

    super.doSetImmutable();
    this.list = makeImmutable(this.mutableList);
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
  protected List<I> getList() {

    return this.list;
  }

  /**
   * @return the optional {@link Map} of the items.
   */
  protected Map<String, I> getMap() {

    return this.map;
  }

  /**
   * @param child the child to rename.
   * @param oldName the old {@link JavaMember#getName() name}.
   * @param newName the new {@link JavaMember#getName() name} to be {@link JavaMember#setName(String) set}.
   * @param renamer the {@link Consumer} to actually perform the renaming (that may change the
   *        {@link #hashCode()} of the child).
   */
  protected void rename(I child, String oldName, String newName, Consumer<String> renamer) {

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
  protected I getByName(String name) {

    return this.map.get(name);
  }

  /**
   * @param name the {@link CodeItemWithName#getName() name} of the requested item.
   * @return the requested item.
   * @throws ObjectNotFoundException if the requested item was not found.
   * @see CodeNodeItemContainerWithName#getRequired(String)
   */
  protected I getRequired(String name) {

    initialize();
    I item = getByName(name);
    if (item == null) {
      throw new ObjectNotFoundException(getClass().getSimpleName(), name);
    }
    return item;
  }

  /**
   * @param item the item to add.
   */
  protected void add(I item) {

    initialize();
    verifyMutalbe();
    addInternal(item);
  }

  /**
   * @param item the item to add.
   */
  protected void addInternal(I item) {

    boolean duplicate;
    if (this.map != null) {
      duplicate = put(item);
    } else {
      duplicate = this.list.contains(item);
    }
    if (duplicate) {
      LOG.debug("Omitting duplicate child item '{}' in {}.", item, getClass().getSimpleName());
    }
    this.list.add(item);
  }

  private boolean put(I item) {

    String key = getKey(item);
    if (key == null) {
      return this.list.contains(item);
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

  /**
   * @param item the item to get the key for.
   * @return the key used to associated the given {@code item} in the {@link #getMap() map}.
   */
  protected String getKey(I item) {

    String key;
    if (item instanceof CodeItemWithQualifiedName) {
      key = ((CodeItemWithQualifiedName) item).getSimpleName();
    } else {
      key = ((CodeItemWithName) item).getName();
    }
    return key;
  }

  @Override
  public boolean remove(I item) {

    verifyMutalbe();
    if (this.map != null) {
      String key = getKey(item);
      if (key != null) {
        this.map.remove(key);
      }
    }
    return getList().remove(item);
  }

  @Override
  public abstract JavaElementNode getParent();

  @Override
  public JavaType getDeclaringType() {

    return getParent().getDeclaringType();
  }

}
