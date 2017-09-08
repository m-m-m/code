/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.node;

import net.sf.mmm.code.api.item.CodeItem;
import net.sf.mmm.code.api.item.CodeItemWithName;
import net.sf.mmm.util.exception.api.ObjectNotFoundException;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * {@link CodeNodeItemContainer} containing {@link CodeItemWithName#getName() named} {@link CodeItem}s.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <I> the type of the contained {@link CodeItem}s.
 * @since 1.0.0
 */
public abstract interface CodeNodeItemContainerWithName<I extends CodeItem> extends CodeNodeItemContainer<I> {

  /**
   * @param name the {@link CodeItemWithName#getName() name} of the requested {@link CodeItem}.
   * @return the {@link CodeItem} from {@link #getAll() all items} with the given {@code name} or {@code null}
   *         if no such item exists.
   */
  I get(String name);

  /**
   * @param name the {@link CodeItemWithName#getName() name} of the requested {@link CodeItem}.
   * @return the {@link CodeItem} from {@link #getAll() all items} with the given {@code name}.
   * @throws ObjectNotFoundException if the requested item was not found.
   */
  I getRequired(String name);

  /**
   * @param name the {@link CodeItemWithName#getName() name} of the requested {@link CodeItem}.
   * @return the {@link #get(String) existing} or {@link #add(String) added} {@link CodeItem}.
   */
  default I getOrCreate(String name) {

    I item = get(name);
    if (item == null) {
      item = add(name);
    }
    return item;
  }

  /**
   * @param name the {@link CodeItemWithName#getName() name} of the {@link CodeItemWithName} to create.
   * @return a new {@link CodeItem} that has been added.
   * @throws ReadOnlyException if {@link #isImmutable() immutable}.
   */
  I add(String name);

  @Override
  CodeNodeItemContainerWithName<I> copy();

}
