/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.base.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.item.CodeItemWithName;
import net.sf.mmm.code.api.item.CodeItemWithQualifiedName;
import net.sf.mmm.code.api.node.CodeNodeItemContainer;
import net.sf.mmm.code.api.node.CodeNodeItemContainerWithName;
import net.sf.mmm.util.exception.api.DuplicateObjectException;

/**
 * Base implementation of {@link CodeNodeItemContainer}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <I> the type of the contained {@link CodeItem}.
 * @since 1.0.0
 */
public abstract class BaseNodeItemContainer<I extends CodeItem> extends BaseNodeItemImpl implements CodeNodeItemContainer<I> {

  private static final Logger LOG = LoggerFactory.getLogger(BaseNodeItemContainer.class);

  private final Map<String, I> map;

  private final List<I> mutableList;

  private List<I> list;

  /**
   * The constructor.
   */
  protected BaseNodeItemContainer() {

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
   * @param template the {@link BaseNodeItemContainer} to copy.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public BaseNodeItemContainer(BaseNodeItemContainer<I> template) {

    super(template);
    this.mutableList = doCopy((List) template.list, this);
    this.list = this.mutableList;
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
    this.list = makeImmutable(this.mutableList, !isKeepListView());
  }

  @Override
  public abstract BaseNodeItem getParent();

  @Override
  protected boolean isSystemImmutable() {

    boolean systemImmutable = super.isSystemImmutable();
    if (!systemImmutable) {
      systemImmutable = isSystemImmutable((BaseNodeItemImpl) getParent());
    }
    return systemImmutable;
  }

  /**
   * @return {@code true} if the {@link List} should turn into an immutable view on the original mutable
   *         {@link List}, {@code false} otherwise.
   * @see #makeImmutable(List, boolean)
   */
  protected boolean isKeepListView() {

    return false;
  }

  /**
   * @return {@code true} if this is a named container that requires a {@link #getMap() map}, {@code false}
   *         otherwise.
   */
  protected boolean isNamed() {

    return this instanceof CodeNodeItemContainerWithName;
  }

  @Override
  public final List<? extends I> getDeclared() {

    initialize();
    return getList();
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
   * @param oldName the old {@link CodeItemWithName#getName() name}.
   * @param newName the new {@link CodeItemWithName#getName() name} to be set.
   * @param renamer the {@link Consumer} to actually perform the renaming (that may change the
   *        {@link #hashCode()} of the child).
   */
  protected void rename(I child, String oldName, String newName, Consumer<String> renamer) {

    String newNameValid;
    if (child instanceof CodeItemWithQualifiedName) {
      newNameValid = getLanguage().verifySimpleName((CodeItemWithQualifiedName) child, newName);
    } else {
      newNameValid = getLanguage().verifyName((CodeItemWithName) child, newName);
    }
    assert (this.map != null);
    if (this.map != null) {
      if (this.map.containsKey(newNameValid)) {
        throw new DuplicateObjectException(child.getClass().getSimpleName(), newNameValid);
      }
      I old = this.map.remove(oldName);
      assert (old == child);
      renamer.accept(newNameValid);
      old = this.map.put(newNameValid, child);
      assert (old == null);
    } else {
      renamer.accept(newNameValid);
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
      // duplicate = this.list.contains(item);
      duplicate = false;
    }
    if (duplicate) {
      LOG.debug("Omitting duplicate child item '{}' in {}.", item, getClass().getSimpleName());
      return;
    }
    this.mutableList.add(item);
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

}
