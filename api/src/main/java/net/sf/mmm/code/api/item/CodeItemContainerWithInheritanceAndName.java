/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.code.api.item;

import net.sf.mmm.code.api.type.CodeType;
import net.sf.mmm.util.exception.api.ReadOnlyException;

/**
 * {@link CodeItemContainerWithInheritance} containing {@link CodeItemWithName#getName() named}
 * {@link CodeItem}s of a particular type.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @param <I> the type of the contained {@link CodeItem}s.
 * @since 1.0.0
 */
public abstract interface CodeItemContainerWithInheritanceAndName<I extends CodeItem> extends CodeItemContainerWithInheritance<I> {

  /**
   * @param name the {@link CodeItemWithName#getName() name} of the requested {@link CodeItem}.
   * @return the {@link CodeItem} from the {@link #getDeclared() declared items} with the given {@code name}
   *         or {@code null} if no such item exists.
   */
  I getDeclared(String name);

  /**
   * @param name the {@link CodeItemWithName#getName() name} of the requested {@link CodeItem}.
   * @return the {@link CodeItem} from {@link #getAll() all items} with the given {@code name} or {@code null}
   *         if no such item exists.
   */
  I get(String name);

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
  CodeItemContainerWithInheritanceAndName<I> copy(CodeType newDeclaringType);

}
