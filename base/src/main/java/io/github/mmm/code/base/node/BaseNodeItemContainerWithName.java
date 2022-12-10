/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.code.base.node;

import io.github.mmm.code.api.copy.CodeCopyMapper;
import io.github.mmm.code.api.item.CodeItem;
import io.github.mmm.code.api.node.CodeNodeItemContainerWithName;

/**
 * Base implementation of {@link CodeNodeItemContainerWithName}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <I> the type of the contained {@link CodeItem}.
 * @since 1.0.0
 */
public abstract class BaseNodeItemContainerWithName<I extends CodeItem> extends BaseNodeItemContainer<I> implements CodeNodeItemContainerWithName<I> {

  /**
   * The constructor.
   */
  protected BaseNodeItemContainerWithName() {

    super();
  }

  /**
   * The copy-constructor.
   *
   * @param template the {@link BaseNodeItemContainerWithName} to copy.
   * @param mapper the {@link CodeCopyMapper}.
   */
  public BaseNodeItemContainerWithName(BaseNodeItemContainerWithName<I> template, CodeCopyMapper mapper) {

    super(template, mapper);
  }

  @Override
  public I get(String name) {

    return get(name, true);
  }

  /**
   * @param name the {@link io.github.mmm.code.api.item.CodeItemWithName#getName() name} of the requested
   *        {@link CodeItem}.
   * @param init {@code true} to ensure this container is {@link #initialize() initialized}, {@code false}
   *        otherwise.
   * @return the {@link CodeItem} from {@link #getDeclared() declared items} with the given {@code name} or
   *         {@code null} if no such item exists.
   */
  protected I get(String name, boolean init) {

    initialize(init);
    return getByName(name);
  }

  @Override
  public final I getDeclared(String name) {

    return getDeclared(name, true);
  }

  /**
   * @param name the {@link io.github.mmm.code.api.item.CodeItemWithName#getName() name} of the requested
   *        {@link CodeItem}.
   * @param init {@code true} to ensure this container is {@link #initialize() initialized}, {@code false}
   *        otherwise.
   * @return the {@link CodeItem} from the {@link #getDeclared() declared items} with the given {@code name}
   *         or {@code null} if no such item exists.
   */
  protected I getDeclared(String name, boolean init) {

    initialize(init);
    return getByName(name);
  }

}
