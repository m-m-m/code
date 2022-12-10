/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.base.exception.DuplicateObjectException;
import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.copy.CodeCopyType;
import io.github.mmm.code.api.item.CodeItem;
import io.github.mmm.code.api.item.CodeItemWithName;
import io.github.mmm.code.api.item.CodeItemWithQualifiedName;
import io.github.mmm.code.api.node.CodeNode;
import io.github.mmm.code.api.node.CodeNodeItem;
import io.github.mmm.code.api.node.CodeNodeItemContainer;
import io.github.mmm.code.api.node.CodeNodeItemContainerWithName;
import io.github.mmm.code.base.item.BaseMutableItem;

/**
 * Base implementation of {@link CodeNodeItemContainer}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <I> the type of the contained {@link CodeItem}.
 * @since 1.0.0
 */
public abstract class BaseNodeItemContainer<I extends CodeItem> extends BaseNodeItem
    implements CodeNodeItemContainer<I> {

  private static final Logger LOG = LoggerFactory.getLogger(BaseNodeItemContainer.class);

  private final Map<String, I> map;

  private final List<I> mutableList;

  private List<I> list;

  private Runnable listLazyInit;

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
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseNodeItemContainer(BaseNodeItemContainer<I> template, CodeCopyMapper mapper) {

    super(template, mapper);
    CodeCopyType itemCopyType = getItemCopyType();
    if (itemCopyType == null) {
      this.mutableList = new ArrayList<>(template.list);
      this.list = this.mutableList;
    } else {
      this.mutableList = new ArrayList<>(template.list.size());
      this.list = this.mutableList;
      // lazy-init: deferred copy
      this.listLazyInit = () -> doLazyInitList(template.list, mapper, itemCopyType);
    }
    if (template.map == null) {
      this.map = null;
    } else {
      this.map = new HashMap<>();
    }
  }

  @SuppressWarnings("unchecked")
  private void doLazyInitList(List<I> templateList, CodeCopyMapper mapper, CodeCopyType type) {

    for (I node : templateList) {
      CodeNode mappedNode = mapper.map((CodeNode) node, type);
      if (mappedNode != null) {
        addInternal((I) mappedNode);
      }
    }
  }

  /**
   * @return the {@link CodeCopyType} for the items in this container.
   */
  protected CodeCopyType getItemCopyType() {

    return CodeCopyType.CHILD;
  }

  @Override
  protected void doSetImmutable() {

    super.doSetImmutable();
    this.list = makeImmutable(this.mutableList, !isKeepListView());
  }

  @Override
  protected boolean isSystemImmutable() {

    boolean systemImmutable = super.isSystemImmutable();
    if (!systemImmutable) {
      systemImmutable = isSystemImmutable((BaseMutableItem) getParent());
    }
    return systemImmutable;
  }

  /**
   * @return {@code true} if the {@link List} should turn into an immutable view on the original mutable {@link List},
   *         {@code false} otherwise.
   * @see #makeImmutable(List, boolean)
   */
  protected boolean isKeepListView() {

    return false;
  }

  /**
   * @return {@code true} if this is a named container that requires a {@link #getMap() map}, {@code false} otherwise.
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

    if (this.listLazyInit != null) {
      this.listLazyInit.run();
      this.listLazyInit = null;
    }
    return this.list;
  }

  /**
   * @return the optional {@link Map} of the items.
   */
  protected Map<String, I> getMap() {

    return this.map;
  }

  /**
   * {@link List#clear() clears} all {@link #getDeclared() declared} items.
   */
  public void clear() {

    verifyMutalbe();
    this.list.clear();
    if (this.map != null) {
      this.map.clear();
    }
  }

  /**
   * @param child the child to rename.
   * @param oldName the old {@link CodeItemWithName#getName() name}.
   * @param newName the new {@link CodeItemWithName#getName() name} to be set.
   * @param renamer the {@link Consumer} to actually perform the renaming (that may change the {@link #hashCode()} of
   *        the child).
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
    I child = ensureParent(item);
    addInternal(child);
  }

  /**
   * @param item the item to {@link #add(CodeItem) add}.
   * @return the item itself or a {@link CodeNodeItem#copy(CodeCopyMapper) copy} with this container as parent.
   */
  @SuppressWarnings("unchecked")
  protected I ensureParent(I item) {

    if (item instanceof CodeNodeItem) {
      CodeNodeItem codeNodeItem = (CodeNodeItem) item;
      if (codeNodeItem.getParent() != this) {
        return (I) doCopyNodeUnsafe(codeNodeItem, this);
      }
    }
    return item;
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
